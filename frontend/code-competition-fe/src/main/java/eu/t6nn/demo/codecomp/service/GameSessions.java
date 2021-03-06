package eu.t6nn.demo.codecomp.service;

import com.google.common.io.Files;
import com.google.gson.Gson;
import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.model.Player;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class GameSessions {

    private static final String SESSION_START_TIME_FILE = "start.time";

    public static final String GAMESESSION_FILENAME = "game-session.json";

    @Value("#{'${session.directory}' ?: systemProperties['java.io.tmpdir']}")
    private File sessionDir;

    @Value("${game.templates.dir}")
    private File gameTemplates;

    private Map<String, Timer> finishTimers = new ConcurrentHashMap<>();

    @PostConstruct
    public void prepareDir() {
        if (sessionDir.exists() && !sessionDir.isDirectory()) {
            throw new IllegalStateException(sessionDir + " is not a directory.");
        } else if (!sessionDir.exists()) {
            if (!sessionDir.mkdirs()) {
                throw new IllegalStateException("Could not create " + sessionDir);
            }
        }
    }

    public GameSession register(Player player, String gameToPlay) {
        GameSession session = new GameSession(player, gameToPlay);
        File sessionDir = sessionDirectoryFor(session.getId());
        createSessionDirectory(sessionDir);
        saveSession(session, sessionDir);

        return session;
    }

    public GameSession byId(String sessionId) {
        File dir = sessionDirectoryFor(sessionId);
        if (dir.isDirectory()) {
            File sessionReg = new File(dir, GAMESESSION_FILENAME);
            if (sessionReg.isFile()) {
                Gson gson = new Gson();
                try (FileReader reader = new FileReader(sessionReg)) {
                    GameSession gameSession = gson.fromJson(reader, GameSession.class);
                    File startTime = new File(dir, SESSION_START_TIME_FILE);
                    if (startTime.exists()) {
                        gameSession.setStarted(true);
                    }
                    return gameSession;
                } catch (IOException e) {
                    throw new IllegalStateException("Unable to read player information");
                }
            }

        }
        throw new IllegalStateException("Session not found.");
    }

    public List<GameSession> findAll() {
        File[] sessionDirCandidates = sessionDir.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if (sessionDirCandidates == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(sessionDirCandidates)
                .filter(this::isSessionDirectory)
                .map(f -> byId(f.getName()))
                .collect(Collectors.toList());
    }

    public long startSessionTimer(String sessionId) {
        try {
            File sessionDir = sessionDirectoryFor(sessionId);
            File startFile = new File(sessionDir, SESSION_START_TIME_FILE);

            if (startFile.exists()) {
                String start = Files.readFirstLine(startFile, Charset.defaultCharset());
                return Long.parseLong(start);
            } else {
                long startTime = new Date().getTime();
                Files.write(startTime + "\n", startFile, Charset.defaultCharset());
                return startTime;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not get/create start time file for session");
        }
    }

    private boolean isSessionDirectory(File dir) {
        return dir.isDirectory() && new File(dir, GAMESESSION_FILENAME).isFile();
    }

    private void createSessionDirectory(File sessionDir) {
        if (!sessionDir.mkdir()) {
            throw new IllegalStateException("Unable to create a new session directory");
        }
    }

    private void saveSession(GameSession session, File sessionDir) {
        File sessionReg = new File(sessionDir, GAMESESSION_FILENAME);
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(sessionReg)) {
            gson.toJson(session, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Could not persist player information");
        }
    }

    public File sessionDirectoryFor(String sessionId) {
        return new File(sessionDir, sessionId);
    }

    public void scheduleFinish(String sessionId, Runnable finisher, long playTime) {
        finishTimers.computeIfAbsent(sessionId, sess -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        finisher.run();
                    } catch (Exception ignored){}
                    finishTimers.remove(sess);
                }
            }, playTime);
            return timer;
        });

    }
}

package eu.t6nn.demo.codecomp.service;

import com.google.gson.Gson;
import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.model.Player;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameSessions {

    public static final String GAMESESSION_FILENAME = "game-session.json";
    @Value("#{'${session.directory}' ?: systemProperties['java.io.tmpdir']}")
    private File sessionDir;

    @Value("${game.templates.dir}")
    private File gameTemplates;

    @PostConstruct
    public void prepareDir() {
        if(sessionDir.exists() && !sessionDir.isDirectory()) {
            throw new IllegalStateException(sessionDir + " is not a directory.");
        } else if(!sessionDir.exists()) {
            if(!sessionDir.mkdirs()) {
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
        if(dir.isDirectory()) {
            File sessionReg = new File(dir, GAMESESSION_FILENAME);
            if(sessionReg.isFile()) {
                Gson gson = new Gson();
                try (FileReader reader = new FileReader(sessionReg)){
                    return gson.fromJson(reader, GameSession.class);
                } catch (IOException e) {
                    throw new IllegalStateException("Unable to read player information");
                }
            }
        }
        throw new IllegalStateException("Session not found.");
    }

    public List<GameSession> findAll() {
        File[] sessionDirCandidates = sessionDir.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if(sessionDirCandidates == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(sessionDirCandidates)
                .filter(this::isSessionDirectory)
                .map(f -> byId(f.getName()))
                .collect(Collectors.toList());
    }

    private boolean isSessionDirectory(File dir) {
        return dir.isDirectory() && new File(dir, GAMESESSION_FILENAME).isFile();
    }

    private void createSessionDirectory(File sessionDir) {
        if(!sessionDir.mkdir()) {
            throw new IllegalStateException("Unable to create a new session directory");
        }
    }

    private void saveSession(GameSession session, File sessionDir) {
        File sessionReg = new File(sessionDir, GAMESESSION_FILENAME);
        Gson gson = new Gson();
        try(FileWriter writer = new FileWriter(sessionReg)) {
            gson.toJson(session, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Could not persist player information");
        }
    }

    public File sessionDirectoryFor(String sessionId) {
        return new File(sessionDir, sessionId);
    }

}

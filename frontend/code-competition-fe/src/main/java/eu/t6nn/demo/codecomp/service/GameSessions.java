package eu.t6nn.demo.codecomp.service;

import com.google.gson.Gson;
import eu.t6nn.demo.codecomp.model.GameListItem;
import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.model.Player;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

@Component
public class GameSessions {

    @Value("#{'${session.directory}' ?: systemProperties['java.io.tmpdir']}")
    private File sessionDir;

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
        GameSession session = new GameSession(player);
        File sessionDir = sessionDirectoryFor(session.getId());
        createSessionDirectory(sessionDir);
        savePlayerRegistration(player, sessionDir);
        return session;
    }

    public GameSession byId(String sessionId) {
        File dir = sessionDirectoryFor(sessionId);
        if(dir.isDirectory()) {
            File playerReg = new File(dir, "player.json");
            if(playerReg.isFile()) {
                Gson gson = new Gson();
                try (FileReader reader = new FileReader(playerReg)){
                    Player player = gson.fromJson(reader, Player.class);
                    return new GameSession(sessionId, player);
                } catch (IOException e) {
                    throw new IllegalStateException("Unable to read player information");
                }
            }
        }
        throw new IllegalStateException("Session not found.");
    }

    private void createSessionDirectory(File sessionDir) {
        if(!sessionDir.mkdir()) {
            throw new IllegalStateException("Unable to create a new session directory");
        }
    }

    private void savePlayerRegistration(Player player, File sessionDir) {
        File playerReg = new File(sessionDir, "player.json");
        Gson gson = new Gson();
        try(FileWriter writer = new FileWriter(playerReg)) {
            gson.toJson(player, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Could not persist player information");
        }
    }

    public File sessionDirectoryFor(String sessionId) {
        return new File(sessionDir, sessionId);
    }

}

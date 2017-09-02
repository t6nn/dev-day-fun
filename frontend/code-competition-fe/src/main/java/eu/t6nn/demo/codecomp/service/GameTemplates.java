package eu.t6nn.demo.codecomp.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class GameTemplates {

    private static final Logger LOG = LoggerFactory.getLogger(GameTemplates.class);

    @Value("${game.templates.dir}")
    private File gameTemplates;

    public void prepareSession(String gameId, File sessionDirectory, File workspaceDirectory) {
        File gameDir = new File(gameTemplates, gameId);
        if(!gameDir.isDirectory()) {
            throw new IllegalArgumentException("Directory " + gameDir + " is not a directory");
        }
        File testDir = new File(gameDir, "tests");
        File verificationDir = new File(gameDir, "verification");
        copyDirectory(workspaceDirectory, testDir);
        copyDirectory(sessionDirectory, verificationDir);
    }

    private void copyDirectory(File sessionDirectory, File testDir) {
        if(testDir.isDirectory()) {
            try {
                FileUtils.copyDirectory(testDir, new File(sessionDirectory, testDir.getName()));
            } catch (IOException e) {
                LOG.warn("Could not copy {} to {}", sessionDirectory, testDir);
            }
        }
    }

}

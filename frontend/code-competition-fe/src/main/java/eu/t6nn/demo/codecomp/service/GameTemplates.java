package eu.t6nn.demo.codecomp.service;

import eu.t6nn.demo.codecomp.model.GameDef;
import eu.t6nn.demo.codecomp.model.Language;
import eu.t6nn.demo.codecomp.model.TaskDef;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class GameTemplates {

    private static final Logger LOG = LoggerFactory.getLogger(GameTemplates.class);

    @Value("${game.templates.dir}")
    private File gameTemplates;

    @Autowired
    private GameList gameList;

    public void prepareSession(String gameId, File sessionDirectory, File workspaceDirectory, Language language) {
        File gameDir = new File(gameTemplates, gameId);
        if(!gameDir.isDirectory()) {
            throw new IllegalArgumentException("Directory " + gameDir + " is not a directory");
        }

        GameDef def = gameList.byId(gameId);

        File workspaceTemplate = new File(gameDir, "workspace");
        copyDirectory(workspaceTemplate, workspaceDirectory);

        for(TaskDef task : def.getTasks()) {
            File taskDir = new File(gameDir, task.getId());

            File testDir = new File(taskDir, "tests");
            File verificationDir = new File(taskDir, "verification");
            File srcDir = new File(taskDir, "src/" + language.name().toLowerCase());

            copyDirectory(srcDir, new File(workspaceDirectory, "src"));
            copyDirectory(testDir, new File(workspaceDirectory, "tests/" + task.getId()));
            copyDirectory(verificationDir, new File(sessionDirectory, "verification/" + task.getId()));
        }

    }

//    private void copyDirectoryWithName(File fromDir, File toDir) {
//        if(fromDir.isDirectory()) {
//            File target = new File(toDir, fromDir.getName());
//            try {
//                FileUtils.copyDirectory(fromDir, target);
//            } catch (IOException e) {
//                LOG.warn("Could not copy {} to {}", fromDir, target);
//            }
//        }
//    }

    private void copyDirectory(File fromDir, File toDir) {
        if(fromDir.isDirectory()) {
            try {
                FileUtils.copyDirectory(fromDir, toDir);
            } catch (IOException e) {
                LOG.warn("Could not copy {} to {}", fromDir, toDir);
            }
        }
    }

}

package eu.t6nn.demo.codecomp.service;

import eu.t6nn.demo.codecomp.model.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GameResults {

    private static final String RESULT_FILENAME = "score.out";

    @Autowired
    private GameSessions gameSessions;

    @Autowired
    private GameList games;

    public Map<GameDef, List<GameResult>> forAll() {
        Map<GameDef, List<GameResult>> results = new HashMap<>();
        List<GameListItem> gameList = games.allGames();
        for(GameListItem game : gameList) {
            results.put(game.getGameDef(), forGame(game.getId()));
        }
        return results;
    }

    public List<GameResult> forGame(@NotNull String gameId) {
        return gameSessions.findAll().stream()
                .filter(gs-> gameId.equals(gs.getGameId()))
                .filter(this::hasScore)
                .map(gs -> new GameResult(gs, gs.getPlayer(), getScore(gs)))
                .filter(r -> r.getScore() != Long.MAX_VALUE)
                .sorted(Comparator.comparingLong(GameResult::getScore))
                .collect(Collectors.toList());
    }

    private boolean hasScore(GameSession session) {
        GameDef def = games.byId(session.getGameId());
        for(TaskDef task : def.getTasks()) {
            if(resultFile(session, task).isFile()) {
                return true;
            }
        }
        return false;
    }

    private File resultFile(GameSession session, TaskDef task) {
        return new File(gameSessions.sessionDirectoryFor(session.getId()), "verification/" + task.getId() + "/" + RESULT_FILENAME);
    }

    private long getScore(GameSession session) {
        GameDef def = games.byId(session.getGameId());
        long score = 0;
        for(TaskDef task : def.getTasks()) {
            try {
                if(resultFile(session, task).isFile()) {
                    score += Long.valueOf(FileUtils.readFileToString(resultFile(session, task), "UTF-8").trim());
                    continue;
                }
            } catch (IOException e) {
                // ignore, will continue with default score
            }
            score += task.getDefaultScore();
        }
        return score;
    }

}

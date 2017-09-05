package eu.t6nn.demo.codecomp.service;

import eu.t6nn.demo.codecomp.model.GameDef;
import eu.t6nn.demo.codecomp.model.GameListItem;
import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.model.GameResult;
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
        return resultFile(session).isFile();
    }

    private File resultFile(GameSession session) {
        return new File(gameSessions.sessionDirectoryFor(session.getId()), RESULT_FILENAME);
    }

    private long getScore(GameSession session) {
        try {
            return Long.valueOf(FileUtils.readFileToString(resultFile(session), "UTF-8").trim());
        } catch (IOException e) {
            return Long.MAX_VALUE;
        }
    }

}

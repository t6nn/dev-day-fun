package eu.t6nn.demo.codecomp.controller;

import eu.t6nn.demo.codecomp.model.GameDef;
import eu.t6nn.demo.codecomp.model.GameResult;
import eu.t6nn.demo.codecomp.service.GameList;
import eu.t6nn.demo.codecomp.service.GameResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LeaderboardController {

    @Autowired
    private GameResults results;

    @Autowired
    private GameList games;

    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {
        model.addAttribute("gameResults", results.forAll());
        return "leaderboard";
    }

    @GetMapping("/leaderboard/{gameId}")
    public String showLeaderboard(@PathVariable String gameId, Model model) {
        Map<GameDef, List<GameResult>> resultsByGame = new HashMap<>();
        resultsByGame.put(games.byId(gameId), results.forGame(gameId));
        model.addAttribute("gameResults", resultsByGame);
        return "leaderboard";
    }
}

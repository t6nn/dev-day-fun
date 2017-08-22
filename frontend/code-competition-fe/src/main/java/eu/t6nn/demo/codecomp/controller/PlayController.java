package eu.t6nn.demo.codecomp.controller;

import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.service.GameSessions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlayController {

    @Autowired
    private GameSessions sessions;

    @RequestMapping("/play/{sessionId}")
    public String play(@PathVariable String sessionId, Model model) {
        GameSession session = sessions.byId(sessionId);
        model.addAttribute("gs", session);
        return "play";
    }

}

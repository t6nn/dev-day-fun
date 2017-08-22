package eu.t6nn.demo.codecomp.controller;

import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.service.GameSessions;
import eu.t6nn.demo.codecomp.service.SessionDirector;
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

    @Autowired
    private SessionDirector director;

    @RequestMapping("/play/{sessionId}")
    public String play(@PathVariable String sessionId, Model model) {
        GameSession session = sessions.byId(sessionId);
        model.addAttribute("gs", session);

        director.direct(session, sess -> {
            System.out.println(sess.workspaceUrl());
        });

        return "play";
    }

    @RequestMapping("/finish/{sessionId}")
    public String finish(@PathVariable String sessionId, Model model) {
        GameSession session = sessions.byId(sessionId);

        director.stop(session);
        return "finish";
    }

}

package eu.t6nn.demo.codecomp.controller;

import eu.t6nn.demo.codecomp.model.DirectedSession;
import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.service.GameList;
import eu.t6nn.demo.codecomp.service.GameSessions;
import eu.t6nn.demo.codecomp.service.SessionDirector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public class PlayController {

    private static final long TIME_TO_PLAY = TimeUnit.MINUTES.toMillis(1);

    @Autowired
    private GameSessions sessions;

    @Autowired
    private SessionDirector director;

    @Autowired
    private GameList gameList;

    @RequestMapping("/play/{sessionId}")
    public String play(@PathVariable String sessionId, Model model) throws ExecutionException, InterruptedException {
        GameSession session = sessions.byId(sessionId);
        model.addAttribute("gs", session);
        model.addAttribute("timeToPlay", TIME_TO_PLAY);

        final CompletableFuture<DirectedSession> runningSession = new CompletableFuture<>();
        director.direct(session, sess -> {
            System.out.println(sess.workspaceUrl());
            runningSession.complete(sess);
        });

        try {
            DirectedSession sess = runningSession.get(5000, TimeUnit.SECONDS);
            model.addAttribute("workspaceUrl", sess.workspaceUrl());
            model.addAttribute("apiUrl", sess.apiUrl());
        } catch (TimeoutException e) {
            model.addAttribute("workspaceUrl", "");
            model.addAttribute("apiUrl", "");
        }

        model.addAttribute("gameDescriptions", gameList.loadGameDescriptions(session.getGameId()));

        return "play";
    }

    @ResponseBody
    @RequestMapping(value = "/start/{sessionId}")
    public long start(@PathVariable String sessionId) {
        sessions.scheduleFinish(sessionId, () -> finish(sessionId), TIME_TO_PLAY + TimeUnit.SECONDS.toMillis(5));
        return sessions.startSessionTimer(sessionId);
    }

    @RequestMapping("/finish/{sessionId}")
    public String finish(@PathVariable String sessionId) {
        GameSession session = sessions.byId(sessionId);

        director.stop(session);
        return "finish";
    }
}

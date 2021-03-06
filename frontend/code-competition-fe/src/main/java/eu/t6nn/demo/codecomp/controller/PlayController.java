package eu.t6nn.demo.codecomp.controller;

import eu.t6nn.demo.codecomp.model.DirectedSession;
import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.service.GameList;
import eu.t6nn.demo.codecomp.service.GameResults;
import eu.t6nn.demo.codecomp.service.GameSessions;
import eu.t6nn.demo.codecomp.service.SessionDirector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public class PlayController {

    @Autowired
    private GameSessions sessions;

    @Autowired
    private SessionDirector director;

    @Autowired
    private GameList gameList;

    @Autowired
    private GameResults gameResults;

    @Value("${che.cookie}")
    private String sessionCookieName;

    @Value("${che.session.max-age-sec}")
    private int sessionMaxAge;

    @RequestMapping("/play/{sessionId}")
    public String play(@PathVariable String sessionId, Model model, HttpServletResponse response) throws ExecutionException, InterruptedException {
        GameSession session = sessions.byId(sessionId);
        model.addAttribute("gs", session);
        model.addAttribute("timeToPlay", TimeUnit.SECONDS.toMillis(sessionMaxAge));

        final CompletableFuture<DirectedSession> runningSession = new CompletableFuture<>();
        director.direct(session, sess -> {
            System.out.println(sess.workspaceUrl());
            runningSession.complete(sess);
        });

        try {
            DirectedSession sess = runningSession.get(5000, TimeUnit.SECONDS);
            model.addAttribute("workspaceUrl", sess.workspaceUrl());
            model.addAttribute("apiUrl", sess.apiUrl());
            model.addAttribute("finished", sess.isFinished());
        } catch (TimeoutException e) {
            model.addAttribute("workspaceUrl", "");
            model.addAttribute("apiUrl", "");
            model.addAttribute("finished", true);
        }

        model.addAttribute("gameDescriptions", gameList.loadGameDescriptions(session.getGameId()));
        setSessionCookie(sessionId, response);

        return "play";
    }

    private void setSessionCookie(String sessionId, HttpServletResponse response) {
        Cookie cookie = new Cookie(sessionCookieName, sessionId);
        cookie.setMaxAge(sessionMaxAge + 120);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void deleteSessionCookie(String sessionId, HttpServletResponse response) {
        Cookie cookie = new Cookie(sessionCookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @ResponseBody
    @RequestMapping(value = "/start/{sessionId}")
    public long start(@PathVariable String sessionId) {
        sessions.scheduleFinish(sessionId, () -> stopSession(sessionId), TimeUnit.SECONDS.toMillis(sessionMaxAge) + TimeUnit.SECONDS.toMillis(5));
        return sessions.startSessionTimer(sessionId);
    }

    @RequestMapping("/finish/{sessionId}")
    public String finish(@PathVariable String sessionId, HttpServletResponse response, Model model) {
        stopSession(sessionId);
        deleteSessionCookie(sessionId, response);
        model.addAttribute("gameResult", gameResults.forSession(sessionId));
        return "finish";
    }

    private void stopSession(String sessionId) {
        GameSession session = sessions.byId(sessionId);
        director.stop(session);
    }
}

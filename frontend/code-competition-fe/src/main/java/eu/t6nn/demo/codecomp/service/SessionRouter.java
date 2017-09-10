package eu.t6nn.demo.codecomp.service;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import eu.t6nn.demo.codecomp.model.DirectedSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class SessionRouter extends ZuulFilter {

    @Autowired
    private SessionDirector sessionDirector;

    @Value("${che.cookie}")
    private String cookieName;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return extractSessionId() != null;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String sessionId = extractSessionId();

        if(sessionId == null) {
            return false;
        }

        DirectedSession session = sessionDirector.findRunningSession(sessionId);
        ctx.setRouteHost(session.backendUrl());

        return null;
    }

    private String extractSessionId() {
        Cookie[] cookies = RequestContext.getCurrentContext().getRequest().getCookies();
        if(cookies == null) {
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}

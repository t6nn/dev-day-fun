package eu.t6nn.demo.codecomp.service;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.filters.FilterRegistry;
import com.netflix.zuul.util.HTTPRequestUtils;
import eu.t6nn.demo.codecomp.model.DirectedSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.SimpleHostRoutingFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.cloud.netflix.zuul.util.RequestUtils;
import org.springframework.stereotype.Component;
import sun.misc.Request;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

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

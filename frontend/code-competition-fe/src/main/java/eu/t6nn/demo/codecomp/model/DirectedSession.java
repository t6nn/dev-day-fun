package eu.t6nn.demo.codecomp.model;

import java.net.URL;

public interface DirectedSession {
    URL backendUrl();

    URL workspaceUrl();
    URL apiUrl();
}

package eu.t6nn.demo.codecomp.model;

import java.net.URL;

public interface DirectedSession {
    boolean isFinished();

    URL backendUrl();

    URL workspaceUrl();
    URL apiUrl();
}

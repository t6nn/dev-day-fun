package eu.t6nn.demo.codecomp.service;

import com.google.gson.Gson;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import eu.t6nn.demo.codecomp.model.DirectedSession;
import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.model.Language;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Component
public class SessionDirector {

    @Autowired
    private GameSessions sessions;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private DockerClient docker;

    @Value("${lang.setup.directory}")
    private File languageSetups;

    @Value("eclipse/che:${che.version}")
    private String cheImage;

    public void direct(GameSession session, final Consumer<DirectedSession> sessionCallback) {
        final File sessionDir = sessions.sessionDirectoryFor(session.getId());

        try {
            setupWorkspace(sessionDir, session.getPlayer().getLang());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to set up workspace.", e);
        }

        executorService.submit(() -> {
            try {
                docker.pull(cheImage);

                final Map<String, List<PortBinding>> portBindings = new HashMap<>();
                List<PortBinding> hostChePort = Collections.singletonList(PortBinding.randomPort("0.0.0.0"));
                portBindings.put("8080", hostChePort);

                final HostConfig hostConfig = HostConfig.builder()
                        .appendBinds(HostConfig.Bind.from(cheDataDir(sessionDir).getAbsolutePath()).to("/data").build())
                        .appendBinds(HostConfig.Bind.from(cheWorkspaceDir(sessionDir).getAbsolutePath()).to("/chedir").build())
                        .portBindings(portBindings).build();

                final ContainerConfig containerConfig = ContainerConfig.builder()
                        .hostConfig(hostConfig)
                        .image(cheImage)
                        .exposedPorts("8080")
                        //.cmd("sh", "-c", "while :; do sleep 1; done")
                        .build();

                final ContainerCreation creation = docker.createContainer(containerConfig);
                final String id = creation.id();

                docker.startContainer(id);
                PortBinding chePortBinding = docker.inspectContainer(id).networkSettings().ports().get("8080").get(0);

                DockerSession dockerSession = new DockerSession(id, chePortBinding);
                storeSession(sessionDir, dockerSession);
                sessionCallback.accept(dockerSession);

            } catch (InterruptedException e) {
                throw new IllegalStateException("Interrupted while setting up docker.");
            } catch (DockerException e) {
                throw new IllegalStateException("Unable to set up docker.", e);
            }
        });
    }

    private void storeSession(File sessionDir, DockerSession dockerSession) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(new File(sessionDir, "session.json"))) {
            gson.toJson(dockerSession, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to store game session");
        }
    }

    private DockerSession loadSession(File sessionDir) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(new File(sessionDir, "session.json"))){
            return gson.fromJson(reader, DockerSession.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load game session");
        }
    }

    public void stop(GameSession gameSession) {
        final File sessionDir = sessions.sessionDirectoryFor(gameSession.getId());
        DockerSession session = loadSession(sessionDir);
        stop(session);
    }

    public void stop(DirectedSession session) {
        if (!(session instanceof DockerSession)) {
            throw new IllegalArgumentException("Unidentified session");
        }
        final DockerSession dockerSession = (DockerSession) session;

        executorService.submit(() -> {
            try {
                docker.stopContainer(dockerSession.containerId, 30);
                docker.removeContainer(dockerSession.containerId);
            } catch (DockerException e) {
                throw new IllegalStateException("Unable to stop docker.", e);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Interrupted while waiting for docker to stop.");
            }
        });
    }

    private void setupWorkspace(File sessionDir, Language lang) throws IOException {
        File languageDir = new File(languageSetups, lang.name().toLowerCase());
        if (!languageDir.isDirectory()) {
            throw new IllegalStateException("Directory " + languageDir + " does not exist");
        }
        File templateDir = new File(languageDir, "ws-template");
        File templateTarget = cheWorkspaceDir(sessionDir);

        File cheData = cheDataDir(sessionDir);
        if (!cheData.isDirectory() && !cheData.mkdirs()) {
            throw new IllegalStateException("Unable to create Che working directory");
        }
        FileUtils.copyDirectory(templateDir, templateTarget);
    }

    private File cheWorkspaceDir(File sessionDir) {
        return new File(sessionDir, "che-workspace");
    }

    private File cheDataDir(File sessionDir) {
        return new File(sessionDir, "che-data");
    }


    private static final class DockerSession implements DirectedSession {
        private final String containerId;
        private final PortBinding cheBinding;

        private DockerSession(String containerId, PortBinding cheBinding) {
            this.containerId = containerId;
            this.cheBinding = cheBinding;
        }

        @Override
        public URL workspaceUrl() {
            try {
                return new URL("http://" + cheBinding.hostIp() + ":" + cheBinding.hostPort());
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}

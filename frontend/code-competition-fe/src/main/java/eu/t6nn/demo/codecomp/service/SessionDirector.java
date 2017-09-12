package eu.t6nn.demo.codecomp.service;

import com.google.gson.Gson;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import eu.t6nn.demo.codecomp.model.DirectedSession;
import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.model.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Component
public class SessionDirector {

    private static final String CHE_SESSION_FILENAME = "che-session.json";

    private static final Logger LOG = LoggerFactory.getLogger(SessionDirector.class);

    @Autowired
    private GameSessions sessions;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private DockerClient docker;

    @Autowired
    private GameTemplates templates;

    @Value("eclipse/che:${che.version}")
    private String cheImage;

    @Value("${che.port.range.start}")
    private int firstChePort;

    @Value("${che.port.range.end}")
    private int lastChePort;

    @Value("${docker.socket:/var/run/docker.sock}")
    private String dockerSocket;

    @Value("localhost:${server.port}")
    private String cheHost;

    private Set<Integer> portsInUse = new ConcurrentSkipListSet<>();

    @PostConstruct
    public void setupDirector() throws DockerException, InterruptedException {
        docker.pull(cheImage);
    }

    public void direct(GameSession session, final Consumer<DirectedSession> sessionCallback) {
        final File sessionDir = sessions.sessionDirectoryFor(session.getId());

        if(isSessionCreated(sessionDir)) {
            DockerSession runningSession = loadSession(sessionDir);
            sessionCallback.accept(runningSession);
            return;
        }

        try {
            File workspaceDir = setupWorkspace(sessionDir, session.getLang());
            templates.prepareSession(session.getGameId(), sessionDir, workspaceDir, session.getLang());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to set up workspace.", e);
        }

        executorService.submit(() -> {
            try {
                final int chePort = allocateChePort();

                final ContainerConfig containerConfig = createContainerConfig(sessionDir, chePort, "dir", "up");

                final ContainerCreation creation = docker.createContainer(containerConfig);
                final String id = creation.id();

                docker.startContainer(id);

                for(Container container: docker.listContainers(DockerClient.ListContainersParam.containersCreatedSince(id))) {
                    System.out.println(container);
                }

                DockerSession dockerSession = new DockerSession(chePort, session.getId());
                storeSession(sessionDir, dockerSession);
                sessionCallback.accept(dockerSession);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Interrupted while setting up docker.");
            } catch (DockerException e) {
                throw new IllegalStateException("Unable to set up docker.", e);
            }
        });
    }

    public DirectedSession findRunningSession(String sessionId) {
        File sessionDir = sessions.sessionDirectoryFor(sessionId);
        if(sessionDir.exists()) {
            DockerSession session = loadSession(sessionDir);
            if(!session.isFinished()) {
                return session;
            } else {
                return null;
            }
        }
        throw new IllegalArgumentException("Invalid session id: " + sessionId);
    }

    private ContainerConfig createContainerConfig(File sessionDir, int chePort, String ... cmd) {
        final HostConfig hostConfig = HostConfig.builder()
                .autoRemove(true)
                .appendBinds(HostConfig.Bind.from(cheDataDir(sessionDir).getAbsolutePath()).to("/data").build())
                .appendBinds(HostConfig.Bind.from(cheWorkspaceDir(sessionDir).getAbsolutePath()).to("/chedir").build())
                .appendBinds(HostConfig.Bind.from(dockerSocket).to("/var/run/docker.sock").build())
                .build();

        return ContainerConfig.builder()
                .hostConfig(hostConfig)
                .image(cheImage)
                .env("CHE_PORT=" + chePort)
                .cmd(cmd)
                .build();
    }

    private int allocateChePort() {
        for(int port = firstChePort; port <= lastChePort; port++) {
            if(portsInUse.add(port)) {
                return port;
            }
        }
        throw new IllegalStateException("Could not allocate a port for che");
    }

    private void storeSession(File sessionDir, DockerSession dockerSession) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(new File(sessionDir, CHE_SESSION_FILENAME))) {
            gson.toJson(dockerSession, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to store game session");
        }
    }

    private boolean isSessionCreated(File sessionDir) {
        return new File(sessionDir, CHE_SESSION_FILENAME).isFile();
    }

    private DockerSession loadSession(File sessionDir) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(new File(sessionDir, CHE_SESSION_FILENAME))){
            return gson.fromJson(reader, DockerSession.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load game session", e);
        }
    }

    public void stop(GameSession gameSession) {
        final File sessionDir = sessions.sessionDirectoryFor(gameSession.getId());
        final DockerSession session = loadSession(sessionDir);

        if(session.isFinished()) {
            return;
        }

        session.finish();
        storeSession(sessionDir, session);

        executorService.submit(() -> {
            try {
                ContainerConfig config = createContainerConfig(sessionDir, session.chePort, "dir", "down");
                final ContainerCreation creation = docker.createContainer(config);
                final String id = creation.id();

                docker.startContainer(id);

                while(true) {
                    try (ServerSocket s = new ServerSocket(session.chePort)){
                        break;
                    } catch (IOException e) {
                        Thread.sleep(1000);
                    }
                }
                portsInUse.remove(session.chePort);
                LOG.info("Container released from port: {}", session.chePort);
            } catch (DockerException e) {
                throw new IllegalStateException("Unable to stop docker.", e);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Interrupted while waiting for docker to stop.");
            }
        });
    }

    private File setupWorkspace(File sessionDir, Language lang) throws IOException {
        File workspaceDir = cheWorkspaceDir(sessionDir);

        File cheData = cheDataDir(sessionDir);
        if (!cheData.isDirectory() && !cheData.mkdirs()) {
            throw new IllegalStateException("Unable to create Che working directory");
        }
        return workspaceDir;
    }

    private File cheWorkspaceDir(File sessionDir) {
        return new File(sessionDir, "che-workspace");
    }

    private File cheDataDir(File sessionDir) {
        return new File(sessionDir, "che-data");
    }


    private static final class DockerSession implements DirectedSession {
        private final int chePort;
        private final String sessionId;
        private Date finished;

        private DockerSession(int chePort, String sessionId) {
            this.chePort = chePort;
            this.sessionId = sessionId;
        }

        void finish() {
            this.finished = new Date();
        }

        @Override
        public boolean isFinished() {
            return this.finished != null;
        }

        @Override
        public URL backendUrl() {
            try {
                return new URL("http://" + InetAddress.getLocalHost().getHostAddress()+ ":" + chePort);
            } catch (UnknownHostException | MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public URL workspaceUrl() {
            try {
                return new URL("http://localhost:8080/dashboard/#/ide/che/solution");
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public URL apiUrl() {
            try {
                return new URL("http://localhost:8080/api");
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}

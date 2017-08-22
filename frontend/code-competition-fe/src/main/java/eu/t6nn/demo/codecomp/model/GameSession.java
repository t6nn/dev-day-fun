package eu.t6nn.demo.codecomp.model;

import java.util.UUID;

public class GameSession {

    private final String id;

    private Player player;

    public GameSession() {
        id = UUID.randomUUID().toString();
    }

    public GameSession(String id, Player player) {
        this.id = id;
        this.player = player;
    }

    public GameSession(Player player) {
        this();
        this.player = player;
    }

    public String getId() {
        return id;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}

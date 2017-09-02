package eu.t6nn.demo.codecomp.model;

import java.util.UUID;

public class GameSession {

    private String id;

    private Player player;

    private String gameId;

    public GameSession() {
        this(null, null);
    }

    private GameSession(String id, Player player, String gameId) {
        this.id = id;
        this.player = player;
        this.gameId = gameId;
    }

    public GameSession(Player player, String gameId) {
        this(UUID.randomUUID().toString(), player, gameId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}

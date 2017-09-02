package eu.t6nn.demo.codecomp.model;

import javax.validation.constraints.NotNull;

public class PlayerRegistration {

    @NotNull
    private Player player = new Player();

    @NotNull
    private String gameId;

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

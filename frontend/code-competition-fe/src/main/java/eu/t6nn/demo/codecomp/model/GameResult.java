package eu.t6nn.demo.codecomp.model;

public class GameResult {

    private final GameSession gameSession;
    private final Player player;
    private final long score;

    public GameResult(GameSession gameSession, Player player, long score) {
        this.gameSession = gameSession;
        this.player = player;
        this.score = score;
    }

    public Player getPlayer() {
        return player;
    }

    public long getScore() {
        return score;
    }

    public GameSession getGameSession() {
        return gameSession;
    }
}

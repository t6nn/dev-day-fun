package eu.t6nn.demo.codecomp.model;

public class GameListItem {

    private final String id;
    private final GameDef gameDef;

    public GameListItem(String id, GameDef gameDef) {
        this.id = id;
        this.gameDef = gameDef;
    }

    public String getId() {
        return id;
    }

    public GameDef getGameDef() {
        return gameDef;
    }
}

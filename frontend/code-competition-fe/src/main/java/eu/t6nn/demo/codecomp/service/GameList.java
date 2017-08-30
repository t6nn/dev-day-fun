package eu.t6nn.demo.codecomp.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.t6nn.demo.codecomp.model.GameDef;
import eu.t6nn.demo.codecomp.model.GameListItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GameList {

    @Value("${game.templates.dir}")
    private File gameTemplates;

    private Map<String, GameDef> defs;

    @PostConstruct
    private void readGameList() throws IOException {
        Gson gson = new Gson();
        try (FileReader rdr = new FileReader(new File(gameTemplates, "games.json"))) {
            defs = gson.fromJson(rdr,
                    new TypeToken<Map<String, GameDef>>() {}.getType());
        }
    }

    public List<GameListItem> allGames() {
        return defs.entrySet().stream().map(e -> new GameListItem(e.getKey(), e.getValue())).collect(Collectors.toList());
    }


}

package eu.t6nn.demo.codecomp.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import eu.t6nn.demo.codecomp.model.GameDef;
import eu.t6nn.demo.codecomp.model.GameListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String DESCRIPTION_FILENAME = "description.md";

    private static final Logger LOG = LoggerFactory.getLogger(GameList.class);

    @Value("${game.templates.dir}")
    private File gameTemplates;

    private Map<String, GameDef> defs;

    @Autowired
    private Parser markdownParser;

    @Autowired
    private HtmlRenderer markdownRenderer;

    @PostConstruct
    private void readGameList() throws IOException {
        Gson gson = new Gson();
        try (FileReader rdr = new FileReader(gameDir(gameTemplates, "games.json"))) {
            defs = gson.fromJson(rdr,
                    new TypeToken<Map<String, GameDef>>() {}.getType());
        }
    }

    public List<GameListItem> allGames() {
        return defs.entrySet().stream().map(e -> new GameListItem(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public GameDef byId(String gameId) {
        GameDef def = defs.get(gameId);
        if(def == null) {
            throw new IllegalArgumentException("Game ID " + gameId + " is unknown.");
        }
        return def;
    }

    public String loadGameDescriptionHtml(String gameId) {
        File gameDescription = new File(gameDir(gameTemplates, gameId), DESCRIPTION_FILENAME);
        if(gameDescription.isFile()) {
            try(FileReader rdr = new FileReader(gameDescription)) {
                Node document = markdownParser.parseReader(rdr);
                return markdownRenderer.render(document);
            } catch (IOException e) {
                LOG.warn("Unable to read game description for " + gameId, e);
            }
        }
        return "";
    }

    private File gameDir(File gameTemplates, String name) {
        return new File(gameTemplates, name);
    }

}

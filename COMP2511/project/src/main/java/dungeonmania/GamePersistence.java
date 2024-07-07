package dungeonmania;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.goals.Goal;
import dungeonmania.util.FileLoader;

public class GamePersistence {
    public static final JsonObject saveGame(Dungeon dungeon, String gameName) {
        JsonObject savedDungeon = new JsonObject();
        Player player = dungeon.getPlayer();

        // add dungeon id
        savedDungeon.addProperty("id", gameName);
        // add dungeon id
        savedDungeon.addProperty("name", dungeon.getName());
        // add config name
        savedDungeon.addProperty("config", dungeon.getConfigName());
        // convert all entities into json
        JsonArray savedEntities = new JsonArray();
        for (Entity entity: dungeon.getEntities()) {
            savedEntities.add(entity.toJson());
        }
        savedEntities.add(player.toJson());
        savedDungeon.add("entities", savedEntities);

        // convert goal of game into json
        Goal goal = dungeon.getGoal();
        if (goal != null) {
            savedDungeon.add("goal_condition", goal.toJson());
        }
        return savedDungeon;
    }

}

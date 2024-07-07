package dungeonmania;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.movingentities.Observer;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.entities.movingentities.enemies.Enemy;
import dungeonmania.goals.AndGoal;
import dungeonmania.goals.BoulderGoal;
import dungeonmania.goals.CompositeGoal;
import dungeonmania.goals.EnemyGoal;
import dungeonmania.goals.ExitGoal;
import dungeonmania.goals.Goal;
import dungeonmania.goals.OrGoal;
import dungeonmania.goals.TreasureGoal;
import dungeonmania.util.FileLoader;

public class DungeonLoader {
    private Config config;
    private Player player;
    private List<Entity> entities = new ArrayList<>();

    /*************
     *  Getters  *
     *************/

    public Config getConfig() {
        return config;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    /*************
     *  Loaders  *
     *************/

    public Goal loadDungeon(String name) {
        String dungeonFile = "";
        try {
            dungeonFile = FileLoader.loadResourceFile("dungeons/" + name + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject dungeon = JsonParser.parseString(dungeonFile).getAsJsonObject();
        JsonObject goalCondition = dungeon.get("goal-condition").getAsJsonObject();
        JsonArray entitiesArray = dungeon.get("entities").getAsJsonArray();

        EntityFactory entityFactory = new EntityFactory();
        for (JsonElement element : entitiesArray) {
            JsonObject entity = element.getAsJsonObject();
            Entity newEntity = entityFactory.createEntity(entity, config.getConfigMap());
            if (entity.get("type").getAsString().equals("player")) {
                player = (Player) newEntity;
            } else {
                entities.add(newEntity);
            }
        }

        entities.stream().filter(e -> e instanceof Enemy).forEach(e -> {
            Observer o = (Observer) e;
            player.attach(o);
        });

        return loadGoal(goalCondition);
    }

    public Goal loadGoal(JsonObject goalObject) {
        // recursive method
        // end condition if subgoals is null
        String goal = goalObject.get("goal").getAsString();
        switch (goal) {
            case "enemies":
                return new EnemyGoal(config.getConfigMap().get("enemy_goal"));
            case "treasure":
                return new TreasureGoal(config.getConfigMap().get("treasure_goal"));
            case "boulders":
                return new BoulderGoal();
            case "exit":
                return new ExitGoal();
            case "AND":
                CompositeGoal and = new AndGoal();
                JsonArray subgoalsAnd = goalObject.getAsJsonArray("subgoals");
                for (JsonElement subgoal: subgoalsAnd) {
                    and.addGoal(loadGoal(subgoal.getAsJsonObject()));
                }
                return and;
            case "OR":
                CompositeGoal or = new OrGoal();
                JsonArray subgoalsOr = goalObject.getAsJsonArray("subgoals");
                for (JsonElement subgoal: subgoalsOr) {
                    or.addGoal(loadGoal(subgoal.getAsJsonObject()));
                }
                return or;
            default:
                return null;
        }
    } 

    public Config loadConfig(String configName) {
        String configFile = "";
        try {
            configFile = FileLoader.loadResourceFile("configs/" + configName + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject configs = JsonParser.parseString(configFile).getAsJsonObject();
        Map<String, Integer> configMap = new HashMap<>();

        for (String configKey : configs.keySet()) {
            configMap.put(configKey, configs.get(configKey).getAsInt());
        }
        config = new Config(configMap);
        return config;
    }
}
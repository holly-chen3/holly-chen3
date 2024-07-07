package dungeonmania;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Entity;
import dungeonmania.entities.items.ItemEntity;
import dungeonmania.entities.items.buildables.BuildableEntity;
import dungeonmania.entities.items.collectables.Bomb;
import dungeonmania.entities.items.collectables.potions.Potion;
import dungeonmania.entities.movingentities.enemies.Mercenary;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.entities.statics.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DungeonManiaController {
    private Dungeon dungeon;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        if (!(dungeons().contains(dungeonName))) {
            throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
        } else if (!(configs().contains(configName))) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        dungeon = new Dungeon(UUID.randomUUID().toString(), dungeonName, configName);
        return getDungeonResponseModel();
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return dungeon == null ? null : dungeon.getDungeonResponse();
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        dungeon.tick();
        ItemEntity item = dungeon.getItem(itemUsedId);
        if (item == null) {
            throw new InvalidActionException(itemUsedId + " is not a valid item in inventory"); 
        }
        if (!(item instanceof Bomb || item instanceof Potion)) {
            throw new IllegalArgumentException("item used is not a bomb, or potion");
        }
        if (item instanceof Bomb) {
            dungeon.bombSurroundings();
        }
        if (item instanceof Potion) {
            dungeon.usePotion((Potion) item);
        }
        dungeon.notifyObservers();
        dungeon.spawn();
        dungeon.battle();
        return getDungeonResponseModel();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        dungeon.movePlayer(movementDirection);
        dungeon.spawn();
        dungeon.battle();
        
        return getDungeonResponseModel();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        Player player = dungeon.getPlayer();
        List<String> buildables = Arrays.asList("bow", "shield", "sceptre", "midnight_armour");
        if (!buildables.contains(buildable)) {
            throw new IllegalArgumentException("item not buildable");
        }
        if (buildable.equals("midnight_armour")) {
            if (dungeon.checkZombies()) {
                throw new InvalidActionException("midnight armour cannot be built with zombies in the dungeon");
            }
        }
        BuildableEntity item = EntityFactory.getBuildable(buildable, dungeon.getConfig().getConfigMap());
        player.build(item);
        return getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity entity = dungeon.getEntity(entityId);

        if (entity != null && entity.isInteractable()) {
            if (entity instanceof Mercenary) {
                dungeon.bribeMercenary((Mercenary) entity);
            } else if (entity instanceof ZombieToastSpawner) {
                dungeon.destroyZombieSpawner((ZombieToastSpawner) entity);
            }
        } else {
            throw new IllegalArgumentException(entityId + " is not a valid entity ID");
        }

        return getDungeonResponseModel();
    }
    
    public void deleteDungeon () {
        dungeon = null;
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        JsonObject savedGame = GamePersistence.saveGame(dungeon, name);
        Gson gson = new Gson();
        String save = gson.toJson(savedGame);
        try {
            String dir = "./src/main/resources/directory";
            File newFile = new File(dir);
            if (!newFile.exists()) {
                newFile.mkdir();
            }

            String filePath = dir + "/" + name + ".json";
            FileWriter newWriter = new FileWriter(filePath, false);
            newWriter.write(save);
            newWriter.close();
        } catch (IOException e) {
            return null;
        }
        return dungeon.getDungeonResponse();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        if (!allGames().contains(name)) {
            throw new IllegalArgumentException(name + " is not a valid dungeon name");
        } 
        return null;
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        String dir = "./src/main/resources/directory";
        try {
            return FileLoader.listFileNamesInDirectory(dir).stream().filter(s -> !s.equals("directory")).collect(Collectors.toList());
        } catch (IOException e) {
            return null;
        }
    }

}

package dungeonmania.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dungeonmania.entities.items.buildables.*;
import dungeonmania.entities.items.collectables.*;
import dungeonmania.entities.items.collectables.potions.*;
import dungeonmania.entities.movingentities.player.*;
import dungeonmania.entities.movingentities.enemies.*;
import dungeonmania.entities.statics.*;
import dungeonmania.entities.statics.door.Door;

public class EntityFactory {
    public Entity createEntity(JsonObject entity, Map<String, Integer> configMap) {
        int entityX = entity.get("x").getAsInt();
        int entityY = entity.get("y").getAsInt();
        JsonElement key = entity.get("key");
        int entityKey = -1;
        if (key != null) {
            entityKey = key.getAsInt();
        }
        switch(entity.get("type").getAsString()) {
            case "player":
                return new Player(entityX, entityY, configMap.get("player_health"), configMap.get("player_attack"));
            case "wall":
                return new Wall(entityX, entityY);
            case "exit":
                return new Exit(entityX, entityY);
            case "boulder":
                return new Boulder(entityX, entityY);
            case "switch":
                return new FloorSwitch(entityX, entityY);
            case "door":
                return new Door(entityX, entityY, entityKey);
            case "portal":
                return new Portal(entityX, entityY, entity.get("colour").getAsString());
            case "zombie_toast_spawner":
                return new ZombieToastSpawner(entityX, entityY);
            case "spider":
                return new Spider(entityX, entityY, configMap.get("spider_health"), configMap.get("spider_attack"));
            case "zombie_toast":
                return new ZombieToast(entityX, entityY, configMap.get("zombie_health"), configMap.get("zombie_attack"));
            case "mercenary":
                return new Mercenary(entityX, entityY, 
                    configMap.get("mercenary_health"), 
                    configMap.get("mercenary_attack"), 
                    configMap.get("bribe_amount"));
            case "treasure":
                return new Treasure(entityX, entityY);
            case "key":
                return new Key(entityX, entityY, entityKey);
            case "invincibility_potion":
                return new InvincibilityPotion(entityX, entityY);
            case "invisibility_potion":
                return new InvisibilityPotion(entityX, entityY);
            case "wood":
                return new Wood(entityX, entityY);
            case "arrow":
                return new Arrows(entityX, entityY);
            case "bomb":
                return new Bomb(entityX, entityY);
            case "sword":
                return new Sword(entityX, entityY, configMap.get("sword_attack"), configMap.get("sword_durability"));
            case "hydra": 
                return new Hydra(entityX, entityY, configMap.get("hydra_health"), configMap.get("hydra_attack"));
            case "sun_stone":
                return new SunStone(entityX, entityY);
            case "swamp_tile":
                return new SwampTile(entityX, entityY, entity.get("movement_factor").getAsInt());
            case "assassin":
                return new Assassin(entityX, entityY, 
                    configMap.get("assassin_health"), 
                    configMap.get("assassin_attack"), 
                    configMap.get("assassin_bribe_amount"), 
                    configMap.get("assassin_bribe_fail_rate"),
                    configMap.get("assassin_recon_radius"));
        }
        return null;
    }

    /**
     * 
     * @param entityX
     * @param entityY
     * @param configMap
     * @return the new zombie toast
     */
    public Entity createZombie(int entityX, int entityY, Map<String, Integer> configMap) {
        return new ZombieToast(entityX, entityY, configMap.get("zombie_health"), configMap.get("zombie_attack"));
    }

    public Entity createSpider(int entityX, int entityY, Map<String, Integer> configMap) {
        return new Spider(entityX, entityY, configMap.get("spider_health"), configMap.get("spider_attack"));
    }

    public Entity createBow(int entityX, int entityY, Map<String, Integer> configMap) {
        return new Bow(configMap.get("bow_durability"));
    }

    public Entity createShield(int entityX, int entityY, Map<String, Integer> configMap) {
        return new Shield(configMap.get("shield_defence"), configMap.get("shield_durability"));
    }

    public Entity createMidnightArmour(int entityX, int entityY, Map<String, Integer> configMap) {
        if (!configMap.containsKey("midnight_armour_defence")) return null;
        return new MidnightArmour(configMap.get("midnight_armour_defence"), configMap.get("midnight_armour_attack"));
    }

    private static Map<String, BuildableEntity> buildablesMap(Map<String, Integer> configMap) {
        Map<String, BuildableEntity> map = new HashMap<>();
        map.put("bow", new Bow(configMap.get("bow_durability")));
        map.put("shield", new Shield(configMap.get("shield_defence"), configMap.get("shield_durability")));
        if (configMap.containsKey("midnight_armour_defence")) {
            map.put("midnight_armour", new MidnightArmour(configMap.get("midnight_armour_defence"), configMap.get("midnight_armour_attack")));
        }
        return map;
    }

    public static List<BuildableEntity> getAllBuildables(Map<String, Integer> configMap) {
        return new ArrayList<>(buildablesMap(configMap).values());
    }

    public static BuildableEntity getBuildable(String buildable, Map<String, Integer> configMap) {
        BuildableEntity item = buildablesMap(configMap).get(buildable);
        return item.create();
    }
}

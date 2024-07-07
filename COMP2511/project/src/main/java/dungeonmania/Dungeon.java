package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.battle.Battle;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.items.ItemEntity;
import dungeonmania.entities.items.collectables.*;
import dungeonmania.entities.movingentities.*;
import dungeonmania.entities.movingentities.enemies.Assassin;
import dungeonmania.entities.movingentities.enemies.Enemy;
import dungeonmania.entities.movingentities.enemies.Hydra;
import dungeonmania.entities.movingentities.enemies.Mercenary;
import dungeonmania.entities.movingentities.enemies.ZombieToast;
import dungeonmania.entities.movingentities.movingbehaviours.MovingFollows;
import dungeonmania.entities.movingentities.player.*;
import dungeonmania.entities.items.collectables.potions.Potion;
import dungeonmania.entities.items.collectables.potions.Queue;
import dungeonmania.entities.statics.FloorSwitch;
import dungeonmania.entities.statics.Portal;
import dungeonmania.entities.statics.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goal;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Dungeon {
    private String id;
    private String name;
    private String configName;
    private List<Entity> droppedBombs = new ArrayList<>();
    private List<Entity> entities;
    private Config config;
    private Goal goal;
    private Player player;
    private int tickCount;
    private List<Battle> battles;
    private Queue queue;

    public Dungeon(String id, String dungeonName, String configName) {
        this.id = id;
        this.name = dungeonName;
        this.configName = configName;
        tickCount = 0;
        DungeonLoader loader = new DungeonLoader();
        config = loader.loadConfig(configName);
        goal = loader.loadDungeon(dungeonName);
        entities = loader.getEntities();
        player = loader.getPlayer();
        battles = new ArrayList<Battle>();
        queue = new Queue();
    }

    /*************
     *  Getters  *
     *************/

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConfigName() {
        return configName;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Treasure> getTreasures() {
        return player.getTreasures();
    }

    public Config getConfig() {
        return config;
    }
    
    public Goal getGoal() {
        return goal;
    }

    public List<Entity> getDroppedBombs() {
        return droppedBombs;
    }

    public List<Entity> getDefeatedEnemies() {
        return player.getDefeatedEnemies();
    }

    public Position getPlayerPosition() {
        return player.getPosition();
    }

    /**
     * Gets all entities at a certain position
     * @param position we are looking at
     * @return list of entities at the certain position
     */
    public List<Entity> getEntity(Position position) {
        return entities.stream().filter(e -> e.getPosition().equals(position))
            .collect(Collectors.toList());
    }

    /**
     * Gets all entities of a certain type
     * @param type we want to retrieve
     * @return list of entities of a certain type
     */
    public List<Entity> getEntities(String type) {
        return entities.stream().filter(e -> e.getType().equals(type))
            .collect(Collectors.toList());
    }

    /**
     * Gets all entities of a certain type at a certain position
     * @param position we are looking at
     * @param type we want to retrieve
     * @return list of entities of a certain type at a certain position
     */
    public List<Entity> getEntity(Position position, String type) {
        return getEntity(position).stream().filter(e -> e.getType()
            .equals(type)).collect(Collectors.toList());
    }

    /**
     * Checks if a entity of a certain type is at a certain position
     * @param position we are looking at
     * @param type we want to retrieve
     * @return true or false depending on whether the entity of a certain type is
     * at the certain position
     */
    public boolean containsEntity(Position position, String type) {
        return !getEntity(position, type).equals(new ArrayList<Entity>());
    }

    /**
     * Gets entity of a certain entityId
     * @param entityId of the entity we want to retrive
     * @return entity with the entityId
     */    
    public Entity getEntity(String entityId) {
        return entities.stream().filter(e -> e.getId().equals(entityId)).findFirst().orElse(null);
    }

    public List<Enemy> getEnemies(Position position) {
        return getEntity(position).stream().filter(e -> e instanceof Enemy)
            .map(e -> (Enemy) e).collect(Collectors.toList());
    } 

    public ItemEntity getItem(String itemId) {
        return player.getInventory()
            .stream()   
            .filter(e -> e.getId().equals(itemId))
            .findFirst()
            .orElse(null);
    }

    private List<String> getBuildables() {
        Player player = getPlayer();
        return EntityFactory
            .getAllBuildables(config.getConfigMap())
            .stream()
            .filter(item -> item instanceof ItemEntity && player.isBuildable(item))
            .map(item -> ((ItemEntity) item).getType())
            .collect(Collectors.toList());
    }
     
    public boolean checkZombies() {
        for (Entity entity : entities) {
            if (entity instanceof ZombieToast) {
                return true;
            }
        }
        return false;
    }

    /*************
     *  Setters  *
     *************/

    public final boolean removeEntity(Entity entity) {
        return entities.remove(entity);
    }

    public void addEntities(Entity entity) {
        entities.add(entity);
    }

    /***************
     *  Responses  *
     ***************/

    public DungeonResponse getDungeonResponse() {
        return new DungeonResponse(id, name, getEntityResponses(), 
            player != null ? player.getInventoryResponse() : null, getBattleResponses(), 
            getBuildables(), getGoalString());
    }

    private List<EntityResponse> getEntityResponses() {
        List<EntityResponse> entityResponses = new ArrayList<>();
        if (!dungeonDeath()) {
            entityResponses.add(player.getEntityResponse());
        }

        for (Entity entity: entities) {
            entityResponses.add(entity.getEntityResponse());
        }
        return entityResponses;
    }

    public List<BattleResponse> getBattleResponses() {
        List<BattleResponse> battleResponses = new ArrayList<>();
        for (Battle battle: battles) {
            battleResponses.add(battle.getBattleResponse());
        }
        return battleResponses;
    }

    public String getGoalString() {
        String string = goal.goalString(this);
        if (string.startsWith("(")) {
            string = string.substring(1, string.length() - 1);
        }
        return string;
    }

    /************
     *  Moving  *
     ************/

    public void movePlayer(Direction direction) {
        player.move(this, direction);
        notifyObservers();
    }

    public void notifyObservers() {
        player.notifyObservers(this);
        player.updateState();
    }

    /*************
     *  Ticking  *
     *************/

    public void tick() {
        tickCount++;
    }
    
    public void spawn() {
        spawnSpider();
        spawnZombie();
    }

    /**
    * Spawns spiders according to position and spawn rate
    * @param tickCount
    */
    public void spawnSpider() {
        EntityFactory entityFactory = new EntityFactory();
       
        int spiderSpawnRate = config.getSpiderSpawnRate();

        if (spiderSpawnRate != 0 && tickCount != 0 && 
            tickCount % spiderSpawnRate == 0) {
            Entity newSpider = null;
            while (newSpider == null) {
                Random rand = new Random();
                int entityX = rand.nextInt(100);
                int entityY = rand.nextInt(100);
                // Spawn spider where there is no boulder
                if (!containsEntity(new Position(entityX, entityY), "boulder")) {
                    // Spawn spiders at the specified xth tick 
                    newSpider = entityFactory.createSpider(entityX, entityY, config.getConfigMap());
                    player.attach((Observer) newSpider);
                    entities.add(newSpider);
                }
            }
        }
    }

    /**
     * Spawns zombies according to position and spawn rate
     * @param tickCount
     */
    public void spawnZombie() {
        EntityFactory entityFactory = new EntityFactory();
        
        int zombieSpawnRate = config.getZombieSpawnRate();

        List<Entity> zombieSpawnerEntities = getEntities("zombie_toast_spawner");
        if (zombieSpawnRate != 0) {
            // Go through all zombie spawners and get the position of each
            for (Entity entity: zombieSpawnerEntities) {
                Position newZTSPos = ((ZombieToastSpawner) entity).spawnZombiePosition(entities);
                if (newZTSPos == null) {
                    break;
                }
                // Spawn zombies at the specified xth tick 
                if (tickCount != 0 && tickCount % zombieSpawnRate == 0) {
                    Entity newZombie = entityFactory.createZombie(newZTSPos.getX(), newZTSPos.getY(), config.getConfigMap());
                    entities.add(newZombie);
                    player.attach((Observer) newZombie);
                }
            }
        }
    }

    public void usePotion(Potion potion) {
        if (queue.getPotions().isEmpty()) {
            potion.consumePotion(player, queue, config, this);
        } 
        queue.addPotion(potion, player);
        player.deleteItemFromInventory(potion);
    }

    /******************
     *  Interactions  *
     *****************/

    /**
     * Removes the zombie toast spawner from the entity list if player is in range
     * and has the appropriate weapon in their inventory
     * @param spawner being destroyed
     * @return true if player satisfies both these criterias and destroys the spawner
     * @return false otherwise (e.g. player doesn't have weapon in inventory but is next
     * to spawner, player isn't cardinally adjacent but does have weapon or player meets none
     * of these criterias)
     */
    public void destroyZombieSpawner(ZombieToastSpawner spawner) throws InvalidActionException{
        if (player.inventoryHasWeapon() && Position.isAdjacent(
            player.getPosition(), spawner.getPosition())) {
            entities.remove(spawner);
            player.addDefeatedEnemies(spawner);
        } else {
            throw new InvalidActionException("Player does not fulfil criteria needed to break spawner");
        }
    }

    /**
     * Bribes a mercenary and turns them into an ally if player is in range
     * and has the appropriate amount of treasure in their inventory
     * @param spawner being destroyed
     * @return true if player satisfies both these criterias and bribes
     * @return false otherwise (e.g. player doesn't have enough treasure in inventory but is next
     * to mercenary, player isn't cardinally adjacent but does have enough treasure or player meets none
     * of these criterias)
     */
    public void bribeMercenary(Mercenary mercenary) throws InvalidActionException{
        int BRIBE_RADIUS = config.getConfigMap().get("bribe_radius");
        if (player.amountInventoryHas("treasure") >= mercenary.getBribeAmount() && Position.isInRadius(
            player.getPosition(), mercenary.getPosition(), BRIBE_RADIUS)) {
            for (int i = 0; i < mercenary.getBribeAmount(); i++) {
                ItemEntity treasure = player.getItemFromInventory("treasure");
                player.deleteItemFromInventory(treasure);
            }

            // If successful bribe give passive buff to player's attack and defence due to ally
            if (mercenary.bribe()) {
                player.increaseAttack(config.getConfigMap().get("ally_attack"));
                player.increaseDefence(config.getConfigMap().get("ally_defence"));
            }

            
        } else {
            throw new InvalidActionException("Player does not fulfil criteria needed to bribe mercenary");
        }
    }

    /**********
     *  Bomb  *
     **********/

    public void bombSurroundings() {
        Bomb bomb = (Bomb) player.getItemFromInventory("bomb");
        int BOMB_RADIUS = config.getConfigMap().get("bomb_radius");
        bomb.setPosition(player.getPosition());

        entities.add(bomb);
        player.deleteItemFromInventory(bomb);
        droppedBombs.add(bomb);
        List<Entity> cardinallyAdjacentEntities = getCardinallyAdjacentEntities(bomb.getPosition());
        if (
            cardinallyAdjacentEntities
                .stream()
                .anyMatch(e -> e instanceof FloorSwitch && ((FloorSwitch) e).isActivated(cardinallyAdjacentEntities))
        ) {
            List<Entity> adjacentEntities = getEntitiesInRadius(bomb.getPosition(), BOMB_RADIUS);
            for (Entity entity : adjacentEntities) {
                if (!(entity instanceof Portal || entity instanceof Player)) {
                    removeEntity(entity);                
                }
            }
            entities.remove(bomb);  
        } 
    }

    public List<Entity> getEntitiesInRadius(Position position, int radius) {
        return entities
            .stream()
            .filter(e -> Position.isInRadius(e.getPosition(), position, radius))
            .collect(Collectors.toList());
    }

    public List<Entity> getCardinallyAdjacentEntities(Position position) {
        return entities
            .stream()
            .filter(e -> Position.isAdjacent(e.getPosition(), position))
            .collect(Collectors.toList());
    }
        
    /************
     *  Battle  *
     ************/

    public void battle() {
        List<Enemy> enemies = getEnemies(player.getPosition());
        for (Enemy enemy: enemies) {
            Battle battle = new Battle(enemy, player);
            addBattles(battle);
            player.battle(battle);

            if (enemy.getHealth() <= 0) {
                entities.remove(enemy);
                player.detach((Observer) enemy);
                player.addDefeatedEnemies(enemy);
            } else if (player.playerDeath()) {
                break;
            }
        }
    }

    public void addBattles(Battle battle) {
        battles.add(battle);
    }

    public void runAway() {
        List<Entity> mercenaries = getEntities("mercenary");
        List<Entity> zombieToasts = getEntities("zombie_toast");
        List<Entity> hydras = getEntities("hydra");
        mercenaries.stream().forEach(e -> ((Mercenary) e).runAway());
        zombieToasts.stream().forEach(e -> ((ZombieToast) e).runAway());
        hydras.stream().forEach(e -> ((Hydra) e).runAway());
    }

    public void invisibleMercenary() {
        List<Entity> entities = getEntities("mercenary");
        List<Mercenary> mercenaries = entities.stream().map(e -> (Mercenary) e).collect(Collectors.toList());

        mercenaries.stream().forEach(e -> e.invisibleMercenary());

        mercenaries.stream()
            .filter(e -> e instanceof Assassin && Position.isInRadius(e.getPosition(), player.getPosition(), ((Assassin) e).getReconRadius()))
            .forEach(e -> e.changeNormalMovement());
    }
    
    public void visibleMercenary() {
        List<Entity> mercenaries = getEntities("mercenary");
        mercenaries.stream().forEach(e -> ((Mercenary) e).changeNormalMovement());
    }

    public void vincibleEnemy() {
        List<Entity> mercenaries = getEntities("mercenary");
        List<Entity> zombieToasts = getEntities("zombie_toast");
        List<Entity> hydras = getEntities("hydra");
        mercenaries.stream().forEach(e -> ((Mercenary) e).changeNormalMovement());
        zombieToasts.stream().forEach(e -> ((ZombieToast) e).changeNormalMovement());
        hydras.stream().forEach(e -> ((Hydra) e).changeNormalMovement());
    }

    /***********
     *  Death  *
     ***********/

    public boolean dungeonDeath() {
        if (player.playerDeath()) {
            return true;
        }
        return false;
    }
}



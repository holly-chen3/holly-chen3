package dungeonmania.entities.movingentities.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.battle.Battle;
import dungeonmania.entities.Entity;
import dungeonmania.entities.items.ItemEntity;
import dungeonmania.entities.items.buildables.*;
import dungeonmania.entities.items.collectables.*;
import dungeonmania.entities.movingentities.MovingEntity;
import dungeonmania.entities.movingentities.Observer;
import dungeonmania.entities.movingentities.Subject;
import dungeonmania.entities.movingentities.enemies.Enemy;
import dungeonmania.entities.movingentities.movingbehaviours.MovingFollows;
import dungeonmania.entities.statics.*;
import dungeonmania.entities.statics.door.ClosedDoorState;
import dungeonmania.entities.statics.door.Door;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends MovingEntity implements Subject{
    private Inventory inventory;
    private List<FloorSwitch> activatedSwitches;
    private List<Observer> observers;
    private List<Entity> entities;
    private List<Entity> defeatedEnemies;
    private List<String> blockable;
    private PlayerState state;
    private Position lastPosition;
    private int defence;

    /**
     * Constructor for Player
     * @param entityX
     * @param entityY
     * @param health
     * @param attack
     */
    public Player(int entityX, int entityY, double health, int attack) {
        super(entityX, entityY, health, attack);
        inventory = new Inventory();
        activatedSwitches = new ArrayList<FloorSwitch>();
        observers = new ArrayList<Observer>();
        blockable = Arrays.asList("wall", "zombie_toast_spawner");
        entities = new ArrayList<Entity>();
        defeatedEnemies = new ArrayList<Entity>();
        state = new NormalPlayerState();
        lastPosition = position;
        defence = 0;
    }

    /*************
     *  Getters  *
     *************/

    public PlayerState getState() {
        return state;
    }

    @Override
    public String getType() {
        return "player";
    }

    public List<Treasure> getTreasures() {
        return inventory.getTreasuresPickedUp();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getDefeatedEnemies() {
        return defeatedEnemies;
    }
    
    public List<ItemEntity> getWeapons(String type) {
        return inventory.getWeapons(type);
    }

    public List<ItemEntity> getWeaponryUsed() {
        return inventory.getWeaponryUsed();
    }

    public List<ItemEntity> getInventory() {
        return inventory.getInventory();
    }

    public List<FloorSwitch> getActivatedSwitches() {
        return activatedSwitches;
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    /*************
     *  Setters  *
     *************/

    public void addActivatedSwitch(FloorSwitch floorSwitch) {
        activatedSwitches.add(floorSwitch);
    }

    public void addDefeatedEnemies(Entity enemy) {
        defeatedEnemies.add(enemy);
    }

    public void changeState(PlayerState state) {
        this.state = state;
    }

    public void removeActivatedSwitch(FloorSwitch floorSwitch) {
        activatedSwitches.remove(floorSwitch);
    }

    public void increaseAttack(int amount) {
        attack += amount;
    }

    public void increaseDefence(int amount) {
        defence += amount;
    }

    /***************
     *  Inventory  *
     ***************/

    public List<ItemResponse> getInventoryResponse() {
        return inventory.getInventoryResponse();
    }

    public ItemEntity getItemFromInventory(String type) {
        return inventory.getItemFromInventory(type);
    }

    public void deleteItemFromInventory(ItemEntity item) {
        inventory.deleteItemFromInventory((ItemEntity) item);
    }

    public void deleteWeapon(ItemEntity weapon) {
        inventory.deleteWeaponFromInventory((ItemEntity) weapon);
    }

    public boolean inventoryHasWeapon() {
        return inventory.inventoryHasWeapon();
    }

    public boolean inventoryHas(String type) {
        return inventory.inventoryHas(type);
    }

    public int amountInventoryHas(String type) {
        return inventory.getItemAmount(type);
    }

    /************
     *  Battle  *
     ************/

    public int getTotalAttack(Enemy enemy) {
        int playerAttack = attack;

        for (ItemEntity sword: getWeapons("sword")) {
            playerAttack += ((Sword) sword).getAttackDamage();
            deleteWeapon(sword);
        }
        for (ItemEntity bow: getWeapons("bow")) {
            playerAttack *= 2;
            ((Bow) bow).decreaseDurability();
            deleteWeapon(bow);
        }
        for (ItemEntity midnightArmour: getWeapons("midnight_armour")) {
            playerAttack += ((MidnightArmour) midnightArmour).getAttack();
        }
        return playerAttack;
    }

    public int defenceAgainstEnemy(Enemy enemy) {
        int enemyAttack = enemy.getAttack() - defence;
        for (ItemEntity shield: getWeapons("shield")) {
            enemyAttack -= ((Shield) shield).getDefence();
            deleteWeapon(shield);
        }
        for (ItemEntity midnightArmour: getWeapons("midnight_armour")) {
            enemyAttack -= ((MidnightArmour) midnightArmour).getDefence();
        }
        return enemyAttack;
    }

    public void battle(Battle battle) {
        state.battle(battle);
    }

    /************
     *  Moving  *
     ************/

    public boolean isMovable(Dungeon dungeon, Direction direction) {
        Position newPosition = position.translateBy(direction);
        List<Entity> entities = dungeon.getEntities();
        List<Entity> atPosition = dungeon.getEntity(newPosition);

        if (!atPosition.stream().anyMatch(e -> blockable.contains(e.getType()))) {
            if (dungeon.containsEntity(newPosition, "boulder")) {
                Boulder boulder = (Boulder) dungeon.getEntity(newPosition, 
                    "boulder").stream().findFirst().get();

                boulder.moveBoulder(direction, entities, this);
                if (boulder.getPosition().equals(newPosition)) {
                    return false;
                }
            } else if (dungeon.containsEntity(newPosition, "portal")) {
                Portal teleportingFrom = (Portal) dungeon.getEntity(newPosition, 
                    "portal").stream().findFirst().get();

                teleport(dungeon, teleportingFrom, direction);
                return false;
            } else if (dungeon.containsEntity(newPosition, "door")) {
                Door door = (Door) dungeon.getEntity(newPosition, "door").stream()
                    .findFirst().get();
                door.openDoor(this);
                if (door.getState() instanceof ClosedDoorState) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public boolean isTeleportable(Dungeon dungeon, Direction direction, Position destinationPortal) {
        Position newPosition = destinationPortal.translateBy(direction);
        List<Entity> entities = dungeon.getEntities();
        List<Entity> atPosition = dungeon.getEntity(newPosition);

        if (!atPosition.stream().anyMatch(e -> blockable.contains(e.getType()))) {
            if (dungeon.containsEntity(newPosition, "boulder")) {
                Boulder boulder = (Boulder) dungeon.getEntity(newPosition, 
                    "boulder").stream().findFirst().get();

                boulder.moveBoulder(direction, entities, this);
                if (boulder.getPosition().equals(newPosition)) {
                    return false;
                }
            } else if (dungeon.containsEntity(newPosition, "door")) {
                Door door = (Door) dungeon.getEntity(newPosition, "door").stream()
                    .findFirst().get();
                door.openDoor(this);
                if (door.getState() instanceof ClosedDoorState) {
                    return false;
                }
            }

            return true;
        }

        return false; 

    }

    public List<Entity> entitiesInPosition(List<Entity> entities, Position newPosition) {
        return entities.stream().filter(e -> e.getPosition().equals(newPosition)).collect(Collectors.toList());
    }

    public void move(Dungeon dungeon, Direction direction) {
        this.entities = dungeon.getEntities();
        lastPosition = position;

        if (isMovable(dungeon, direction)) {
            position = position.translateBy(direction); 
        } 
        
        inventory.entityPickup(dungeon, position);
        dungeon.tick();
    }

    public void teleport(Dungeon dungeon, Portal portalStart, Direction movementDirection) {
        portalStart.teleport(dungeon, this, movementDirection);
    }

    public void updateState() {
        state.updateState();
    }
    
    /***************
     *  Observers  *
     ***************/
    @Override
    public void attach(Observer o) {
        observers.add(o);
        
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
        
    }

    @Override
    public void notifyObservers(Dungeon dungeon) {
        observers.forEach(o -> {o.update(this, dungeon);});
        
    }


    /****************
     *  Buildables  *
     ****************/
    
    public void build(BuildableEntity buildable) throws InvalidActionException {
        if (buildable.isBuildable(inventory)) {
            buildable.build(inventory);
        } else {
            throw new InvalidActionException(
                "Insufficient materials!"
            );
        }
    }

    public boolean isBuildable(BuildableEntity item) {
        return item.isBuildable(inventory);
    }

    /******************
     *  Player Death  *
     ******************/

    public boolean playerDeath () {
        if (health <= 0) {
            return true;
        }
        return false;
    }
    
}


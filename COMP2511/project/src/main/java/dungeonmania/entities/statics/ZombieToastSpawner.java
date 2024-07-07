package dungeonmania.entities.statics;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity{
    /**
     * Constructor for ZombieToastSpawner
     * @param entityX
     * @param entityY
     */
    public ZombieToastSpawner(int entityX, int entityY) {
        super(entityX, entityY);
        interactable = true;
    }

    @Override
    public String getType() {
        return "zombie_toast_spawner";
    }
    
    /**
     * Checks if the ZombieToast can spawn in a cardinally adjacent square
     * @param entities
     * @return true if there are no static entities (except floorswitch) in that square 
     * and false otherwise
     */
    public boolean isMovableTo(List<Entity> entities) {
        for (Entity entity: entities) {
            if (entity instanceof StaticEntity && !(entity instanceof FloorSwitch)) {
                return false;
            }
        }
        return true;
    }

    /**
     * List of all entities in a specific position in the dungeon
     * @param entities
     * @param newPosition
     * @return a list of entities in specified position
     */
    public List<Entity> entitiesInPosition(List<Entity> entities, Position newPosition) {
        return entities.stream().filter(e -> e.getPosition().equals(newPosition)).collect(Collectors.toList());
    }

    /**
     * Goes through cardinally adjacent squares in a clockwise direction to see if they are blocked
     * @param entities
     * @return position of square if zombie is able to spawn there
     */
    public Position spawnZombiePosition(List<Entity> entities) {
        // Collect a list of entities in the cardinally adjacent squares to the zombie spawner
        List<Entity> upEntities = entitiesInPosition(entities, getPosition().translateBy(Direction.UP));
        List<Entity> rightEntities = entitiesInPosition(entities, getPosition().translateBy(Direction.RIGHT));
        List<Entity> downEntities = entitiesInPosition(entities, getPosition().translateBy(Direction.DOWN));
        List<Entity> leftEntities = entitiesInPosition(entities, getPosition().translateBy(Direction.LEFT));
  
        if (isMovableTo(upEntities)) {
            return getPosition().translateBy(Direction.UP);
        } else if (isMovableTo(rightEntities)) {
            return getPosition().translateBy(Direction.RIGHT);
        } else if (isMovableTo(downEntities)) {
            return getPosition().translateBy(Direction.DOWN);
        } else if (isMovableTo(leftEntities)) {
            return getPosition().translateBy(Direction.LEFT);
        }
        return null;
    }
}

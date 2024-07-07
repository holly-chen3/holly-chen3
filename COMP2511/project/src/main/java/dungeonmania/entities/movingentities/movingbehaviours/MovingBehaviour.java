package dungeonmania.entities.movingentities.movingbehaviours;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.entities.statics.SwampTile;
import dungeonmania.entities.statics.door.ClosedDoorState;
import dungeonmania.entities.statics.door.Door;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingBehaviour {
    
    protected Entity entity;
    protected List<Entity> entities;
    protected List<Direction> directions;
    protected Player player;

    public List<Entity> atPosition(Position position) {
        return entities.stream().filter(e -> e.getPosition()
            .equals(position)).collect(Collectors.toList());
    }
    
    public abstract Direction movingDirection(Player player, Entity entity, List<Entity> entities);

    public String isMovable(Direction direction) {
        Position newPosition = entity.getPosition().translateBy(direction);
        return isMovable(newPosition);
    }
    
    public String isMovable(Position position) {
        Entity blocker = atPosition(position).stream()
            .filter(e -> entity.getBlockable().contains(e.getType())).findFirst()
            .orElse(null);
        
        if (blocker != null && blocker instanceof Door) {
            Door door = (Door) blocker;
            if (!(door.getState() instanceof ClosedDoorState)) {
                return null;
            }
        }

        return blocker == null ? null : blocker.getType();
    }

    public boolean hasClosedDoor(List<Entity> entities) {
        List<Door> doors = entities.stream().filter(e -> e.getType()
            .equals("door")).map(e -> (Door) e)
            .collect(Collectors.toList());
        if (doors.stream().anyMatch(d -> d.getState() instanceof 
            ClosedDoorState)) {
            return true;
        } else {
            return false;
        }
    }

    public int movementFactor(List<Entity> entities, Position position) {
        this.entities = entities;
        SwampTile swampTile = (SwampTile) atPosition(position).stream().filter(e -> 
            e.getType().equals("swamp_tile")).findFirst().orElse(null);

        return swampTile == null ? 0 : swampTile.getMovementFactor();
    }
}

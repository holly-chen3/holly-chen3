package dungeonmania.entities.statics;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.movingentities.player.*;
import dungeonmania.entities.statics.door.Door;
import dungeonmania.entities.statics.door.OpenedDoorState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity{

    public Boulder(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "boulder";
    }

    public void moveBoulder(Direction movementDirection, List<Entity> entities, Player player) {
        // if it is next to a wall or wall like object, do not move
        // and move the player back to its original position
        // else move the boulder to the next position
        Position newPosition = getPosition().translateBy(movementDirection);
        List<Entity> oldEntities = entitiesInPosition(entities, position);
        List<Entity> newEntities = entitiesInPosition(entities, newPosition);
        if (!isMovableTo(newEntities, player)) {
            return;
        }
        if (oldEntities.stream().anyMatch(e -> e instanceof FloorSwitch)) {
            // if there is a floor switch in the boulder's old position, delete floor switch from list
            player.removeActivatedSwitch((FloorSwitch)oldEntities.stream().filter(e -> e instanceof FloorSwitch).findFirst().get());
        }
        position = newPosition;
    }

    public List<Entity> entitiesInPosition(List<Entity> entities, Position newPosition) {
        return entities.stream().filter(e -> e.getPosition().equals(newPosition)).collect(Collectors.toList());
    }

    public boolean isMovableTo(List<Entity> entities, Player player) {
        // in this list of entities in the specific position, 
        // if there consists of another static entity 
        // other than floor switch, then the boulder cannot move to that position.
        for (Entity entity: entities) {
            if (entity instanceof FloorSwitch) {
                player.addActivatedSwitch((FloorSwitch)entity);
            } else if (entity instanceof StaticEntity) {
                if (entity instanceof Door && ((Door)entity).getState() instanceof OpenedDoorState) {
                    break;
                }
                return false;
            }
        }
        return true;
    }
}

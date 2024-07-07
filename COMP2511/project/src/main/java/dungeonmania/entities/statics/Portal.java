package dungeonmania.entities.statics;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingentities.MovingEntity;
import dungeonmania.entities.movingentities.enemies.Enemy;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends StaticEntity{
    private String colour;
    private int nextDirIndex;

    public Portal(int entityX, int entityY, String colour) {
        super(entityX, entityY);
        this.colour = colour;
        nextDirIndex = -1;   

    }

    @Override
    public JsonObject toJson() {
        JsonObject portal = super.toJson();
        portal.addProperty("colour", colour);
        return portal;
    }

    public String getColour() {
        return colour;
    }

    @Override
    public String getType() {
        return "portal";
    }

    public Portal getMatchingPortal(List<Entity>entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals("portal")) {
                Portal other = (Portal) entity;
                if (other.getColour().equals(this.colour) && other != this) {
                    return other;
                }
            }
        }
        return null; 	
    }

    public List<Entity> entitiesInPosition(List<Entity> entities, Position newPosition) {
        return entities.stream().filter(e -> e.getPosition().equals(newPosition)).collect(Collectors.toList());
    }

    public void teleport(Dungeon dungeon, MovingEntity teleporter, Direction movementDirection) {
        Direction destination = movementDirection;
        List<Entity> entities = dungeon.getEntities();
        Portal destinationPortal = getMatchingPortal(entities);
        destination = clockwiseTeleport(dungeon, teleporter, movementDirection, destinationPortal.getPosition());
        if (destination == null) {
            return;
        }
        teleporter.setPosition(destinationPortal.getPosition().translateBy(destination.getOffset()));
        chainTeleport(dungeon, teleporter, destination);
    }
 
    public Direction clockwiseTeleport(Dungeon dungeon, MovingEntity teleporter, Direction movementDirection, Position destinationPortal) {
        List<Direction> directions = Arrays.asList(Direction.UP, Direction.RIGHT, 
        Direction.DOWN, Direction.LEFT);

        for (int i = 0; i < 4; i++) {
            if (directions.get(i).equals(movementDirection)) {
                nextDirIndex = i;
                break;
            }
        }

        for (int i = 0; i < 4; i++) {
            Direction clockwiseDirection = directions.get(nextDirIndex);
            if (teleporter instanceof Player) {
                if (((Player)teleporter).isTeleportable(dungeon, clockwiseDirection, destinationPortal)) {
                    return clockwiseDirection;
                }
            } else if (teleporter instanceof Enemy) {
                if (((Enemy)teleporter).isMovable(clockwiseDirection) == null) {
                    return clockwiseDirection;
                }
            }
            nextDirIndex = (nextDirIndex + 1) % 4;
        }  
        return null;
    } 

    public void chainTeleport(Dungeon dungeon, MovingEntity teleporter, Direction movementDirection) {
        List<Entity> entities = dungeon.getEntities();
        List<Entity>teleporterPosEntities = entitiesInPosition(entities, teleporter.getPosition());
        for (Entity entity : teleporterPosEntities) {
            if (entity instanceof Portal && entity != this) {
                ((Portal)entity).teleport(dungeon, teleporter, movementDirection);
            }
        }
    } 
}

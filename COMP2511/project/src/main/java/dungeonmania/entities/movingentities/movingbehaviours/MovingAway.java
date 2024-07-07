package dungeonmania.entities.movingentities.movingbehaviours;

import java.util.Arrays;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.movingentities.player.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingAway extends MovingBehaviour{
    private Position playerPosition;
    private Position entityPosition;

    public MovingAway() {
        directions = Arrays.asList(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
    }

    @Override
    public Direction movingDirection(Player player, Entity entity, List<Entity> entities) {
        this.entity = entity;
        this.entities = entities;
        this.entityPosition = entity.getPosition();
        this.playerPosition = player.getPosition();
        int playerX = playerPosition.getX();
        int playerY = playerPosition.getY();
        int entityX = entityPosition.getX();
        int entityY = entityPosition.getY();
        // if the player is on the right, then you move
        if (playerX >= entityX && isMovable(Direction.LEFT) == null) {
            return Direction.LEFT;
        } else if (playerX < entityX && isMovable(Direction.RIGHT) == null) {
            return Direction.RIGHT;
        } else if (playerY > entityY && isMovable(Direction.UP) == null) {
            return Direction.UP;
        } else if (playerY < entityY && isMovable(Direction.DOWN) == null) {
            return Direction.DOWN;
        }
        return null;
    }
}

package dungeonmania.entities.movingentities.movingbehaviours;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.util.Direction;

public class MovingCircularly extends MovingBehaviour {
    private int nextDirIndex;

    public MovingCircularly() {
        directions = Arrays.asList(Direction.RIGHT, 
        Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.LEFT,
        Direction.UP, Direction.UP, Direction.RIGHT);
        nextDirIndex = -1;   
    }

    @Override
    public Direction movingDirection(Player player, Entity entity, List<Entity> entities) {
        this.entity = entity;
        this.entities = entities;
        this.player = player;

        if (nextDirIndex == -1) {
            if (isMovable(Direction.UP) == null) {
                nextDirIndex++;
                return Direction.UP;
            }
            return null;
        }

        for (int i = 0; i < 2; i++) {
            Direction direction = directions.get(nextDirIndex);

            if (isMovable(direction) == null) {
                nextDirIndex = (nextDirIndex + 1) % 8;
                return direction;
            } else if (isMovable(direction).equals("boulder")) {
                Collections.reverse(directions);
                // modding doesnt account for negative numbers in java
                nextDirIndex = (((directions.size() - nextDirIndex + 4) % 8) + 8) % 8;
            }
        }

        return null;
    }
}

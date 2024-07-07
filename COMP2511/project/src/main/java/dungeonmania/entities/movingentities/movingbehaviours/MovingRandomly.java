package dungeonmania.entities.movingentities.movingbehaviours;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import dungeonmania.entities.Entity;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.util.Direction;

public class MovingRandomly extends MovingBehaviour {

    public MovingRandomly() {
        directions = Arrays.asList(Direction.UP, Direction.RIGHT, 
            Direction.DOWN, Direction.LEFT);
    }

    @Override
    public Direction movingDirection(Player player, Entity entity, List<Entity> entities) {
        this.entity = entity;
        this.entities = entities;



        Random rand = new Random();
        int initialDirection = rand.nextInt(4);
        for (int i = initialDirection; i < initialDirection + 4; i++) {
            if (isMovable(directions.get(i % 4)) == null) {
                return directions.get(i % 4);
            }
        }

        return null;
    }
}

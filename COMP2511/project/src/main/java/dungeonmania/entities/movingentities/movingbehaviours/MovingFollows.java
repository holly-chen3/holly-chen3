package dungeonmania.entities.movingentities.movingbehaviours;

import java.util.Arrays;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.util.Direction;

public class MovingFollows extends MovingBehaviour{

    public MovingFollows() {
        directions = Arrays.asList(Direction.UP, Direction.RIGHT, 
            Direction.DOWN, Direction.LEFT);
    }

    @Override
    public Direction movingDirection(Player player, Entity entity, List<Entity> entities) {
        if (!player.getPosition().equals(player.getLastPosition())) {
            entity.setPosition(player.getLastPosition());
        }

        return null;
    }
}

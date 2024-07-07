package dungeonmania.entities.movingentities.enemies;

import java.util.Arrays;

import dungeonmania.Dungeon;
import dungeonmania.entities.movingentities.movingbehaviours.MovingCircularly;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;

public class Spider extends Enemy{

    public Spider(int entityX, int entityY, double health, int attack) {
        super(entityX, entityY, health, attack);
        movingBehaviour = new MovingCircularly();
        blockable = Arrays.asList("boulder");
    }

    @Override
    public String getType() {
        return "spider";
    }

    @Override
    public void runAway() {
        // spider does not run away
    }

    @Override
    public void changeNormalMovement() {
        // spider does not change movement
        
    }

    @Override
    public boolean teleport(Dungeon dungeon, Portal portalStart, Direction direction) {
        // Do nothing
        return false;
    }
}

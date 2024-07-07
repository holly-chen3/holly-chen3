package dungeonmania.entities.movingentities.enemies;

import java.util.Arrays;

import dungeonmania.Dungeon;
import dungeonmania.entities.movingentities.movingbehaviours.MovingRandomly;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;

public class ZombieToast extends Enemy{
    private MovingRandomly movingRandomly = new MovingRandomly();

    /**
     * Constructor for Zombie Toast
     * @param entityX
     * @param entityY
     * @param health
     * @param attack
     */
    public ZombieToast(int entityX, int entityY, double health, int attack) {
        super(entityX, entityY, health, attack);
        blockable = Arrays.asList("wall", "door", "boulder", "portal", "zombie_toast_spawner");
        movingBehaviour = movingRandomly;
    }
    
    @Override
    public String getType() {
        return "zombie_toast";
    }

    @Override
    public void changeNormalMovement() {
        movingBehaviour = movingRandomly;
    }

    @Override
    public boolean teleport(Dungeon dungeon, Portal portalStart, Direction direction) {
        // Do nothing
        return false;
    }
}

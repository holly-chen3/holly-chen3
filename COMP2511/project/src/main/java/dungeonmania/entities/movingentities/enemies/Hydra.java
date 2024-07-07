package dungeonmania.entities.movingentities.enemies;

import java.util.Arrays;
import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.entities.movingentities.movingbehaviours.MovingRandomly;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;

public class Hydra extends Enemy{

    private MovingRandomly movingRandomly = new MovingRandomly();
    private boolean noRespawn;

    public Hydra(int entityX, int entityY, double health, int attack) {
        super(entityX, entityY, health, attack);
        blockable = Arrays.asList("wall", "boulder", "portal", "zombie_toast_spawner");
        movingBehaviour = movingRandomly;
        noRespawn = false;
    }

    @Override
    public String getType() {
        return "hydra";
    }

    public void setNoRespawn(boolean noRespawn) {
        this.noRespawn = noRespawn;
    }
 
    public void reduceHealth(int damage) {
        Random chance = new Random();
        if (!noRespawn && chance.nextBoolean()) {
            super.reduceHealth(-damage);
        } else {
            noRespawn = false;
            super.reduceHealth(damage);
        }
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

package dungeonmania.entities.movingentities.enemies;

import java.util.Arrays;

import dungeonmania.Dungeon;
import dungeonmania.entities.movingentities.movingbehaviours.MovingFollows;
import dungeonmania.entities.movingentities.movingbehaviours.MovingRandomly;
import dungeonmania.entities.movingentities.movingbehaviours.MovingTowards;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;

public class Mercenary extends Enemy{
    protected int bribeAmount;

    public Mercenary(int entityX, int entityY, double health, int attack, 
        int bribeAmount) {
        super(entityX, entityY, health, attack);
        movingBehaviour = new MovingTowards();
        blockable = Arrays.asList("boulder", "zombie_toast_spawner", "wall", "door");
        interactable = true;
        this.bribeAmount = bribeAmount;
    }

    @Override
    public String getType() {
        return "mercenary";
    }
    
    public int getBribeAmount() {
        return bribeAmount;
    }

    public void invisibleMercenary() {
        movingBehaviour = new MovingRandomly();
    }
    
    @Override
    public void changeNormalMovement() {
        movingBehaviour = new MovingTowards();
    }

    public boolean bribe() {
        hostile = false;
        interactable = false;
        movingBehaviour = new MovingFollows();
        return true;
    }

    @Override
    public boolean teleport(Dungeon dungeon, Portal portalStart, Direction direction) {
        if (portalStart != null) {
            portalStart.teleport(dungeon, this, direction);
            return true;
        }

        return false;
        
    }
}

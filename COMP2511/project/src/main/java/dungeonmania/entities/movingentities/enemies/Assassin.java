package dungeonmania.entities.movingentities.enemies;

import java.util.Random;

import dungeonmania.entities.movingentities.movingbehaviours.MovingFollows;
import dungeonmania.entities.movingentities.movingbehaviours.MovingRandomly;
import dungeonmania.entities.movingentities.movingbehaviours.MovingTowards;
import dungeonmania.util.Position;

public class Assassin extends Mercenary{
    private double bribeFailRate;
    private int reconRadius;

    public Assassin(int entityX, int entityY, double health, int attack, 
        int bribeAmount, double bribeFailRate, int reconRadius) {
        super(entityX, entityY, health, attack, bribeAmount);
        this.bribeFailRate = bribeFailRate;
        this.reconRadius = reconRadius;    
    }

    public int getReconRadius() {
        return reconRadius;
    }

    @Override 
    public String getType() {
        return "assassin";
    }
    
    @Override
    public void invisibleMercenary() {
        movingBehaviour = new MovingRandomly();
    }
    
    @Override
    public void changeNormalMovement() {
        movingBehaviour = new MovingTowards();
    }

    @Override
    public boolean bribe() {
        Random rand = new Random();
        if (rand.nextDouble() <= bribeFailRate) {
            return false;
        } else {
            hostile = false;
            interactable = false;
            movingBehaviour = new MovingFollows();
            return true;
        }
    }
}

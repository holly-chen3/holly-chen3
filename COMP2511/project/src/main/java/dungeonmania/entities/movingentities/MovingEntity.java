package dungeonmania.entities.movingentities;

import dungeonmania.entities.Entity;

public abstract class MovingEntity extends Entity{
    protected double health;
    protected int attack;

    public MovingEntity(int entityX, int entityY, double health, int attack) {
        super(entityX, entityY);
        this.health = health;
        this.attack = attack;
    }

    public MovingEntity(int entityX, int entityY) {
        super(entityX, entityY);
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public double getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    } 

    public void reduceHealth(int damage) {
        this.setHealth(this.getHealth() - damage);
    }
}

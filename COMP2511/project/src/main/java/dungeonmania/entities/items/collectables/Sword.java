package dungeonmania.entities.items.collectables;

import dungeonmania.entities.items.Weapon;

public class Sword extends CollectableEntity implements Weapon{
    private int swordAttackDamage;
    private int swordDurability;

    public Sword(int entityX, int entityY, int swordAttackDamage, int swordDurability) {
        super(entityX, entityY);
        this.swordAttackDamage = swordAttackDamage;
        this.swordDurability = swordDurability;
    }

    @Override
    public String getType() {
        return "sword";
    }

    public int getAttackDamage() {
        swordDurability--;
        return swordAttackDamage;
    }
    
    @Override
    public int getDurability() {
        return swordDurability;
    }
}

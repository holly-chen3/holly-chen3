package dungeonmania.entities.items.buildables;

import dungeonmania.entities.items.ItemEntity;
import dungeonmania.entities.items.Weapon;
import dungeonmania.entities.movingentities.player.Inventory;

public class MidnightArmour extends ItemEntity implements Weapon, BuildableEntity{
    
    private int midnightArmourDefence;
    private int midnightArmourAttack;
    private int SWORD_REQUIRED = 1;
    private int SUN_STONE_REQUIRED = 1;

    public MidnightArmour(int midnightArmourDefence, int midnightArmourAttack) {
        super();
        this.midnightArmourDefence = midnightArmourDefence;
        this.midnightArmourAttack = midnightArmourAttack;
    }

    @Override
    public String getType() {
        return "midnight_armour";
    }

    public int getDefence() {
        return midnightArmourDefence;
    }

    public int getAttack() {
        return midnightArmourAttack;
    }

    @Override
    public int getDurability() {
        return -1;
    }

    @Override
    public boolean isBuildable(Inventory inventory) {
        return inventory.hasMaterialQuantity("sword", SWORD_REQUIRED) &&
            inventory.hasMaterialQuantity("sun_stone", SUN_STONE_REQUIRED);        
    }

    @Override
    public void build(Inventory inventory) {
        inventory.removeMaterialQuantity("sword", SWORD_REQUIRED);
        inventory.removeMaterialQuantity("sun_stone", SUN_STONE_REQUIRED);
        inventory.addToInventory(new MidnightArmour(this.getDefence(), this.getAttack())); 
    }

    @Override
    public BuildableEntity create() {
        return new MidnightArmour(this.getDefence(), this.getAttack());
    }
}

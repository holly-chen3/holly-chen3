package dungeonmania.entities.items.buildables;

import dungeonmania.entities.items.ItemEntity;
import dungeonmania.entities.items.Weapon;
import dungeonmania.entities.movingentities.player.Inventory;

public class Shield extends ItemEntity implements Weapon, BuildableEntity{
    private int shieldDefence;
    private int shieldDurability;
    private int WOOD_REQUIRED = 2;
    private int TREASURE_REQUIRED = 1;
    private int KEY_REQUIRED = 1;

    public Shield(int shieldDefence, int shieldDurability) {
        super();
        this.shieldDefence = shieldDefence;
        this.shieldDurability = shieldDurability;
    }

    @Override
    public String getType() {
        return "shield";
    }

    public int getDefence() {
        decreaseDurability();
        return shieldDefence;
    }

    @Override
    public int getDurability() {
        return shieldDurability;
    }

    public void decreaseDurability() {
        shieldDurability--;
    }

    @Override
    public boolean isBuildable(Inventory inventory) {
        return inventory.hasMaterialQuantity("wood", WOOD_REQUIRED) &&
            inventory.hasMaterialQuantity("key", KEY_REQUIRED) ||
            inventory.hasMaterialQuantity("treasure", TREASURE_REQUIRED) ||
            inventory.hasMaterialQuantity("sun_stone", TREASURE_REQUIRED);        
    }

    @Override
    public void build(Inventory inventory) {
        // Always uses wood first, checks sunstone next and then key and then treasure
        inventory.removeMaterialQuantity("wood", WOOD_REQUIRED);
        if (inventory.hasMaterialQuantity("sun_stone", TREASURE_REQUIRED)) {
        } else if (inventory.hasMaterialQuantity("key", KEY_REQUIRED)) {
            inventory.removeMaterialQuantity("key", KEY_REQUIRED);
        } else if (inventory.hasMaterialQuantity("treasure", TREASURE_REQUIRED)) {
            inventory.removeMaterialQuantity("treasure", TREASURE_REQUIRED);
        } 
        inventory.addToInventory(new Shield(this.getDurability(), this.getDefence()));        
    }

    @Override
    public BuildableEntity create() {
        return new Shield(this.getDurability(), this.getDefence());
    }
}

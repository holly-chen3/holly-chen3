package dungeonmania.entities.items.buildables;

import dungeonmania.entities.items.ItemEntity;
import dungeonmania.entities.items.Weapon;
import dungeonmania.entities.movingentities.player.Inventory;

public class Bow extends ItemEntity implements Weapon, BuildableEntity {
    
    private int bowDurability;
    private int WOOD_REQUIRED = 1;
    private int ARROWS_REQUIRED = 3;


    public Bow(int bowDurability) {
        super();
        this.bowDurability = bowDurability;
    }

    @Override
    public String getType() {
        return "bow";
    }
    
    @Override
    public int getDurability() {
        return bowDurability;
    }

    public void decreaseDurability() {
        bowDurability--;
    }

    public boolean isBuildable(Inventory inventory) {
        return inventory.hasMaterialQuantity("wood", WOOD_REQUIRED) &&
            inventory.hasMaterialQuantity("arrow", ARROWS_REQUIRED);

    }

    public void build(Inventory inventory) {
        inventory.removeMaterialQuantity("wood", WOOD_REQUIRED);
        inventory.removeMaterialQuantity("arrow", ARROWS_REQUIRED);
        inventory.addToInventory(new Bow(this.getDurability()));
    }

    public BuildableEntity create() {
        return new Bow(this.getDurability());
    }
    


}

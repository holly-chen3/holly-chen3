package dungeonmania.entities.items.buildables;

import dungeonmania.entities.movingentities.player.Inventory;

public interface BuildableEntity {

    public boolean isBuildable(Inventory inventory);

    public void build(Inventory inventory);

    public BuildableEntity create();
}

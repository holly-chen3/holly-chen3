package dungeonmania.entities.items.collectables.potions;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.entities.items.collectables.CollectableEntity;
import dungeonmania.entities.movingentities.player.*;

public abstract class Potion extends CollectableEntity{

    public Potion(int entityX, int entityY) {
        super(entityX, entityY);
    }
    
    public abstract void consumePotion(Player player, Queue queue, Config config, Dungeon dungeon);
}

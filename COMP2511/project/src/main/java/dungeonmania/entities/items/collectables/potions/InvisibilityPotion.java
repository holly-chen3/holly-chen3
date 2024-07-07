package dungeonmania.entities.items.collectables.potions;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.entities.movingentities.player.*;

public class InvisibilityPotion extends Potion{

    public InvisibilityPotion(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "invisibility_potion";
    }

    @Override
    public void consumePotion(Player player, Queue queue, Config config, Dungeon dungeon) {
        player.changeState(new InvisiblePlayerState(dungeon, queue));
    }
}

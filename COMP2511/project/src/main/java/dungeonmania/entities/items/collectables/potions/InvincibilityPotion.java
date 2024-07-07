package dungeonmania.entities.items.collectables.potions;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.entities.movingentities.player.*;

public class InvincibilityPotion extends Potion{

    public InvincibilityPotion(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "invincibility_potion";
    }

    @Override
    public void consumePotion(Player player, Queue queue, Config config, Dungeon dungeon) {
        player.changeState(new InvinciblePlayerState(dungeon, queue));
    }
}

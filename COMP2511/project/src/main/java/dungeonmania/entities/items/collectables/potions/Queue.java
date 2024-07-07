package dungeonmania.entities.items.collectables.potions;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.movingentities.player.*;

public class Queue {

    List<Potion> potions;

    public Queue() {
        this.potions = new ArrayList<>();
    }

    public List<Potion> getPotions() {
        return potions;
    }

    public void addPotion(Potion potion, Player player) {
        player.deleteItemFromInventory(potion);
        potions.add(potion);
    }

    public void removeFromQueue() {
        potions.remove(0);
    }

    public Potion getNextPotion() {
        return potions.get(0);
    }
}

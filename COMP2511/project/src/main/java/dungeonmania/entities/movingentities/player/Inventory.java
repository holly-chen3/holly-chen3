package dungeonmania.entities.movingentities.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.items.ItemEntity;
import dungeonmania.entities.items.Weapon;
import dungeonmania.entities.items.collectables.CollectableEntity;
import dungeonmania.entities.items.collectables.SunStone;
import dungeonmania.entities.items.collectables.Treasure;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Inventory {
    private List<ItemEntity> inventoryEntities;
    private List<Treasure> treasuresPickedUp;

    public Inventory() {
        inventoryEntities = new ArrayList<ItemEntity>();
        treasuresPickedUp = new ArrayList<Treasure>();
    }

    /*************
     *  Getters  *
     *************/

    public List<ItemEntity> getInventory() {
        return inventoryEntities;
    }

    public List<ItemResponse> getInventoryResponse() {
        List<ItemResponse> inventoryResponse = new ArrayList<>();
        for (ItemEntity item: inventoryEntities) {
            inventoryResponse.add(item.getItemResponse());
        }
        return inventoryResponse;
    }

    public List<ItemEntity> getWeaponryUsed() {
        return inventoryEntities.stream().filter(e -> e instanceof Weapon).collect(Collectors.toList());
    }

    public List<Treasure> getTreasuresPickedUp() {
        return treasuresPickedUp;
    }

    public List<ItemEntity> getWeapons(String type) {
        return inventoryEntities.stream().filter(e -> e.getType().equals(type)).collect(Collectors.toList());
    }

    public ItemEntity getItemFromInventory(String type) {
        return inventoryEntities.stream().filter(e -> e.getType().equals(type)).findFirst().get();
    }

    public int getItemAmount(String type) {
        return (int) inventoryEntities.stream().filter(e -> e.getType().equals(type)).count(); 
    }

    /*************
     *  Boolean  *
     *************/

    public boolean inventoryHas(String type) {
        return !(getItemAmount(type) == 0);
    }

    public boolean inventoryHasWeapon() {
        for (ItemEntity item: inventoryEntities) {
            if (item instanceof Weapon) {
                return true;
            }
        }
        return false;
    }

    /*************
     *  Setters  *
     *************/

    public void addToInventory(Entity entity) {
        // when player picks up a collectable item, put in inventory
        inventoryEntities.add((ItemEntity) entity);
        if (entity instanceof Treasure) {
            treasuresPickedUp.add((Treasure) entity);
        }
    }

    public void addTreasuresPickedUp(Treasure treasure) {
        treasuresPickedUp.add(treasure);
    }

    public void deleteItemFromInventory(ItemEntity item) {
        inventoryEntities.remove(item);
    }


    /************
     *  Battle  *
     ************/


    public void deleteWeaponFromInventory(ItemEntity weapon) {
        if (((Weapon) weapon).getDurability() <= 0) {
            deleteItemFromInventory(weapon);
        }
    }

    /******************
     *  Collectables  *
     ******************/

    public void entityPickup(Dungeon dungeon, Position position) {
        // loop through the entity list
        // if the position for any of them are the same as player
        // call pickup method
        List<Entity> collectableEntities = dungeon.getEntity(position).stream()
            .filter(e -> e instanceof CollectableEntity)
            .collect(Collectors.toList());
        for (Entity entity: collectableEntities) {
            if ((entity.getType().equals("key") && inventoryHas("key"))
                || dungeon.getDroppedBombs().contains(entity)) {
                continue;
            }
            addToInventory(entity);
            dungeon.getEntities().remove(entity);
        }
    }

    /****************
     *  Buildables  *
     ****************/

    public boolean hasMaterialQuantity(String material, int requiredQuantity) {
        int quantity = 0;

        for (ItemEntity item : inventoryEntities) {
            if (item.getType().equals(material)) {
                quantity++;
            } 
        }
        return quantity >= requiredQuantity;
    }

    public void removeMaterialQuantity(String material, int quantity) {
        if (this.hasMaterialQuantity(material, quantity)) {
            List<Entity> removeList = new ArrayList<>();
            inventoryEntities
                .stream()
                .filter(item -> item.getType().equals(material))
                .limit(quantity)
                .forEach(item -> removeList.add(item));
            removeList.stream().forEach(i -> inventoryEntities.remove(i));
        }
    }
}

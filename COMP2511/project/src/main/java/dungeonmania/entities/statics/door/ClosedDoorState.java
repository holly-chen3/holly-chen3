package dungeonmania.entities.statics.door;


import dungeonmania.entities.items.collectables.Key;
import dungeonmania.entities.movingentities.player.*;

public class ClosedDoorState extends DoorState{

    public ClosedDoorState(Door door) {
        super(door);
    }

    @Override
    public void openDoor(Player player) {
        if (player.inventoryHas("sun_stone")) {
            door.changeState(new OpenedDoorState(door));
        } 
        // when door is closed, open door if there is a key and key is correct
        else if (player.inventoryHas("key")) { 
            Key key = (Key) player.getItemFromInventory("key");
            if (door.getKey() == key.getKey()) {
                door.changeState(new OpenedDoorState(door));
                player.deleteItemFromInventory(key);
            }

        }
    }
    
}
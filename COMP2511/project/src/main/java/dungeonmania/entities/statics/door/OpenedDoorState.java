package dungeonmania.entities.statics.door;

import dungeonmania.entities.movingentities.player.*;

public class OpenedDoorState extends DoorState{

    public OpenedDoorState(Door door) {
        super(door);
    }

    @Override
    public void openDoor(Player player) {
        // when door is already opened, do nothing
    }
    
}

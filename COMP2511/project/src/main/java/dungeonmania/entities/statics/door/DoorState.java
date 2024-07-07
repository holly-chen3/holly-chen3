package dungeonmania.entities.statics.door;

import dungeonmania.entities.movingentities.player.*;

public abstract class DoorState {
    protected Door door;

    public DoorState(Door door) {
        this.door = door;
    }

    public abstract void openDoor(Player player);
}

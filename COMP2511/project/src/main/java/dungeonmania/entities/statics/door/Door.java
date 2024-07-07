package dungeonmania.entities.statics.door;

import com.google.gson.JsonObject;

import dungeonmania.entities.movingentities.player.*;
import dungeonmania.entities.statics.StaticEntity;

public class Door extends StaticEntity {
    private DoorState state;
    private int key;
    
    public Door(int entityX, int entityY, int key) {
        super(entityX, entityY);
        this.key = key;
        this.state = new ClosedDoorState(this);
    }
    @Override
    public JsonObject toJson() {
        JsonObject door = super.toJson();
        door.addProperty("key", key);
        return door;
    }
    public void changeState(DoorState state) {
        this.state = state;
    }
    
    public int getKey() {
        return key;
    }

    @Override
    public String getType() {
        return "door";
    }

    public void openDoor(Player player) {
        state.openDoor(player);
    }

    public DoorState getState() {
        return state;
    }
}

package dungeonmania.entities.items.collectables;

import com.google.gson.JsonObject;

public class Key extends CollectableEntity{
    private int key;

    public Key(int entityX, int entityY, int key) {
        super(entityX, entityY);
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    @Override
    public String getType() {
        return "key";
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = super.toJson();
        obj.addProperty("key", key);
        return obj;
    }
}

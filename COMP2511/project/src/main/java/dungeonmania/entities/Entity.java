package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class Entity {
    protected Position position;
    protected String id;
    protected List<String> blockable;
    protected boolean interactable;
    
    public Entity(int entityX, int entityY) {
        this.position = new Position(entityX, entityY);
        this.id = UUID.randomUUID().toString();
        blockable = new ArrayList<String>();
        interactable = false;
    }

    public Entity () {
        // for buildables
    }

    public JsonObject toJson() {
        JsonObject entity = new JsonObject();
        entity.addProperty("type", getType());
        entity.addProperty("id", id);
        entity.addProperty("x", (position == null) ? 0: position.getX());
        entity.addProperty("y", (position == null) ? 0: position.getY());
        return entity;
    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(id, getType(), position, isInteractable());
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public List<String> getBlockable() {
        return blockable;
    }

    public abstract String getType();

    public boolean isInteractable() {
        return interactable;
    }
}

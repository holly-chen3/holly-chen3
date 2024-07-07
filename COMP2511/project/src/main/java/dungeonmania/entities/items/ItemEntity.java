package dungeonmania.entities.items;

import dungeonmania.entities.Entity;
import dungeonmania.response.models.ItemResponse;

public abstract class ItemEntity extends Entity{

    public ItemEntity(int entityX, int entityY) {
        super(entityX, entityY);
    }

    public ItemEntity() {
        super();
    }

    public ItemResponse getItemResponse() {
         return new ItemResponse(getId(), getType());
    }
    
}

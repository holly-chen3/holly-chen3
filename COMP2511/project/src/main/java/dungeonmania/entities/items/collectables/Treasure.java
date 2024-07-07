package dungeonmania.entities.items.collectables;

public class Treasure extends CollectableEntity{

    public Treasure(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "treasure";
    }
}

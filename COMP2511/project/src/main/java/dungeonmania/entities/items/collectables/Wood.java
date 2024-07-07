package dungeonmania.entities.items.collectables;

public class Wood extends CollectableEntity{

    public Wood(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "wood";
    }
}

package dungeonmania.entities.items.collectables;

public class Arrows extends CollectableEntity{

    public Arrows(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "arrow";
    }
    
}

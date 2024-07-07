package dungeonmania.entities.items.collectables;

public class Bomb extends CollectableEntity{

    public Bomb(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "bomb";
    }
}
    



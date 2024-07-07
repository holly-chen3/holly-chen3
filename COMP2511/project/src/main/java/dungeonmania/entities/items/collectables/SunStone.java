package dungeonmania.entities.items.collectables;

public class SunStone extends Treasure {

    public SunStone(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "sun_stone";
    }
    
}

package dungeonmania.entities.statics;

public class Wall extends StaticEntity{

    public Wall(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "wall";
    }
}

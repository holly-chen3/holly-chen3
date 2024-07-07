package dungeonmania.entities.statics;

public class Exit extends StaticEntity{

    public Exit(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "exit";
    }
}

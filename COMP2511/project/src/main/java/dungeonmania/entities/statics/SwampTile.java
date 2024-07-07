package dungeonmania.entities.statics;

public class SwampTile extends StaticEntity{
    private int movementFactor;

    public SwampTile(int entityX, int entityY, int movementFactor) {
        super(entityX, entityY);
        this.movementFactor = movementFactor;
    }

    @Override
    public String getType() {
        return "swamp_tile";
    }

    public int getMovementFactor() {
        return movementFactor;
    }
    
}

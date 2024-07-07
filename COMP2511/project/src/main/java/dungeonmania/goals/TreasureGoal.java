package dungeonmania.goals;

import dungeonmania.Dungeon;

public class TreasureGoal extends LeafGoal{
    private int treasureGoal;

    public TreasureGoal(int treasureGoal) {
        super("treasure");
        this.treasureGoal = treasureGoal;
    }

    @Override
    public int count(Dungeon dungeon) {
        return (int) dungeon.getTreasures().size();
    }

    @Override
    public boolean isCompleted(Dungeon dungeon) {
        return count(dungeon) >= treasureGoal;
    }
    
    
}

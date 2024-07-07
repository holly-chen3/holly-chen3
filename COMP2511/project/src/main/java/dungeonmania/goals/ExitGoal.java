package dungeonmania.goals;

import dungeonmania.Dungeon;
import dungeonmania.entities.statics.Exit;

public class ExitGoal extends LeafGoal{

    public ExitGoal() {
        super("exit");
    }

    @Override
    public int count(Dungeon dungeon) {
        return (int) dungeon.getEntities()
            .stream()
            .filter(e -> 
                (e instanceof Exit) && 
                (e.getPosition().equals(dungeon.getPlayerPosition()))
            )
            .count();
    }

    @Override
    public boolean isCompleted(Dungeon dungeon) {
        return count(dungeon) == 1;
    }
    
}

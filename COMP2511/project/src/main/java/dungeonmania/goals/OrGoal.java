package dungeonmania.goals;

import dungeonmania.Dungeon;

public class OrGoal extends CompositeGoal{

    @Override
    public String getGoal() {
        return "OR";
    }
    
    @Override
    public boolean isCompleted(Dungeon dungeon) {
        return getGoals().stream().anyMatch(e -> e.isCompleted(dungeon));
    }
}

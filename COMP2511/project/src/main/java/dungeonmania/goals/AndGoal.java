package dungeonmania.goals;

import com.google.gson.JsonObject;

import dungeonmania.Dungeon;

public class AndGoal extends CompositeGoal{

    @Override
    public String getGoal() {
        return "AND";
    }
    
    @Override
    public boolean isCompleted(Dungeon dungeon) {
        return getGoals().stream().allMatch(e -> e.isCompleted(dungeon));
    }
}

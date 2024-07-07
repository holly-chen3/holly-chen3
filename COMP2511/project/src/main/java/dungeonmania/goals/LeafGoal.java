package dungeonmania.goals;

import com.google.gson.JsonObject;

import dungeonmania.Dungeon;

public abstract class LeafGoal implements Goal{
    private String goal;

    public LeafGoal(String goal) {
        this.goal = goal;
    }

    public String getGoal() {
        return goal;
    }

    @Override
    public String goalString(Dungeon dungeon) {
        return isCompleted(dungeon) ? "": ":" + goal;
    }

    @Override
    public JsonObject toJson() {
        JsonObject goal = new JsonObject();
        goal.addProperty("goal", getGoal());
        return goal;
    }

    public abstract boolean isCompleted(Dungeon dungeon);
    public abstract int count(Dungeon dungeon);
}

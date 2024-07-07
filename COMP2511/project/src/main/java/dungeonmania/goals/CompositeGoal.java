package dungeonmania.goals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dungeonmania.Dungeon;

public abstract class CompositeGoal implements Goal{
    private final List<Goal> goals = new ArrayList<>();
    
    public final List<Goal> getGoals() {
        return goals;
    }

    public final void addGoal(Goal goal) {
        goals.add(goal);
    }

    @Override
    public String goalString(Dungeon dungeon) {
        if (isCompleted(dungeon)) {
            return "";
        }
        String subgoal = " " + getGoal() + " ";
        return goals
            .stream()
            .filter(goal -> !goal.isCompleted(dungeon))
            .map(goal -> goal.goalString(dungeon))
            .collect(Collectors.joining(subgoal, "(", ")"));
    }

    @Override
    public JsonObject toJson() {
        JsonObject goal = new JsonObject();
        goal.addProperty("goal", getGoal());
        JsonArray subgoals = new JsonArray();
        goals.stream().forEach(e -> subgoals.add(e.toJson()));
        goal.add("subgoals", subgoals);
        return goal;
    }    
}

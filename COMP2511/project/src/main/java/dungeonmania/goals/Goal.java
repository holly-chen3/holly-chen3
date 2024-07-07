package dungeonmania.goals;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dungeonmania.Dungeon;

public interface Goal {
    public String getGoal();
    public String goalString(Dungeon dungeon);
    public boolean isCompleted(Dungeon dungeon);
    public JsonObject toJson();
}

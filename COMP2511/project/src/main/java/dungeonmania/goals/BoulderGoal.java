package dungeonmania.goals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.statics.Boulder;
import dungeonmania.entities.statics.FloorSwitch;
import dungeonmania.util.Position;

public class BoulderGoal extends LeafGoal{

    public BoulderGoal() {
        super("boulders");
    }

    @Override
    public int count(Dungeon dungeon) {
        List<Position> switches = new ArrayList<>();
        List<Position> boulders = new ArrayList<>();
        for (Entity e: dungeon.getEntities()) {
            Position position = e.getPosition();
            if (e instanceof Boulder) {
                boulders.add(position);
            } else if (e instanceof FloorSwitch) {
                switches.add(position);
            }
        }
        Collection<Position> different = new HashSet<>(switches);
        different.removeAll(boulders);
        return different.size();
    }

    @Override
    public boolean isCompleted(Dungeon dungeon) {
        return count(dungeon) == 0;
    }
}

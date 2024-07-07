package dungeonmania.entities.statics;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity{

    public FloorSwitch(int entityX, int entityY) {
        super(entityX, entityY);
    }

    @Override
    public String getType() {
        return "switch";
    }

    public List<Entity> entitiesInPosition(List<Entity> entities, Position newPosition) {
        return entities.stream().filter(e -> e.getPosition().equals(newPosition)).collect(Collectors.toList());
    }

    public boolean isActivated(List<Entity>entities) {
        List<Entity> entity = entitiesInPosition(entities, this.getPosition());
        return entity.stream().anyMatch(e -> (e instanceof Boulder));
    }
}

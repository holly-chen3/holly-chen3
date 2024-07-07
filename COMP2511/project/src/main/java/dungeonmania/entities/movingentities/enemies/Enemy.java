package dungeonmania.entities.movingentities.enemies;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingentities.MovingEntity;
import dungeonmania.entities.movingentities.Observer;
import dungeonmania.entities.movingentities.Subject;
import dungeonmania.entities.movingentities.movingbehaviours.MovingAway;
import dungeonmania.entities.movingentities.movingbehaviours.MovingBehaviour;
import dungeonmania.entities.movingentities.movingbehaviours.MovingFollows;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class Enemy extends MovingEntity implements Observer{
    protected MovingBehaviour movingBehaviour;
    protected boolean hostile;
    protected int ticksSpentOnPos;
    
    public Enemy(int entityX, int entityY, double health, int attack) {
        super(entityX, entityY, health, attack);
        hostile = true;
        ticksSpentOnPos = 0;
    }

    public String isMovable(Direction movementDirection) {
        return movingBehaviour.isMovable(movementDirection);
    }

    public abstract boolean teleport(Dungeon dungeon, Portal portalStart, Direction direction);

    public void move(Dungeon dungeon, Player player, List<Entity> entities) {
        Direction direction = null;
        if (ticksSpentOnPos >= movingBehaviour.movementFactor(entities, position)) {
            direction = movingBehaviour.movingDirection(player, this, 
                entities);
        }

        ticksSpentOnPos++;

        if (direction != null) {
            Position newPosition = position.translateBy(direction);
            Portal portalStart = (Portal) entities.stream().filter(e -> 
                e.getType().equals("portal") && e.getPosition()
                .equals(newPosition)).findFirst().orElse(null);

            if (!teleport(dungeon, portalStart, direction)) {
                position = position.translateBy(direction);
            }
            
            ticksSpentOnPos = 0;
        }
    }

    @Override
    public void update(Subject obj, Dungeon dungeon) {
        if (obj instanceof Player) {
            Player player = (Player) obj;
            move(dungeon, player, player.getEntities());
        }
    }

    public void runAway() {
        movingBehaviour = new MovingAway();
    }

    public abstract void changeNormalMovement();
    
    public boolean isHostile() {
        return hostile;
    }
}

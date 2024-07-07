package dungeonmania.entities.movingentities.movingbehaviours;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Queue;

import dungeonmania.entities.Entity;
import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingTowards extends MovingBehaviour {
    private int rows;
    private int cols;

    public MovingTowards() {
        directions = Arrays.asList(Direction.UP, Direction.RIGHT, 
            Direction.DOWN, Direction.LEFT);
    }
    
    @Override
    public Direction movingDirection(Player player, Entity entity, List<Entity> entities) {
        this.player = player;
        this.entity = entity;
        this.entities = entities;
        rows = getMaxCoords().getY() + 2;
        cols = getMaxCoords().getX() + 2;

        boolean[][] map = new boolean[rows][cols];

        Position nextPosition = dijkstras(map, entity.getPosition());
        Direction nextDirection = Direction.UP;
        if (nextPosition != null) {
            for (int i = 0; i < 4; i++) {
                if (Position.calculatePositionBetween(entity.getPosition(), 
                    nextPosition).equals(directions.get(i).getOffset())) {
                    nextDirection = directions.get(i);
                    return nextDirection;
                }
            }
        }

        return null;
    }

    public Position getMaxCoords () {
        int maxXCoord = 0;
        int maxYCoord = 0;

        for (Entity e : entities) {
            int x = e.getPosition().getX();
            int y = e.getPosition().getY();
            if (x > maxXCoord) {
                maxXCoord = x;
            }

            if (y > maxYCoord) {
                maxYCoord = y;
            }
        }

        return new Position(maxXCoord, maxYCoord);
    }

    public boolean isValid(Position position) {
        if (position.getX() < 0 || position.getY() < 0 || 
            position.getX() >= cols || position.getY() >= rows) {
            return false;
        }

        return true;
    }

    public Position dijkstras(boolean[][] map, Position source) {
        Map<Position, Double> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();
        Queue<Position> positions = new LinkedList<>();

        for (int i = 0; i < rows + 2; i++) {
            for (int j = 0; j < cols + 2; j++) {
                Position position = new Position(j, i);
                dist.put(position, 1000.0);
                prev.put(position, null);
                positions.add(position);
            }
        }

        dist.put(source, 0.0);
        while (!positions.isEmpty()) {
            Map<Position, Double> queuedDist = dist.entrySet().stream()
                .filter(d -> positions.contains(d.getKey()))
                .collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue));

            Position closest = Collections.min(queuedDist.entrySet(), 
                Map.Entry.comparingByValue()).getKey();
            positions.remove(closest);

            for (int i = 0; i < 4; i++) {
                Position translated = closest.translateBy(directions.get(i));

                if (isValid(translated) && isMovable(translated) == null && 
                    dist.get(closest) + 1 + movementFactor(entities, translated) < dist.get(translated)) {
                    dist.put(translated, dist.get(closest) + 1 + movementFactor(entities, translated));
                    prev.put(translated, closest);
                }
            }
        }

        if (prev.get(player.getPosition()) != null) {
            Position path = player.getPosition();
            Position temp = path;
            while (!path.equals(source)) {
                temp = path;
                path = prev.get(path);
            }

            return temp;
        }

        return null;
    }
}

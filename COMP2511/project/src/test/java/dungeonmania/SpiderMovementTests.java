package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getEntities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SpiderMovementTests {
    private static List<Position> genericMovementTrajector(Position pos, boolean continuous) {
        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();

        if (continuous) {
            movementTrajectory.add(new Position(x  , y-1));
            movementTrajectory.add(new Position(x+1, y-1));
            movementTrajectory.add(new Position(x+1, y));
            movementTrajectory.add(new Position(x+1, y+1));
            movementTrajectory.add(new Position(x  , y+1));
            movementTrajectory.add(new Position(x-1, y+1));
            movementTrajectory.add(new Position(x-1, y));
            movementTrajectory.add(new Position(x-1, y-1));
        } else {
            movementTrajectory.add(new Position(x  , y-1));
            movementTrajectory.add(new Position(x+1, y-1));
            movementTrajectory.add(new Position(x+1, y));
            movementTrajectory.add(new Position(x+1, y-1));
            movementTrajectory.add(new Position(x  , y-1));
            movementTrajectory.add(new Position(x-1, y-1));
            movementTrajectory.add(new Position(x-1, y));
            movementTrajectory.add(new Position(x-1, y-1));
        }

        return movementTrajectory;
    }

    private static void genericSpiderSequence(String dungeonName, boolean continuous) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(dungeonName, "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = genericMovementTrajector(pos, continuous);
        int nextPositionElement = 0;

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.NONE);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());
            
            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test basic movement of spiders")
    public void basicMovement() {
        genericSpiderSequence("d_spiderTest_basicMovement", true);
    }

    @Test
    @DisplayName("Test spiders can climb over walls, switches, doors, exits, portals and zombie toast spawners.")
    public void climbingMovement() {
        genericSpiderSequence("d_spiderTest_climbingMovement", true);
    }

    @Test
    @DisplayName("Test spiders reverse movement at boulders")
    public void reverseMovement() {
        genericSpiderSequence("d_spiderTest_reverseMovement", false);
    }

    @Test
    @DisplayName("Test spiders stuck between two boulders")
    public void stuckMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_stuckMovement", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.NONE);
            assertEquals(pos.translateBy(Direction.UP), 
                getEntities(res, "spider").get(0).getPosition());
        }
    }

    @Test
    @DisplayName("Test spiders stuck under boulder")
    public void stuckUnderMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_stuckUnderMovement", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.NONE);
            assertEquals(pos, getEntities(res, "spider").get(0).getPosition());
        }
    }

    @Test
    @DisplayName("Test spiders slowed down by swamp tile")
    public void testSwampTile() {
        /*
         *  [  ]  [  ]  swamp
         *  [  ] spider swamp player
         *  [  ]  [  ]  [  ]  exit
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_swampTileMovement", "c_spiderTest_basicMovement");

        List<Direction> path = Arrays.asList(Direction.UP, Direction.RIGHT, 
            Direction.NONE, Direction.NONE, Direction.DOWN, Direction.NONE, 
            Direction.NONE, Direction.NONE, Direction.NONE, Direction.DOWN, 
            Direction.LEFT, Direction.LEFT, Direction.UP, Direction.UP, 
            Direction.RIGHT);

        Position lastPosition = getEntities(res, "spider").get(0).getPosition();
        for (int i = 0; i < path.size(); i++) {
            res = dmc.tick(Direction.NONE);
            Position currentPosition = getEntities(res, "spider").get(0).getPosition();
            assertEquals(lastPosition.translateBy(path.get(i)), currentPosition);
            lastPosition = currentPosition;
        }
    }
}
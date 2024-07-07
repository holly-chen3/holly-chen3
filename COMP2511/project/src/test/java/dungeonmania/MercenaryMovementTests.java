package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.countEntityOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class MercenaryMovementTests {
    @Test
    @DisplayName("Test the mercenary moves towards player in basic scenario")
    public void testBasicMovement() {
        /*
         *  exit   wall  wall  wall
         * player  [  ]  merc  wall
         *  wall   wall  wall  wall
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryPlayerDies");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        
        DungeonResponse postRes = dmc.tick(Direction.DOWN);
        assertEquals(pos.translateBy(Direction.LEFT), 
            getEntities(postRes, "mercenary").get(0).getPosition());

        postRes = dmc.tick(Direction.DOWN);
        assertEquals(getPlayer(res).get().getPosition(), 
            getEntities(postRes, "mercenary").get(0).getPosition());
    }

    @Test
    @DisplayName("Test the mercenary blocked by boulder, wall, closed door and spawner")
    public void testBlockedMovement() {
        /*
         *  exit   wall boulder wall
         * player  wall  merc  spawner
         *  wall   wall  door  wall
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_blockedMovement", "c_battleTests_basicMercenaryPlayerDies");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        
        DungeonResponse postRes = dmc.tick(Direction.DOWN);
        assertEquals(pos, getEntities(postRes, "mercenary").get(0).getPosition());
    }

    @Test
    @DisplayName("Test the mercenary can sidestep a wall to get to player")
    public void testWallSideStep() {
        /*
         *                    spawner
         *  wall               door
         * player              wall   merc
         *  wall             boulder  exit
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_wallSideStep", "c_battleTests_basicMercenaryPlayerDies");
        
        for (int i = 0; i < 7; i++) {
            res = dmc.tick(Direction.DOWN);
        }

        assertEquals(getPlayer(res).get().getPosition()
            .translateBy(Direction.RIGHT), 
            getEntities(res, "mercenary").get(0).getPosition());
    }
    
    @Test
    @DisplayName("Test the mercenary can teleport")
    public void testTeleportMovement() {
        /*
         *  wall                         
         * player         portal         portal  merc
         *  wall                                 exit
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_teleportMovement", "c_battleTests_basicMercenaryPlayerDies");
        
        res = dmc.tick(Direction.DOWN);
        assertEquals(getPlayer(res).get().getPosition()
            .translateBy(Direction.RIGHT), 
            getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    @DisplayName("Test the mercenary movements is affected by swamp tiles")
    public void testSwampMovement() {
        /*
         *  exit   swamp                swamp
         *         swamp                        
         * player  swamp  swamp  swamp  merc
         * swamp       
         *                swamp
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_swampMovement", "c_battleTests_basicMercenaryPlayerDies");
        
        List<Direction> path = Arrays.asList(Direction.DOWN, Direction.LEFT, 
            Direction.LEFT, Direction.LEFT, Direction.LEFT, 
            Direction.NONE, Direction.NONE, Direction.UP);

        Position lastPosition = getEntities(res, "mercenary").get(0).getPosition();
        for (int i = 0; i < path.size(); i++) {
            res = dmc.tick(Direction.NONE);
            Position currentPosition = getEntities(res, "mercenary").get(0).getPosition();
            assertEquals(lastPosition.translateBy(path.get(i)), currentPosition);
            lastPosition = currentPosition;
        }

        assertEquals(0, countEntityOfType(res, "player"));
        assertEquals(1, countEntityOfType(res, "mercenary"));
    }
}

package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static dungeonmania.TestUtils.getEntities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BoulderTests {
    @Test
    @DisplayName("Test player pushes a boulder without constraints")
    public void testPlayerBoulderMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_basicBoulder", "c_movementTest_testMovementDown");
    
        // get position of boulder
        Position posOne = getEntities(res, "boulder").get(0).getPosition();

        // move player downward
        res = dmc.tick(Direction.DOWN);
        
        // assert position of boulder has changed
        Position posTwo = getEntities(res, "boulder").get(0).getPosition();
        assertNotEquals(posOne, posTwo);

        // assert player is now in the boulder's old position
        assertEquals(posOne, getEntities(res, "player").get(0).getPosition());

        // repeat
        res = dmc.tick(Direction.DOWN);
        assertNotEquals(posTwo, getEntities(res, "boulder").get(0).getPosition());
        assertEquals(posTwo, getEntities(res, "player").get(0).getPosition());
    }
    // TODO: when parameter testing is understood, change these three tests into one
    @Test
    @DisplayName("Test player cannot push two boulders")
    public void testPlayerCannotPushTwoBoulders() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_twoBoulders", "c_movementTest_testMovementDown");
    
        // get position of one boulder and player
        Position posOne = getEntities(res, "boulder").get(0).getPosition();
        Position posPlayer = getEntities(res, "player").get(0).getPosition();

        // move player downward
        res = dmc.tick(Direction.DOWN);
        
        // assert position of boulder has not changed
        Position posTwo = getEntities(res, "boulder").get(0).getPosition();
        assertEquals(posOne, posTwo);

        // assert player is still in same position
        assertEquals(posPlayer, getEntities(res, "player").get(0).getPosition());
    }
    
    @Test
    @DisplayName("Test one boulder next to wall, player cannot push")
    public void testPlayerCannotPushBoulderThroughWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_wallBlock", "c_movementTest_testMovementDown");
    
        // get position of boulder and player
        Position posOne = getEntities(res, "boulder").get(0).getPosition();
        Position posPlayer = getEntities(res, "player").get(0).getPosition();

        // move player downward
        res = dmc.tick(Direction.DOWN);
        
        // assert position of boulder has not changed
        Position posTwo = getEntities(res, "boulder").get(0).getPosition();
        assertEquals(posOne, posTwo);

        // assert player is still in same position
        assertEquals(posPlayer, getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @DisplayName("Test portal acts as a wall for boulder")
    public void testPlayerCannotPushBoulderThroughPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_portalBoulder", "c_bombTest_placeBombRadius2");
    
        // get position of boulder and player
        Position posOne = getEntities(res, "boulder").get(0).getPosition();
        Position posPlayer = getEntities(res, "player").get(0).getPosition();

        // move player downward
        res = dmc.tick(Direction.DOWN);
        
        // assert position of boulder has not changed
        Position posTwo = getEntities(res, "boulder").get(0).getPosition();
        assertEquals(posOne, posTwo);

        // assert player is still in same position
        assertEquals(posPlayer, getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @DisplayName("Test boulder can move ontop of a floor switch")
    public void testPlayerCanPushBoulderOnFloorSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");
    
        // get position of boulder and player
        Position posOne = getEntities(res, "boulder").get(0).getPosition();

        // move player right
        res = dmc.tick(Direction.RIGHT);
        
        // assert position of boulder has changed to on the switch
        Position posTwo = getEntities(res, "boulder").get(0).getPosition();
        Position switchPos = getEntities(res, "switch").get(0).getPosition();
        assertEquals(posTwo, switchPos);

        // assert player is in boulder old position
        assertEquals(posOne, getEntities(res, "player").get(0).getPosition());
    }
}

package dungeonmania;

import static dungeonmania.TestUtils.getEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PortalTests {

    @Test
    @DisplayName("Test successful teleportation from one portal")
    public void testOnePortalTeleportSuccess() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalsTest_testPlayerOnly", "c_portalsTest_testEntitiesTeleport");
        Position start = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1,5), start);

        // move player LEFT into the orange portal
        res = dmc.tick(Direction.LEFT);
        Position pos = getEntities(res, "player").get(0).getPosition();

        // player expected at (1,5) after teleporting through orange portal in the LEFT direction
        assertEquals(new Position(1,5), pos);
    }

    @Test
    @DisplayName("Test successful teleportation with multiple portals")
    public void testMultiplePortalTeleportSuccess() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalsTest_testPlayerOnly", "c_portalsTest_testEntitiesTeleport");

        // player expected at (3,1) after teleporting UP through blue portal
        // (3, 1) is a purple portal
        // chained teleportation ends at (0,4) which is one position above the orange portal
        res = dmc.tick(Direction.UP);
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(0,4), pos);
    }

    @Test
    @DisplayName("Test teleportation works both ways")
    public void testPortalTeleportBack() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalsTest_testPlayerOnly", "c_portalsTest_testEntitiesTeleport");
        Position start = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1,5), start);
        
        // player expected at (1, 5) after teleporting through orange portal on the left
        res = dmc.tick(Direction.LEFT);
        Position posLeft = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1,5), posLeft);

        // player expected at (1, 5) after teleporting through orange portal on the right
        res = dmc.tick(Direction.RIGHT);
        Position posRight = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1,5), posRight);
    }

    @Test
    @DisplayName("Test no teleporation when destination portal surrounded by walls")
    public void testDestinationPortalSurroundedByWalls() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalsTest_testPlayerOnly", "c_portalsTest_testEntitiesTeleport");

        // the green portal is surrounded by walls so there should be no change
        // in the players position after 'teleporting'        
        // move player DOWN into the green portal
        res = dmc.tick(Direction.DOWN);
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1,5), pos);
    }

    @Test
    @DisplayName("Test moves to first empty grid cardinally adjacent to destinational portal")
    public void testClockwiseLanding() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalsTest_testEntitiesTeleport", "c_portalsTest_testEntitiesTeleport");
        
        // teleporting through the red portal in the right direction ends up on a wall
        // next opened spot for teleportation in the clockwise direction is (1, 1)
        res = dmc.tick(Direction.RIGHT);
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1,1), pos);
    }
}

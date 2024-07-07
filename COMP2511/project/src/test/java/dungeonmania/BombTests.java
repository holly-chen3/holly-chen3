package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BombTests {
    @Test  
    @DisplayName("Bomb can be collected by player and removed from inventory")
    public void testBombPickup() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(1, getEntities(res, "bomb").size());
        // player walks to pick up bomb
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, getEntities(res, "player").get(0).getPosition());
        // player inventory should now have 1 bomb
        assertEquals(1, getInventory(res, "bomb").size());
        assertEquals(0, getEntities(res, "bomb").size());
    }

    @Test
    @DisplayName("Bomb turns cardinally adjacent floor switch from unactivated to activated")
    public void testActivateUnactivatedSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");
        assertEquals(1, getEntities(res, "bomb").size());

        // player walks to pick up bomb
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(1, getInventory(res, "bomb").size());
        assertEquals(1, getEntities(res, "player").size());

        res = dmc.tick(Direction.RIGHT);


        // DROP BOMB HERE
        try {
            res = dmc.tick(getInventory(res, "bomb").get(0).getId());
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        }
    
        // player inventory should now have 0 bomb
        assertEquals(0, getInventory(res, "bomb").size());
        assertEquals(2, getEntities(res, "wall").size());
        assertEquals(1, getEntities(res, "player").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(1, getEntities(res, "exit").size());
        assertEquals(1, getEntities(res,"bomb").size());
    }

    @Test 
    @DisplayName("Cannot pick up placed bomb.")
    public void testCannotPickUpPlacedBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        // player picks up bomb
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getInventory(res, "bomb").size());

        res = dmc.tick(Direction.RIGHT);

        try {
            res = dmc.tick(getInventory(res, "bomb").get(0).getId());
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        // only 2 entities remain on the map, the rest is within explosive radius
        assertEquals(0, getInventory(res, "bomb").size());
        assertEquals(1, getEntities(res, "bomb").size());
    }


    @Test
    @DisplayName("Cardinally adjacent entities are destroyed when bomb explodes")
    public void testDestroySurroundingEntities() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        // player walks RIGHT to trigger switch with boulder
        dmc.tick(Direction.RIGHT);

        // player picks up bomb
        dmc.tick(Direction.DOWN);

        res = dmc.tick(Direction.RIGHT);

        try {
            res = dmc.tick(getInventory(res, "bomb").get(0).getId());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }

        // only 2 entities remain on the map, the rest is within explosive radius
        assertEquals(0, getInventory(res, "bomb").size());
        assertEquals(1, getEntities(res, "player").size());
        assertEquals(1, getEntities(res, "exit").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(0, getEntities(res,"boulder").size());
        assertEquals(0, getEntities(res, "treasure").size());
        assertEquals(0, getEntities(res, "wall").size());
    }
}

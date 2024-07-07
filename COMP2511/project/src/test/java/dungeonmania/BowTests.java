package dungeonmania;
import dungeonmania.util.Direction;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;


public class BowTests {
    @Test  
    @DisplayName("Player does not have enough materials for bow")
    public void testBowInsufficentMaterial() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bowTest_createBow", "c_bowTest_createBow");
        assertEquals(3, getEntities(res, "arrow").size());
        assertEquals(1, getEntities(res,"wood").size());
        
        // player walks to pick up materials for creating the bow
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(3, getInventory(res, "arrow").size());
        assertEquals(0, getInventory(res, "wood").size());

        assertThrows(InvalidActionException.class, () -> {
            dmc.build("bow");
        });
        
        // player inventory should not have a bow, arrows should still be in inventory
        assertEquals(0, getInventory(res, "bow").size());
        assertEquals(3, getInventory(res, "arrow").size());
    }

    @Test  
    @DisplayName("Bow is created")
    public void testBowCreate() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bowTest_createBow", "c_bowTest_createBow");
        assertEquals(3, getEntities(res, "arrow").size());
        assertEquals(1, getEntities(res,"wood").size());
        
        // player walks to pick up materials for creating the bow
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "wood").size());

        try {
            res = dmc.build("bow");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        }
        // player inventory should now have 1 bow
        assertEquals(1, getInventory(res, "bow").size());
        assertEquals(0, getInventory(res, "arrow").size());
        assertEquals(0, getInventory(res, "wood").size());
    }

    @Test  
    @DisplayName("Bow will break after battle")
    public void testBowDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bowTest_createBow", "c_bowTest_createBow");
        assertEquals(3, getEntities(res, "arrow").size());
        assertEquals(1, getEntities(res,"wood").size());
        
        // player walks to pick up materials for creating the bow
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "wood").size());

        try {
            res = dmc.build("bow");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        }
        // player inventory should now have 1 bow
        assertEquals(1, getInventory(res, "bow").size());
        assertEquals(0, getInventory(res, "arrow").size());
        assertEquals(0, getInventory(res, "wood").size());

        res = dmc.tick(Direction.NONE);
        // after 5 ticks, the zombie should spawn in the same square the player is on
        // player engages in battle with zombie and bow should break (bow durability = 1)
        // enemy dies in one round (2-(2*5/5) = 0), without bow enemy shouldn't die in one round (2-(5/5) = 1)
        assertEquals(0, getInventory(res, "bow").size());
    }
}

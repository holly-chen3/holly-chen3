package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;

public class DungeonResponseTests {
    @Test
    @DisplayName("Test dungeon response with correct dungeon map name is generated")
    public void testCorrectDungeonName() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");

        // check expected result is met
        assertEquals(res.getDungeonName(), "d_movementTest_testMovementDown");
    }
    
    @Test
    @DisplayName("Test that a dungeon is overwritten when a new game is started with dungeon response")
    public void testOverWrittenDungeon() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        DungeonResponse newDungeonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");

        // assert ids of dungeons are not equal
        assertNotEquals(initDungeonRes.getDungeonId(), newDungeonRes.getDungeonId());
    }

    @Test
    @DisplayName("Test for exceptions regarding dungeon name")
    public void testExceptionNames() {
        DungeonManiaController dmc = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, () -> {
            dmc.newGame("d", "c");
        });
    }

    @Test
    @DisplayName("Test for exceptions regarding config")
    public void testExceptionConfig() {
        DungeonManiaController dmc = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, () -> {
            dmc.newGame("d_movementTest_testMovementDown", "c");
        });
    }
    
    @Test
    @DisplayName("Test that dungeon response returns null for a non-existing dungeon")
    public void testNoDungeon() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertEquals(dmc.getDungeonResponseModel(), null);
    }
}

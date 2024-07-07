package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static dungeonmania.TestUtils.getEntities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class HydraTests {
    @Test 
    @DisplayName("Test the hydra can move away from its initial position in a random direction")
    public void testHydraRandomMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_hydraTest_randomMovement", "c_hydraTest_movementBlocked");

        // allow hydra to move once
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualHydra = getEntities(actualDungonRes, "hydra").stream().findFirst().orElse(null);

        // create the unexpected result
        EntityResponse unexpectedHydra = new EntityResponse(actualHydra.getId(), actualHydra.getType(), new Position(8, 1), false);

        // assert after movement
        assertNotEquals(actualHydra, unexpectedHydra);   
    }

    @Test
    @DisplayName("Test the hydra is blocked by walls, boulders and portals")
    public void testBlocksMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_hydraTest_movementBlocked", "c_hydraTest_movementBlocked");

        // allow hydra to move once
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse actualHydra = getEntities(actualDungonRes, "hydra").stream().findFirst().orElse(null);

        // create the expected result
        EntityResponse expectedHydra = new EntityResponse(actualHydra.getId(), actualHydra.getType(), new Position(1, 3), false);

        // assert after movement
        assertEquals(actualHydra, expectedHydra);
    }

}


    


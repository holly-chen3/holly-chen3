package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PlayerPickupTests {
    @Test
    @DisplayName("Test one collectable is picked up by player")
    public void testCollectablePickup() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeysTest_useKeyWalkThroughOpenDoor", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(1, getEntities(res, "key").size());
        // player walks into a collectable item
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, getEntities(res, "player").get(0).getPosition());
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(0, getEntities(res, "key").size());
    }

    @Test
    @DisplayName("Test more than one collectable is picked up by player")
    public void testTwoCollectablePickup() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_collectableTest_twoCollectables", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(1, getEntities(res, "key").size());
        assertEquals(1, getEntities(res, "treasure").size());

        // player walks into a collectable item
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, getEntities(res, "player").get(0).getPosition());
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(0, getEntities(res, "key").size());
        assertEquals(0, getEntities(res, "treasure").size());
    }

    @Test
    @DisplayName("Test only one key can be picked up by player")
    public void testTwoKeyPickup() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_twoKeyTest_testCollectable", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(2, getEntities(res, "key").size());

        // player walks into key
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, getEntities(res, "player").get(0).getPosition());
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getEntities(res, "key").size());

        // player walks into another key
        res = dmc.tick(Direction.DOWN);
        assertNotEquals(pos, getEntities(res, "player").get(0).getPosition());
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getEntities(res, "key").size());
    }
}

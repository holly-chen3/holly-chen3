package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import static dungeonmania.TestUtils.getGoals;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SunStoneTests {
    @Test
    @DisplayName("Test sunstone can be picked up by player")
    public void testSunStonePickup() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_properties", "c_complexGoalsTest_andAll");
        assertEquals(1, getEntities(res, "sun_stone").size());
        // player walks into a collectable item
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(0, getEntities(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test sunstone can be used during building entities and retained after usage")
    public void testSunStoneBuidables() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_properties", "c_complexGoalsTest_andAll");
        assertEquals(1, getEntities(res, "sun_stone").size());
        assertEquals(2, getEntities(res,"wood").size());
        // player walks into a collectable item
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(2, getInventory(res,"wood").size());

        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        }
    
        assertEquals(1, getInventory(res, "shield").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(0, getInventory(res, "wood").size());
    }  

    @Test
    @DisplayName("Test sunstone passes treasure goals")
    public void basicSunstoneTreasureGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_properties", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        assertTrue(getGoals(res).contains(":treasure"));
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":treasure"));
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test sunstone can be used to open door")
    public void testSunStoneOpenDoors() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_properties", "c_complexGoalsTest_andAll");
        assertEquals(1, getEntities(res, "sun_stone").size());
        assertEquals(2, getEntities(res,"wood").size());
        // player walks into a collectable item
        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());

        Position posDoor = getEntities(res, "door").get(0).getPosition();
        Position posPlayer = getEntities(res, "player").get(0).getPosition();

        assertEquals(posDoor, posPlayer);
        assertEquals(1, getInventory(res, "sun_stone").size());
    }  
}
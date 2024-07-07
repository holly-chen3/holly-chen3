package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.getEntities;

public class GoalTests {
       
    @Test
    @DisplayName("Testing a once player reaches exit, the goal is completed")
    public void basicExitGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");

        assertTrue(getGoals(res).contains(":exit"));
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":exit"));
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":exit"));
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Testing once player has moved the boulder onto floor switch, the goal is completed")
    public void basicBoulderGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_switchTest_switchBoulder", "c_movementTest_testMovementDown");

        assertTrue(getGoals(res).contains(":boulders"));
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":boulders"));
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Testing at least x amount of treasures must be completed to achieve the treasure goal")
    public void basicTreasureGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_treasure", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        assertTrue(getGoals(res).contains(":treasure"));
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":treasure"));
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Testing at least x amount of enemies must be killed to achieve the enemy goal")
    public void basicEnemyGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_enemy", "c_battleTests_basicMercenaryMercenaryDies");

        assertTrue(getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));
        assertEquals("", getGoals(res));
    }
    
    @Test
    @DisplayName("Testing at least x amount of enemies must be killed to achieve the enemy goal - spawner")
    public void basicSpawnerGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_twoSpawners", "c_battleTests_basicMercenaryMercenaryDies");

        assertTrue(getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.UP);
        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        try {
            res = dmc.interact(zombId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        assertFalse(getGoals(res).contains(":enemies"));
        assertEquals("", getGoals(res));
    }
    
    @Test
    @DisplayName("Testing enemy AND exit goal conditions")
    public void complexEnemyAndExitGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_andEnemyExit", "c_battleTests_basicMercenaryMercenaryDies");

        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":exit"));
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":exit"));
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":exit"));
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Testing boulder OR exit goal conditions")
    public void complexBoulderOrExitGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_orBoulderExit", "c_battleTests_basicMercenaryMercenaryDies");

        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":exit"));
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":boulders"));
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Testing at least x amount of enemies must be killed to achieve the enemy goal - spawner with OR")
    public void basicSpawnerComplexGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_orAll", "c_battleTests_basicMercenaryMercenaryDies");

        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":treasure"));
        res = dmc.tick(Direction.UP);
        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        try {
            res = dmc.interact(zombId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        assertFalse(getGoals(res).contains(":enemies"));
        assertEquals("", getGoals(res));
    }
}

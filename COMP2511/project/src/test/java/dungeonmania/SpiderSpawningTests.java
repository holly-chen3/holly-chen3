package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class SpiderSpawningTests {
    @Test
    @DisplayName("Test spawn rate of three")
    public void testSpiderSpawning() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderSpawningTest_basic", "c_spiderTest_spawning");

        // move player downward (i.e. 1 tick)
        res = dmc.tick(Direction.DOWN);
        // ensure that there are no spiders spawned
        assertEquals(0, getEntities(res, "spider").size());

        // move player downward (i.e. 2 ticks)
        res = dmc.tick(Direction.DOWN);
        // ensure that there are no spiders spawned
        assertEquals(0, getEntities(res, "spider").size());

        // move player downward (i.e. 3 ticks)
        res = dmc.tick(Direction.DOWN);
        // 1 spider should have spawned
        assertEquals(1, getEntities(res, "spider").size());

        // move player downward twice (i.e. 4, 5 ticks)
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        // should still be 1 zombie
        assertEquals(1, getEntities(res, "spider").size());

        // move player downward (i.e. 6 ticks)
        res = dmc.tick(Direction.DOWN);
        // now, there should be 2 spiders in the dungeon
        assertEquals(2, getEntities(res, "spider").size());
    }
}
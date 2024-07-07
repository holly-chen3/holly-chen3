package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import dungeonmania.exceptions.InvalidActionException;

public class ZombieToastSpawnerTests {
    @Test
    @DisplayName("Test zombie spawns in cardinally adjacent top square with no blockage")
    public void testZombieSpawnerTopSquare() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_simpleZTSpawner", "c_zombieToastSpawnerTest_spawningZombies");

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // after tick, get the zombie's position and check that it is in the top adjacent square to the spawner
        Position posZomb = getEntities(res, "zombie_toast").get(0).getPosition();

        Position posSpawnerTop = new Position (8, 1);

        assertEquals(posZomb, posSpawnerTop);
    }

    @Test
    @DisplayName("Test zombie spawns in cardinally adjacent right square when top square is blocked")
    public void testZombieSpawnerRightSquare() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_oneBlock", "c_zombieToastSpawnerTest_spawningZombies");

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // after tick, get the zombie's position and check that it is in the right adjacent square to the spawner
        Position posZomb = getEntities(res, "zombie_toast").get(0).getPosition();

        Position posSpawnerRight = new Position (9, 2);

        assertEquals(posZomb, posSpawnerRight);
    }

    @Test
    @DisplayName("Test zombie spawns in cardinally adjacent bottom square when top and right square are blocked")
    public void testZombieSpawnerBottomSquare() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_twoBlocks", "c_zombieToastSpawnerTest_spawningZombies");

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // after tick, get the zombie's position and check that it is in the bottom adjacent square to the spawner
        Position posZomb = getEntities(res, "zombie_toast").get(0).getPosition();

        Position posSpawnerBottom = new Position (8, 3);

        assertEquals(posZomb, posSpawnerBottom);
    }

    @Test
    @DisplayName("Test zombie spawns in cardinally adjacent left square when top, right and bottom squares are blocked")
    public void testZombieSpawnerLeftSquare() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_threeBlocks", "c_zombieToastSpawnerTest_spawningZombies");

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // after tick, get the zombie's position and check that it is in the left adjacent square to the spawner
        Position posZomb = getEntities(res, "zombie_toast").get(0).getPosition();

        Position posSpawnerLeft = new Position (7, 2);

        assertEquals(posZomb, posSpawnerLeft);
    }

    @Test
    @DisplayName("Test zombie spawns in cardinally adjacent top square when left, right and bottom squares are blocked")
    public void testZombieSpawnerTopSquareOthersBlocked() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_threeBlocksNotTop", "c_zombieToastSpawnerTest_spawningZombies");

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // after tick, get the zombie's position and check that it is in the top adjacent square to the spawner
        Position posZomb = getEntities(res, "zombie_toast").get(0).getPosition();

        Position posSpawnerTop = new Position (8, 1);

        assertEquals(posZomb, posSpawnerTop);
    }

    @Test
    @DisplayName("Test zombie can not spawn when all cardinally adjacent squares are blocked")
    public void testZombieSpawnerAllBlocked() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_allBlocked", "c_zombieToastSpawnerTest_spawningZombies");

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // check that there's no zombies as spawner is completely blocked
        assertEquals(0, getEntities(res, "zombie_toast").size());
    }

    @Test
    @DisplayName("Test zombie can spawn on collectable entity")
    public void testZombieSpawnerCollectableEntity() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_collectableAtTop", "c_zombieToastSpawnerTest_spawningZombies");

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // after tick, get the zombie's position and check that it is in the top adjacent square to the spawner
        Position posZomb = getEntities(res, "zombie_toast").get(0).getPosition();

        Position posSpawnerTop = new Position (8, 1);

        Position posTreasure = getEntities(res, "treasure").get(0).getPosition();
        
        assertEquals(posZomb, posSpawnerTop);

        // check that the zombie and the treasure co-exist on the same square
        assertEquals(posZomb, posTreasure);
    }

    @Test
    @DisplayName("Test zombie can spawn on floor switch")
    public void testZombieSpawnerFloorSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_floorSwitch", "c_zombieToastSpawnerTest_spawningZombies");

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // after tick, get the zombie's position and check that it is in the top adjacent square to the spawner
        Position posZomb = getEntities(res, "zombie_toast").get(0).getPosition();

        Position posSpawnerTop = new Position (8, 1);

        Position posSwitch = getEntities(res, "switch").get(0).getPosition();
        
        assertEquals(posZomb, posSpawnerTop);

        // check that the zombie and the floor switch co-exist on the same square
        assertEquals(posZomb, posSwitch);
    }

    @Test
    @DisplayName("Test zombie spawner blocked by another zombie spawner")
    public void testZombieSpawnerTwoSpawners() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_twoSpawners", "c_zombieToastSpawnerTest_spawningZombies");

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // after tick, a zombie should've spawned from the spawner in (8,1) up in (8,0)
        Position posZomb = getEntities(res, "zombie_toast").get(1).getPosition();

        Position posSpawnerTop = new Position (8, 0);
        
        assertEquals(posZomb, posSpawnerTop);

        // the second zombie spawner should spawn the zombie to the right as the top is blocked
        // by the first spawner
        Position posZombBlocked = getEntities(res, "zombie_toast").get(0).getPosition();

        Position posBlockedSpawnerRight = new Position (9,2);

        assertEquals(posZombBlocked, posBlockedSpawnerRight);
    }

    @Test
    @DisplayName("Test spawn rate of three")
    public void testZombieSpawnerThreeSpawnRate() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_simpleZTSpawner", "c_zombieToastSpawnerTest_threeSpawnRate");

        // move player downward (i.e. 1 tick)
        res = dmc.tick(Direction.DOWN);
        // ensure that there are no zombies spawned
        assertEquals(0, getEntities(res, "zombie_toast").size());

        // move player downward (i.e. 2 ticks)
        res = dmc.tick(Direction.DOWN);
        // ensure that there are no zombies spawned
        assertEquals(0, getEntities(res, "zombie_toast").size());

        // move player downward (i.e. 3 ticks)
        res = dmc.tick(Direction.DOWN);
        // 1 zombie should have spawned
        assertEquals(1, getEntities(res, "zombie_toast").size());

        // move player downward twice (i.e. 4, 5 ticks)
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        // should still be 1 zombie
        assertEquals(1, getEntities(res, "zombie_toast").size());

        // move player downward (i.e. 6 ticks)
        res = dmc.tick(Direction.DOWN);
        // now, there should be 2 zombies in the dungeon
        assertEquals(2, getEntities(res, "zombie_toast").size());
    }

    @Test
    @DisplayName("Test spawn rate of two")
    public void testZombieSpawnerTwoSpawnRate() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_simpleZTSpawner", "c_zombieToastSpawnerTest_twoSpawnRate");

        // move player downward (i.e. 1 tick)
        res = dmc.tick(Direction.DOWN);
        // ensure that there are no zombies spawned
        assertEquals(0, getEntities(res, "zombie_toast").size());

        // move player downward (i.e. 2 ticks)
        res = dmc.tick(Direction.DOWN);
        // 1 zombie should have spawned
        assertEquals(1, getEntities(res, "zombie_toast").size());

        // move player downward (i.e. 3 ticks)
        res = dmc.tick(Direction.DOWN);
        // should still be 1 zombie
        assertEquals(1, getEntities(res, "zombie_toast").size());

        // move player downward (i.e. 4 ticks)
        res = dmc.tick(Direction.DOWN);
        // should now be 2 zombies
        assertEquals(2, getEntities(res, "zombie_toast").size());

        // move player downward twice (i.e. 6 ticks)
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        // now, there should be 3 zombies in the dungeon
        assertEquals(3, getEntities(res, "zombie_toast").size());
    }

    @Test
    @DisplayName("Test no zombies spawn with zero spawn rate")
    public void testZombieSpawnerZeroSpawnRate() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_simpleZTSpawner", "c_movementTest_testMovementDown");

        // no ticks have occurred, should be 0 zombies
        assertEquals(0, getEntities(res, "zombie_toast").size());

        // move player downward
        res = dmc.tick(Direction.DOWN);

        // 1 tick has occurred, but spawn rate is 0 so there should still be 0 zombies
        assertEquals(0, getEntities(res, "zombie_toast").size());
    }

    @Test
    @DisplayName("Test player breaks spawner from top")
    public void testZombieSpawnerBreakTop() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_breaking", "c_movementTest_testMovementDown");

        // assert there is 1 spawner in the dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());

        // move player downward from (8,0) to (8,1)
        res = dmc.tick(Direction.DOWN);

        // player collects the sword which is at (8,1) and is now right on top of the spawner
        assertEquals(1, getInventory(res, "sword").size());

        // player breaks spawner from one square above
        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        try {
            res = dmc.interact(zombId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        // assert that there is no spawners left in dungeon
        assertEquals(0, getEntities(res, "zombie_toast_spawner").size());
    }

    @Test
    @DisplayName("Test player breaks spawner from right")
    public void testZombieSpawnerBreakRight() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_breaking", "c_movementTest_testMovementDown");

        // assert there is 1 spawner in the dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());

        // move player downward from (8,0) to (8,1)
        res = dmc.tick(Direction.DOWN);

        // player collects the sword which is at (8,1) and is now right on top of the spawner
        assertEquals(1, getInventory(res, "sword").size());

        // player moves right and then down to stand to the right of the spawner
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // player breaks spawner from the square to the right
        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        try {
            res = dmc.interact(zombId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }

        // assert that there is no spawners left in dungeon
        assertEquals(0, getEntities(res, "zombie_toast_spawner").size());
    }

    @Test
    @DisplayName("Test player breaks spawner from bottom")
    public void testZombieSpawnerBreakBottom() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_breaking", "c_movementTest_testMovementDown");

        // assert there is 1 spawner in the dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());

        // move player downward from (8,0) to (8,1)
        res = dmc.tick(Direction.DOWN);

        // player collects the sword which is at (8,1) and is now right on top of the spawner
        assertEquals(1, getInventory(res, "sword").size());

        // player moves right, down, down, left
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);

        // player breaks spawner from one square below
        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        try {
            res = dmc.interact(zombId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        // assert that there is no spawners left in dungeon
        assertEquals(0, getEntities(res, "zombie_toast_spawner").size());
    }

    @Test
    @DisplayName("Test player breaks spawner from the left")
    public void testZombieSpawnerBreakLeft() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_breaking", "c_movementTest_testMovementDown");

        // assert there is 1 spawner in the dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());

        // move player downward from (8,0) to (8,1)
        res = dmc.tick(Direction.DOWN);

        // player collects the sword which is at (8,1) and is now right on top of the spawner
        assertEquals(1, getInventory(res, "sword").size());

        // player moves left and then down
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);

        // player breaks spawner from one square to the left
        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        try {
            res = dmc.interact(zombId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        // assert that there is no spawners left in dungeon
        assertEquals(0, getEntities(res, "zombie_toast_spawner").size());
    }
    
    @Test
    @DisplayName("Test player breaks two spawners")
    public void testZombieSpawnerBreaksTwo() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_twoSpawners", "c_movementTest_testMovementDown");

        // assert there are 2 spawners in the dungeon
        assertEquals(2, getEntities(res, "zombie_toast_spawner").size());

        // move player up from (8,4) to (8,3)
        res = dmc.tick(Direction.UP);

        // player collects the sword which is at (8,3) and is now right below one of the spawners
        assertEquals(1, getInventory(res, "sword").size());

        // player breaks spawner from one square below
        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        String zombId2 = getEntities(res, "zombie_toast_spawner").get(1).getId();
        try {
            res = dmc.interact(zombId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        // assert that there is 1 spawner left in dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());

        // player should be able to move on top of the spot that the spawner was on
        res = dmc.tick(Direction.UP);

        // player breaks the second spawner
        try {
            res = dmc.interact(zombId2);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        // assert that there are no spawners left
        assertEquals(0, getEntities(res, "zombie_toast_spawner").size());
    }

    @Test
    @DisplayName("Test player cannot break spawner in a not cardinally-adjacent square")
    public void testZombieSpawnerCannotBreakDistance() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_breaking", "c_movementTest_testMovementDown");

        // assert there is 1 spawner in the dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());

        // move player downward from (8,0) to (8,1)
        res = dmc.tick(Direction.DOWN);

        // player collects the sword which is at (8,1) and is now right on top of the spawner
        assertEquals(1, getInventory(res, "sword").size());

        // move player to the left to (7,1)
        res = dmc.tick(Direction.LEFT);

        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        // player now cannot break the spawner even with a weapon as they are too far
        assertThrows(InvalidActionException.class, () -> {
            dmc.interact(zombId);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            dmc.interact(zombId+"hello");
        });
        
        // assert that there is still 1 spawner in dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());
    }


    @Test
    @DisplayName("Test player cannot break spawner without weapon")
    public void testZombieSpawnerCannotBreakNoWeapon() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_breaking", "c_movementTest_testMovementDown");

        // assert there is 1 spawner in the dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());

        // move player left from (8,0) to (7,0) and then down twice to be next to spawner
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        // player cannot break the spawner without a weapon
        assertThrows(InvalidActionException.class, () -> {
            dmc.interact(zombId);
        });
        
        // assert that there is still 1 spawner in dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());
    }

    @Test
    @DisplayName("Test player cannot break spawner without weapon and in the wrong spot")
    public void testZombieSpawnerCannotBreakNoWeaponWrongSpot() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_breaking", "c_movementTest_testMovementDown");

        // assert there is 1 spawner in the dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());

        // move player left from (8,0) to (7,0)
        res = dmc.tick(Direction.LEFT);

        String zombId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        // player cannot break the spawner without a weapon and in the wrong spot
        assertThrows(InvalidActionException.class, () -> {
            dmc.interact(zombId);
        });
        
        // assert that there is still 1 spawner in dungeon
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());
    }
}
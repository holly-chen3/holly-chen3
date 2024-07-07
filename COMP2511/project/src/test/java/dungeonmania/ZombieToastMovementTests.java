package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import static dungeonmania.TestUtils.getEntities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class ZombieToastMovementTests {
    @Test
    @DisplayName("Test the zombie toast can move away from its initial position in a random direction")
    public void testRandomMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_zombieToastSpawnerTest_simpleZTSpawner", "c_zombieToastSpawnerTest_spawningZombies");

        // allow zombie toast to spawn
        dmc.tick(Direction.DOWN);

        // allow zombie toast to move once
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualZombie = getEntities(actualDungonRes, "zombie_toast").stream().findFirst().orElse(null);

        // create the unexpected result
        EntityResponse unexpectedZombie = new EntityResponse(actualZombie.getId(), actualZombie.getType(), new Position(8, 1), false);

        // assert after movement
        assertNotEquals(actualZombie, unexpectedZombie);
    }

    @Test
    @DisplayName("Test the zombie toast is blocked by walls, portals and closed doors")
    public void testBlockedMovement() {
        /*    
         *        boulder  wall           
         *  exit   zts            door  
         *        portal  portal      
         */
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_zombieToastTest_blockedZombieToast", "c_zombieToastSpawnerTest_spawningZombies");

        // allow zombie toast to spawn
        dmc.tick(Direction.LEFT);

        // allow zombie toast to move once
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse actualZombie = getEntities(actualDungonRes, "zombie_toast").stream().findFirst().orElse(null);

        // create the expected result
        EntityResponse expectedZombie = new EntityResponse(actualZombie.getId(), actualZombie.getType(), new Position(9, 2), false);

        // assert after movement
        assertEquals(actualZombie, expectedZombie);
    }

    @Test
    @DisplayName("Test the zombie toast is blocked by boulders")
    public void testBlockedByBoulders() {
        /*    
         *        boulder  boulder           
         *  exit    zts             boulder  
         *        boulder  boulder      
         */
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_zombieToastTest_blockedZombieToast", "c_zombieToastSpawnerTest_spawningZombies");

        // allow zombie toast to spawn
        dmc.tick(Direction.LEFT);

        // allow zombie toast to move once
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse actualZombie = getEntities(actualDungonRes, "zombie_toast").stream().findFirst().orElse(null);

        // create the expected result
        EntityResponse expectedZombie = new EntityResponse(actualZombie.getId(), actualZombie.getType(), new Position(9, 2), false);

        // assert after movement
        assertEquals(actualZombie, expectedZombie);
    }

    @Test
    @DisplayName("Test the zombie toast is slowed down by swamp tile")
    public void testSwampTile() {
        /*    
         *        boulder  boulder           
         *  exit    zts             swamp
         *        boulder  boulder      
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastTest_swampTile", "c_zombieToastSpawnerTest_spawningZombies");

        // allow zombie toast to spawn
        res = dmc.tick(Direction.NONE);

        List<Direction> path = Arrays.asList(Direction.RIGHT, Direction.NONE, 
        Direction.NONE, Direction.NONE, Direction.NONE, Direction.NONE);

        Position lastPosition = getEntities(res, "zombie_toast").get(0).getPosition();
        for (int i = 0; i < path.size(); i++) {
            res = dmc.tick(Direction.NONE);
            Position currentPosition = getEntities(res, "zombie_toast").get(0).getPosition();
            assertEquals(lastPosition.translateBy(path.get(i)), currentPosition);
            lastPosition = currentPosition;
        }

        res = dmc.tick(Direction.NONE);
        // check that zombie toast moves off of swamp tile after movement factor is applied
        assertNotEquals(new Position(10, 2), getEntities(res, "zombie_toast").get(0).getPosition());
    }
}
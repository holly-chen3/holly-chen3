package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import dungeonmania.exceptions.InvalidActionException;


public class ShieldTests {
    @Test
    @DisplayName("Test player does not have sufficient items to craft shield")
    public void testShieldInsufficientItems1Wood1Key() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_movementTest_testMovementDown");

        /*
         *     []     []   player   []
         * treasure  wood   wood   key
         *     []
         * ztspawner
         */   

        // move player downward from (8,1) to (8,2)
        res = dmc.tick(Direction.DOWN);
        // player collects 1 wood which is at (8,2)
        assertEquals(1, getInventory(res, "wood").size());

        // move player right from (8,2) to (9,2)
        res = dmc.tick(Direction.RIGHT);
        // player collects 1 key at (9,2)
        assertEquals(1, getInventory(res, "key").size());

        // player attempts to build a shield but cannot (missing 1 wood)
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("shield");
        });
        
        // assert that there isn't a shield in inventory
        assertEquals(0, getInventory(res, "shield").size());
    }

    @Test
    @DisplayName("Test player does not have sufficient items to craft shield2")
    public void testShieldInsufficientItems2WoodOnly() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_movementTest_testMovementDown");

        /*
         *     []     []   player   []
         * treasure  wood   wood   key
         * ztspawner
         */  

        // move player downward from (8,1) to (8,2)
        res = dmc.tick(Direction.DOWN);
        // player collects 1 wood which is at (8,2)
        assertEquals(1, getInventory(res, "wood").size());

        // move player left from (8,2) to (9,2)
        res = dmc.tick(Direction.LEFT);
        // player collects 1 more wood at (9,2)
        assertEquals(2, getInventory(res, "wood").size());

        // player attempts to build a shield but cannot (missing 1 wood)
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("shield");
        });
        
        // assert that there isn't a shield in inventory
        assertEquals(0, getInventory(res, "shield").size());
    }

    @Test
    @DisplayName("Test invalid buildable string")
    public void testShieldIllegalArg() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_movementTest_testMovementDown");

        // move player downward from (8,1) to (8,2)
        res = dmc.tick(Direction.DOWN);
        // player collects 1 wood which is at (8,2)
        assertEquals(1, getInventory(res, "wood").size());

        // move player right from (8,2) to (9,2)
        res = dmc.tick(Direction.RIGHT);
        // player collects 1 key at (9,2)
        assertEquals(1, getInventory(res, "key").size());

        // player attempts to build a shield but cannot (missing 1 wood)
        assertThrows(IllegalArgumentException.class, () -> {
            dmc.build("hello");
        });
    }

    @Test
    @DisplayName("Test player can create shield Treasure")
    public void testShieldCreateTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_movementTest_testMovementDown");

        /*
         *     []     []   player   []
         * treasure  wood   wood   key
         *     []
         * ztspawner
         */   

        // move player downward from (8,1) to (8,2)
        res = dmc.tick(Direction.DOWN);
        // player collects 1 wood which is at (8,2)
        assertEquals(1, getInventory(res, "wood").size());

        // move player left from (8,2) to (7,2)
        res = dmc.tick(Direction.LEFT);
        // player collects 1 more wood at (7,2)
        assertEquals(2, getInventory(res, "wood").size());

        // move player left from (7,2) to (6,2)
        res = dmc.tick(Direction.LEFT);
        // player collects treasure at (6,2)
        assertEquals(1, getInventory(res, "treasure").size());

        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        } 
        
        // assert that there is a shield in inventory
        assertEquals(1, getInventory(res, "shield").size());

        // assert that 2 wood and 1 key is not in the inventory anymore
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "treasure").size());
    }

    @Test
    @DisplayName("Test player can create shield Key")
    public void testShieldCreateKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_movementTest_testMovementDown");

        /*
         *     []     []   player   []
         * treasure  wood   wood   key
         *     []
         * ztspawner
         */   

        // move player downward from (8,1) to (8,2)
        res = dmc.tick(Direction.DOWN);
        // player collects 1 wood which is at (8,2)
        assertEquals(1, getInventory(res, "wood").size());

        // move player left from (8,2) to (7,2)
        res = dmc.tick(Direction.LEFT);
        // player collects 1 more wood at (7,2)
        assertEquals(2, getInventory(res, "wood").size());

        // move player left from (7,2) to (9,2)
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // player collects treasure at (6,2)
        assertEquals(1, getInventory(res, "key").size());

        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        } 
        
        // assert that there is a shield in inventory
        assertEquals(1, getInventory(res, "shield").size());

        // assert that 2 wood and 1 key is not in the inventory anymore
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "key").size());
    }

    @Test
    @DisplayName("Test player create shield with sunstone")
    public void testShieldCreateSunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_movementTest_testMovementDown");

        /*
         *     []     []   player   []
         * treasure  wood   wood   key
         *     []  sunstone
         * ztspawner
         */   

        // move player downward from (8,1) to (8,2)
        res = dmc.tick(Direction.DOWN);
        // player collects 1 wood which is at (8,2)
        assertEquals(1, getInventory(res, "wood").size());

        // move player left from (8,2) to (7,2)
        res = dmc.tick(Direction.LEFT);
        // player collects 1 more wood at (7,2)
        assertEquals(2, getInventory(res, "wood").size());

        // move player down to (7.3)
        res = dmc.tick(Direction.DOWN);
        // player collects sunstone at (7,3)
        assertEquals(1, getInventory(res, "sun_stone").size());

        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        } 
        
        // assert that there is a shield in inventory
        assertEquals(1, getInventory(res, "shield").size());

        // assert that 2 wood is not in the inventory anymore but sunstone remains
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test player creates shield - have all required materials - uses sunstone first")
    public void testShieldCreateSunstoneFirst() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_movementTest_testMovementDown");

        /*
         *     []     []   player   []
         * treasure  wood   wood   key
         *     []  sunstone
         * ztspawner
         */   

        // move player downward from (8,1) to (8,2)
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        assertEquals(1, getInventory(res, "key").size());
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        
        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        } 
        
        // assert that there is a shield in inventory
        assertEquals(1, getInventory(res, "shield").size());

        // assert that 2 wood is not in the inventory anymore
        assertEquals(0, getInventory(res, "wood").size());
        // however, everything else should still exist
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test player creates shield - uses up key first between key and treasure")
    public void testShieldCreateKeyFirst() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_movementTest_testMovementDown");

        /*
         *     []     []   player   []
         * treasure  wood   wood   key
         *     []  sunstone
         * ztspawner
         */   

        // move player so that treasure is collected first
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getInventory(res, "key").size());
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "treasure").size());
        
        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        } 
        
        // assert that there is a shield in inventory
        assertEquals(1, getInventory(res, "shield").size());

        // assert that 2 wood and key are not in the inventory anymore
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "key").size());

        // however, treasure and sunstone should still be in the inventory
        assertEquals(1, getInventory(res, "treasure").size());
    }

    @Test
    @DisplayName("Test player creates shield - uses up sunstone first between treasure and sunstone")
    public void testShieldCreateSunstoneFirstNotTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_movementTest_testMovementDown");

        /*
         *     []     []   player   []
         * treasure  wood   wood   key
         *     []  sunstone
         * ztspawner
         */   

        // move player so that treasure is collected first
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getInventory(res, "key").size());
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        
        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        } 
        
        // assert that there is a shield in inventory
        assertEquals(1, getInventory(res, "shield").size());

        // assert that 2 wood and key are not in the inventory anymore
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test shield will protect player")
    public void testShieldDefendsPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        // The config file spawns the player with 1.2 health and enemy attack damage of 12
        // The dungeon file will spawn the zombie on the 4th count
        DungeonResponse res = dmc.newGame("d_shieldTest", "c_shieldTests");

        /*
         *     []     []   player   []
         * treasure  wood   wood   key
         *     []
         * ztspawner
         */  

        // move player downward from (8,1) to (8,2)
        res = dmc.tick(Direction.DOWN);
        // player collects 1 wood which is at (8,2)

        // move player left from (8,2) to (7,2)
        res = dmc.tick(Direction.LEFT);
        // player collects 1 more wood at (7,2)

        // move player left from (7,2) to (6,2)
        res = dmc.tick(Direction.LEFT);
        // player collects treasure at (6,2)
       
        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        } 
        
        // assert that there is a shield in inventory
        assertEquals(1, getInventory(res, "shield").size());

        
        res = dmc.tick(Direction.NONE);
        assertEquals(0, getEntities(res, "zombie_toast").size());
        // move player down from (6,2) to (6,3) - player is now one square above ztspawner
        res = dmc.tick(Direction.DOWN);
        
        // spawner spawns a zombie in (6,3) - spawner always spawns 1 above
        // since player is in the same square as zombie - they'll engage in a battle
        // using battle calculation: player health = 1.2 - ((12-2)/10) = 0.2 (player should still be alive)
        // without shield, player will be dead = 1.2 - 12/10 = 0
        assertNotEquals(null, getPlayer(res));
        // assert that there are no zombies left
        assertEquals(0, getEntities(res, "zombie_toast").size());
        // assert that the shield is gone (durable = 1)
        assertEquals(0, getInventory(res, "shield").size());
    }

    @Test  
    @DisplayName("Shield is created with 2 wood and 1 key")
    public void testShieldCreateWithKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest_createShield", "c_shieldTest_createShield");
        assertEquals(1, getEntities(res, "key").size());
        assertEquals(1, getEntities(res, "treasure").size());
        assertEquals(2, getEntities(res,"wood").size());
        
        // player walks to pick up materials for creating the shield
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(2, getInventory(res, "wood").size());

        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        }
        // player inventory should now have 1 bow
        assertEquals(1, getInventory(res, "shield").size());
        assertEquals(0, getInventory(res, "key").size());
        assertEquals(0, getInventory(res, "wood").size());
    }

    @Test  
    @DisplayName("Shield is created with 2 wood and 1 treasure")
    public void testShieldCreateWithTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldTest_createShield", "c_shieldTest_createShield");
        assertEquals(1, getEntities(res, "key").size());
        assertEquals(1, getEntities(res, "treasure").size());
        assertEquals(2, getEntities(res,"wood").size());
        
        // player walks to pick up materials for creating the shield
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(2, getInventory(res, "wood").size());

        try {
            res = dmc.build("shield");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        }
        // player inventory should now have 1 shield
        assertEquals(1, getInventory(res, "shield").size());
        assertEquals(0, getInventory(res, "treasure").size());
        assertEquals(0, getInventory(res, "wood").size());
    }
}

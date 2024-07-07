package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

import dungeonmania.exceptions.InvalidActionException;

public class MidnightArmourTests {
    @Test
    @DisplayName("Test player cannot craft midnight armour due to zombies in dungeon")
    public void testMidnightArmourZombies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombies", "c_midnightArmourTest_zombie");

         /*
         *      player   
         *      sword
         *     sunstone
         * wall   []     wall
         *     ztspawner
         */     

        // move player downward from (8,1) to (8,2)
        res = dmc.tick(Direction.DOWN);
        // player collects 1 sword which is at (8,2)
        assertEquals(1, getInventory(res, "sword").size());

        // move player downward to collect sunstone at (8,3)
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "sun_stone").size());

        // assert there is one zombie in the dungeon now
        assertEquals(1, getEntities(res, "zombie_toast").size());

        // player attempts to build midnight armour but cannot (missing 1 wood)
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("midnight_armour");
        });
        
        // assert that there isn't a midnight armour in inventory
        assertEquals(0, getInventory(res, "midnight_armour").size());
    }

    @Test
    @DisplayName("Test player can craft midnight armour - no zombies")
    public void testMidnightArmourCraft() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombies", "c_movementTest_testMovementDown");


        /*
         *      player   
         *      sword
         *     sunstone
         * wall   []     wall
         *     ztspawner
         */      

        // move player downward from (8,1) to (8,2)
        res = dmc.tick(Direction.DOWN);
        // player collects 1 sword which is at (8,2)
        assertEquals(1, getInventory(res, "sword").size());

        // move player downward to collect sunstone at (8,3)
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "sun_stone").size());

        // assert there are no zombies in the dungeon 
        assertEquals(0, getEntities(res, "zombie_toast").size());

        // player attempts to build midnight armour
        try {
            res = dmc.build("midnight_armour");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        // assert that there is midnight armour in inventory
        assertEquals(1, getInventory(res, "midnight_armour").size());
        // assert that sword and sunstone are not in the inventory anymore
        assertEquals(0, getInventory(res, "sword").size());
        assertEquals(0, getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test midnight armour gives extra attack damage and defence")
    public void testMidnightArmourAttackDefence() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombies", "c_midnightArmourTest_zombiesBattle");

        /*
         *      player   
         *      sword
         *     sunstone
         * wall   []     wall
         *     ztspawner
         */   

        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "sword").size());
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "sun_stone").size());

        // assert there are no zombies in the dungeon 
        assertEquals(0, getEntities(res, "zombie_toast").size());

        // player attempts to build midnight armour
        try {
            res = dmc.build("midnight_armour");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        // assert that there is midnight armour in inventory
        assertEquals(1, getInventory(res, "midnight_armour").size());

        // player battles zombie 
        res = dmc.tick(Direction.NONE);
        assertEquals(1, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.NONE);

        for (RoundResponse round : res.getBattles().get(0).getRounds()) {
            // in config file, midnight armour defence should cancel out zombie attack, thus resulting 
            // in no change in player's health
            assertEquals(round.getWeaponryUsed().get(0).getType(), "midnight_armour");
            assertEquals(-0.0, round.getDeltaCharacterHealth());
            // without midar, enemy health should decrease by 0.2 but with boosted attack from midar
            // enemy health should decrease by 0.8
            assertNotEquals(-0.2, round.getDeltaEnemyHealth());
            assertEquals(-0.8, round.getDeltaEnemyHealth());
        }
    }
}

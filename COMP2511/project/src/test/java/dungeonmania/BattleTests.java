package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class BattleTests {

    private static void genericSwordSequence(DungeonManiaController controller) {
        controller.tick(Direction.DOWN);
        controller.tick(Direction.UP);
    }

    // these tests work for now when spiders and zombie toasts and mercenaries are not moving.
    private static DungeonResponse genericSpiderSequence(DungeonManiaController controller, String configFile, boolean sword) {
        /*
         *  exit  spider wall  wall
         * player  [  ]        wall
         *  sword         wall  wall
         *         wall
         */
        controller.newGame("d_battleTest_basicSpider", configFile);
        if (sword) {
            genericSwordSequence(controller);
        }
        controller.tick(Direction.RIGHT);
        return controller.tick(Direction.UP);
    }

    private static DungeonResponse genericZombieSequence(DungeonManiaController controller, String configFile, boolean sword) {
        /*
         *  exit  spider  wall  wall
         * player sword   merc  wall
         * wall   zombie  wall  wall
         *         wall
         */
        controller.newGame("d_battleTest_basicZombieToast", configFile);
        if (sword) {
            genericSwordSequence(controller);
        }
        return controller.tick(Direction.RIGHT);
    }

    private static DungeonResponse genericMercenarySequence(DungeonManiaController controller, String configFile, boolean sword) {
        /*
         *  exit         wall  wall
         * player  [  ]  merc  wall
         * sword         wall  wall
         *         wall
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_justMercenary", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");
        
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        if (sword) {
            genericSwordSequence(controller);
        }
        
        return controller.tick(Direction.RIGHT);
    }

    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10));
            assertEquals(round.getDeltaEnemyHealth(), -(playerAttack / 5));
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    public boolean isWeaponinRound(String expectedWeaponUsed, BattleResponse battle) {
        List<RoundResponse> rounds = battle.getRounds();
        List<String> weaponsUsed = new ArrayList<>();
        for (RoundResponse round : rounds) {
            List<ItemResponse> weaponryUsed = round.getWeaponryUsed();
            for (ItemResponse item: weaponryUsed) {
                String type = item.getType();
                weaponsUsed.add(type);
            }
        }
        for (String weaponUsed: weaponsUsed) {
            if (weaponUsed.equals(expectedWeaponUsed)) {
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("Test weaponry used is added correctly")
    public void testWeaponryUsed() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_swordPlayerWins", true);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertTrue(isWeaponinRound("sword", battle));
        assertEquals(0, countEntityOfType(postBattleResponse, "mercenary"));
        assertEquals(0, getInventory(postBattleResponse, "sword").size());
    }

    @Test
    @DisplayName("Test basic battle calculations - spider - player loses")
    public void testHealthBelowZeroSpider() {
        DungeonManiaController controller = new DungeonManiaController();
        
        // now player is at spider
        DungeonResponse postBattleResponse = genericSpiderSequence(controller, "c_battleTests_basicMercenaryPlayerDies", false);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("spider", battle, false, "c_battleTests_basicMercenaryPlayerDies");
    }


    @Test
    @DisplayName("Test basic battle calculations - spider - player wins")
    public void testRoundCalculationsSpider() {
        DungeonManiaController controller = new DungeonManiaController();
        // now player is at spider
        BattleResponse battle = genericSpiderSequence(controller, "c_battleTests_basicMercenaryMercenaryDies", false).getBattles().get(0);
        assertBattleCalculations("spider", battle, true, "c_battleTests_basicMercenaryMercenaryDies");
    }

    @Test
    @DisplayName("Test basic battle calculations - zombie - player loses")
    public void testHealthBelowZeroZombie() {
        DungeonManiaController controller = new DungeonManiaController();
        // now player is at zombie
        DungeonResponse postBattleResponse = genericZombieSequence(controller, "c_battleTests_basicMercenaryPlayerDies", false);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("zombie", battle, false, "c_battleTests_basicMercenaryPlayerDies");
    }

    @Test
    @DisplayName("Test basic battle calculations - zombie - player wins")
    public void testRoundCalculationsZombie() {
        DungeonManiaController controller = new DungeonManiaController();
        // now player is at zombie
        BattleResponse battle = genericZombieSequence(controller, "c_battleTests_basicMercenaryMercenaryDies", false).getBattles().get(0);
        assertBattleCalculations("zombie", battle, true, "c_battleTests_basicMercenaryMercenaryDies");
    }

    @Test
    @DisplayName("Test basic battle calculations - mercenary - player loses")
    public void testHealthBelowZeroMercenary() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericSpiderSequence(controller, "c_battleTests_basicMercenaryPlayerDies", false);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("spider", battle, false, "c_battleTests_basicMercenaryPlayerDies");
    }


    @Test
    @DisplayName("Test basic battle calculations - mercenary - player wins")
    public void testRoundCalculationsMercenary() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_basicMercenaryMercenaryDies", false);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("mercenary", battle, true, "c_battleTests_basicMercenaryMercenaryDies");
    }

    @Test
    @DisplayName("Test battle with sword - mercenary - player wins")
    public void testAttackMercenaryWithSword() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_swordPlayerWins", true);
        assertEquals(0, countEntityOfType(postBattleResponse, "mercenary"));
    }

    @Test
    @DisplayName("Test battle with sword - mercenary - player wins - see if sword durability decreases")
    public void testAttackMercenarySwordDurability() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_swordPlayerWins", true);
        assertEquals(0, countEntityOfType(postBattleResponse, "mercenary"));
        assertEquals(0, getInventory(postBattleResponse, "sword").size());
    }    
    
    @Test
    @DisplayName("Test battle with three enemies")
    public void testAttackThreeEnemies() {
        /*
         *        wall   
         *  wall  player wall
         *        wall      
         */
        // player, two mercenaries and one zombie toast in the same tile
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_battleTest_battleThreeEnemies", "c_battleTests_basicMercenaryMercenaryDies");
        assertEquals(2, countEntityOfType(res, "mercenary"));
        assertEquals(1, countEntityOfType(res, "zombie_toast"));
        res = controller.tick(Direction.NONE);
        
        assertEquals(0, countEntityOfType(res, "mercenary"));
        assertEquals(0, countEntityOfType(res, "zombie_toast"));
    }

    @Test
    @DisplayName("Test sword gives extra attack damage")
    public void testSwordAttack() {
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
        
        // player battles zombie 
        res = dmc.tick(Direction.NONE);
        assertEquals(1, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.NONE);

        for (RoundResponse round : res.getBattles().get(0).getRounds()) {
            // without sword, enemy health should decrease by 0.2 but with boosted attack from sword
            // enemy health should decrease by 0.8
            assertEquals("sword", round.getWeaponryUsed().get(0).getType());
            assertNotEquals(-0.2, round.getDeltaEnemyHealth());
            assertEquals(-0.8, round.getDeltaEnemyHealth());
        }
    }
}

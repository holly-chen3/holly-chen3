package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.countEntityOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class MercenaryBriberyTests {
    private static DungeonResponse genericAllyMovementSequence(DungeonManiaController dmc) {
        DungeonResponse res = dmc.getDungeonResponseModel();
        List<Direction> directions = Arrays.asList(Direction.UP, Direction.RIGHT, 
            Direction.DOWN, Direction.LEFT);

        // Bribe mercenary
        String mercId = getEntities(res, "mercenary").get(0).getId();
        try {
            res = dmc.interact(mercId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
    
        // Move player in randomly generated directions and check that mercenary
        // always occupies the square the player was previously in
        Random rand = new Random();
        Position lastPosition = getPlayer(res).get().getPosition();
        Position lastMercPos = getEntities(res, "mercenary").get(0).getPosition();
        for (int i = 0; i < 10; i++) {
            res = dmc.tick(directions.get(rand.nextInt(4)));
            Position currentPosition = getPlayer(res).get().getPosition();
            Position currentMercPos = getEntities(res, "mercenary").get(0).getPosition();

            // If player does not move, check that merc doesn't move, else merc should be in player's last position
            if (lastPosition.equals(currentPosition)) {
                assertEquals(lastMercPos, currentMercPos);
            } else {
                assertEquals(lastPosition, currentMercPos);
            }

            lastPosition = currentPosition;
            lastMercPos = currentMercPos;
        }

        return res;
    }

    @Test
    @DisplayName("Test exception is thrown does not have enough gold to bribe")
    public void testNotEnoughGold() {
        /*
         *  exit   wall  wall  wall
         * player  [  ]  [  ]  merc  wall
         *  wall   wall  wall  wall
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_basicMercenary", "c_briberyTests_allyBattleBuff");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        
        DungeonResponse postRes = dmc.tick(Direction.DOWN);
        assertEquals(pos.translateBy(Direction.LEFT), 
            getEntities(postRes, "mercenary").get(0).getPosition());
        
        assertEquals(0, getInventory(res, "treasure").size());

        String mercId = getEntities(res, "mercenary").get(0).getId();
        assertThrows(InvalidActionException.class, () -> {
            dmc.interact(mercId);
        });
    }

    @Test
    @DisplayName("Test exception thrown when the player is out of range to bribe")
    public void testOutOfRange() {
        /*
         *  exit   wall  wall  wall
         * player  [  ]  [  ]  merc  wall
         *  wall   wall  wall  wall
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_briberyTest_playerHasGold", "c_briberyTests_allyBattleBuff");

        res = dmc.tick(Direction.NONE);

        assertEquals(1, getInventory(res, "treasure").size());

        String mercId = getEntities(res, "mercenary").get(0).getId();
        assertThrows(InvalidActionException.class, () -> {
            dmc.interact(mercId);
        });
    }

    @Test
    @DisplayName("Test the bribed mercenary does not enter same cell as player to battle player")
    public void testSuccessfulBribe() {
        /*
         *  exit   wall  wall  wall
         * player  [  ]  [  ]  merc  wall
         *  wall   wall  wall  wall
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_briberyTest_playerHasGold", "c_briberyTests_allyBattleBuff");
        getEntities(res, "mercenary").get(0).getPosition();

        res = dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        assertEquals(getPlayer(res).get().getPosition()
            .translateBy(Direction.RIGHT), 
            getEntities(res, "mercenary").get(0).getPosition());

        assertEquals(1, getInventory(res, "treasure").size());

        // Bribe mercenary
        String mercId = getEntities(res, "mercenary").get(0).getId();
        try {
            res = dmc.interact(mercId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        assertEquals(0, getInventory(res, "treasure").size());

        res = dmc.tick(Direction.DOWN);
        assertEquals(getPlayer(res).get().getPosition().translateBy(Direction.RIGHT), 
            getEntities(res, "mercenary").get(0).getPosition());
        assertEquals(1, countEntityOfType(res, "player"));
        assertEquals(1, countEntityOfType(res, "mercenary"));
    }

    @Test
    @DisplayName("Test the mercenary follows the player after being bribed")
    public void testAllyFollows() {
        /*
         *                    spawner
         *  wall               door
         * player  [  ]  [  ]  wall   merc
         *  wall              boulder   
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_briberyTest_followPlayer", "c_briberyTests_allyBattleBuff");

        // Move mercenary to be in bribing range of player
        for (int i = 0; i < 7; i++) {
            res = dmc.tick(Direction.DOWN);
        }        

        assertEquals(getPlayer(res).get().getPosition()
            .translateBy(Direction.RIGHT), 
            getEntities(res, "mercenary").get(0).getPosition());

        
        assertEquals(1, getInventory(res, "treasure").size());
        res = genericAllyMovementSequence(dmc);
        assertEquals(0, getInventory(res, "treasure").size());
    }

    @Test
    @DisplayName("Test bribe radius of two will enable mercenary to teleport to player's last position")
    public void testBribeRadius2() {
        /*
         *                    spawner
         *  wall               door
         * player  [  ]  [  ]  wall   merc
         *  wall              boulder   
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_briberyTest_followPlayer", "c_briberyTests_bribeRadius2");

        // Move mercenary to be in bribing range of player
        for (int i = 0; i < 6; i++) {
            res = dmc.tick(Direction.DOWN);
        }        

        assertEquals(getPlayer(res).get().getPosition()
            .translateBy(new Position(2, 0)), 
            getEntities(res, "mercenary").get(0).getPosition());

        assertEquals(1, getInventory(res, "treasure").size());
        res = genericAllyMovementSequence(dmc);
        assertEquals(0, getInventory(res, "treasure").size());
    }

        @Test
    @DisplayName("Test different bribe amounts")
    public void testBribeAmounts() {
        /*
         *                    spawner
         *  wall               door
         * player  [  ]  [  ]  wall   merc
         *  wall              boulder   
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_briberyTest_bribeAmounts", "c_briberyTests_bribeAmounts");

        // Move mercenary to be in bribing range of player
        for (int i = 0; i < 7; i++) {
            res = dmc.tick(Direction.DOWN);
        }        

        assertEquals(getPlayer(res).get().getPosition()
            .translateBy(Direction.RIGHT), 
            getEntities(res, "mercenary").get(0).getPosition());

        assertEquals(5, getInventory(res, "treasure").size());
        res = genericAllyMovementSequence(dmc);
        assertEquals(2, getInventory(res, "treasure").size());
    }

    @Test
    @DisplayName("Test bribed mercenary will follow player through portal")
    public void testTeleportFollowMovement() {
        /*
         *  wall                         
         * player  [  ]  portal  portal  merc
         *  wall                         exit
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_teleportMovement", "c_briberyTests_allyBattleBuff");

        // Move mercenary to be in bribing range of player
        res = dmc.tick(Direction.NONE);

        assertEquals(getPlayer(res).get().getPosition()
            .translateBy(Direction.RIGHT), 
            getEntities(res, "mercenary").get(0).getPosition());

        // Bribe mercenary
        String mercId = getEntities(res, "mercenary").get(0).getId();
        try {
            res = dmc.interact(mercId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        
        assertEquals(0, getInventory(res, "treasure").size());

        List<Direction> path = Arrays.asList(Direction.RIGHT, Direction.RIGHT, 
            Direction.UP, Direction.LEFT, Direction.LEFT, Direction.DOWN, 
            Direction.RIGHT);

        
        Position lastPosition = getPlayer(res).get().getPosition();
        for (int i = 0; i < path.size(); i++) {
            res = dmc.tick(path.get(i));
            assertEquals(lastPosition, getEntities(res, "mercenary").get(0).getPosition());
            lastPosition = getPlayer(res).get().getPosition();
        }
    }

    @Test
    @DisplayName("Test the mercenary contributes attack and defence buff in battle")
    public void testAllyBuff() {
        /*
         *  merc         exit
         * 
         * player       
         *     
         *  merc             
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_briberyTest_battleBuff", "c_briberyTests_allyBattleBuff");
        assertEquals(1, countEntityOfType(res, "player"));
        assertEquals(2, countEntityOfType(res, "mercenary"));

        // let player pick up treasure on their position and for mercenaries to move into bribery range
        res = dmc.tick(Direction.NONE);

        // Bribe a mercenary
        String mercId = getEntities(res, "mercenary").get(0).getId();
        try {
            res = dmc.interact(mercId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }

        // let unbribed mercenary move onto player's position to start a battle
        res = dmc.tick(Direction.NONE);
    
        // In a normal situtation with no allies, the config would enable player to die
        // However, with ally buffs, player should win the battle against the enemy mercenary
        assertEquals(1, countEntityOfType(res, "player"));
        assertEquals(1, countEntityOfType(res, "mercenary"));
        for (RoundResponse round : res.getBattles().get(0).getRounds()) {
            assertEquals(-0.0, round.getDeltaCharacterHealth());
            assertNotEquals(-0.2, round.getDeltaEnemyHealth());
            assertEquals(-0.8, round.getDeltaEnemyHealth());
        }
    }
}
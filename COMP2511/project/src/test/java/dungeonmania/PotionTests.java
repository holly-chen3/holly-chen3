package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.entityInPosition;
import static dungeonmania.TestUtils.countEntityOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PotionTests {
    public Position movementAway(DungeonResponse res, Position position) {
        Position player = getPlayer(res).get().getPosition();
        int positionX = position.getX();
        int positionY = position.getY();
        if (player.getX() >= position.getX()) {
            positionX = position.getX() - 1;
        } else if (player.getX() < position.getX()) {
            positionX = position.getX() + 1;
        } else if (player.getY() > position.getY()) {
            positionY = position.getY() - 1;
        } else if (player.getY() < position.getY()) {
            positionY = position.getY() + 1;
        }
        return new Position(positionX, positionY);
    }
    /*@Test
    @DisplayName("Test use item not in inventory will raise invalid exception")
    public void invalidExceptionItem() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_invincibilitySpider", "c_battleTests_basicPotionLastLong");
        res = dmc.tick(Direction.DOWN);
        String invincPotion = getEntities(res, "invincibility_potion").get(0).getId();
        assertThrows(InvalidActionException.class, () -> {
            dmc.tick(invincPotion);
        });
    }*/
    
    @Test
    @DisplayName("Test invincibility potion can be picked up and consumed, and immediately defeat enemy")
    public void basicInvincibilityPotion() {
        /*                      wall            wall
         *         wall        player        mercenary  wall
         *         wall  invincibility_potion   wall
         *                      wall
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_SurroundedWalls", "c_battleTests_basicPotionLastLong");
        assertEquals(3, getEntities(res, "invincibility_potion").size());
        assertEquals(1, getEntities(res, "mercenary").size());
        // player walks into invincibility potion
        res = dmc.tick(Direction.DOWN);
        assertEquals(3, getInventory(res, "invincibility_potion").size());
        assertEquals(0, getEntities(res, "invincibility_potion").size());
        String invincibilityPotion = getInventory(res, "invincibility_potion").get(0).getId();
        try {
            res = dmc.tick(invincibilityPotion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        assertTrue(getGoals(res).contains(":enemies"));
        dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test invisibility potion can be picked up and consumed, and mercenary cannot kill player")
    public void basicInvisibilityPotion() {
        /*                      wall            wall
         *         wall        player        mercenary  wall
         *         wall  invisibility_potion    wall
         *                      wall
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_SurroundedWalls", "c_battleTests_basicPotionLastLong");
        assertEquals(3, getEntities(res, "invisibility_potion").size());
        assertEquals(1, getEntities(res, "mercenary").size());
        assertEquals(1, getEntities(res, "player").size());
        // player walks into invisibility potion
        res = dmc.tick(Direction.DOWN);
        assertEquals(3, getInventory(res, "invisibility_potion").size());
        assertEquals(0, getEntities(res, "invisibility_potion").size());
        String invisibilityPotion = getInventory(res, "invisibility_potion").get(0).getId();
        try {
            res = dmc.tick(invisibilityPotion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }

        /*                      wall       wall
         *         wall       mercenary           wall
         *         wall        player      wall
         *                      wall
         */

        res = dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        assertEquals(1, getEntities(res, "mercenary").size());
        assertEquals(1, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test invincibility potion can be picked up and consumed, and mercenary can kill player when potion expires")
    public void basicInvincibilityPotionExpire() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_Invincibility", "c_battleTests_basicMercenaryPlayerDies");
        assertEquals(1, getEntities(res, "invincibility_potion").size());
        assertEquals(1, getEntities(res, "mercenary").size());
        // player walks into an invincibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(0, getEntities(res, "invincibility_potion").size());
        String potion = getInventory(res, "invincibility_potion").get(0).getId();
        try {
            res = dmc.tick(potion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        Position mercenary = getEntities(res, "mercenary").get(0).getPosition();
        res = dmc.tick(Direction.LEFT);
        Position position = movementAway(res, mercenary);
        // mercenary now moves away from player
        mercenary = getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(position, mercenary);
        assertTrue(entityInPosition(getEntities(res, "mercenary"), "mercenary", position));
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        assertEquals(0, countEntityOfType(res, "player"));
    }

    @Test
    @DisplayName("Test invisibility potion can be picked up and consumed, and spider can kill player when potion expires")
    public void basicInvisibilityPotionExpire() {
        /*                      wall            wall
         *         wall        player        mercenary  wall
         *         wall  invisibility_potion    wall
         *                      wall
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_SurroundedWalls", "c_battleTests_basicMercenaryPlayerDies");
        assertEquals(3, getEntities(res, "invisibility_potion").size());
        assertEquals(1, getEntities(res, "mercenary").size());
        assertEquals(1, getEntities(res, "player").size());
        // player walks into invisibility potion
        res = dmc.tick(Direction.DOWN);
        assertEquals(3, getInventory(res, "invisibility_potion").size());
        assertEquals(0, getEntities(res, "invisibility_potion").size());
        String invisibilityPotion = getInventory(res, "invisibility_potion").get(0).getId();
        try {
            res = dmc.tick(invisibilityPotion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }

        res = dmc.tick(Direction.NONE);
        assertEquals(1, countEntityOfType(res, "player"));
        dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        assertEquals(0, countEntityOfType(res, "player"));
    }
  
    @Test
    @DisplayName("Test invisibility potion can be picked up and consumed, and mercenary can kill player when potion expires")
    public void basicInvisibilityPotionExpireMercenary() {
        /*                      wall            wall
         *         wall        player        mercenary  wall
         *         wall  invisibility_potion    wall
         *                      wall
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_SurroundedWalls", "c_battleTests_basicMercenaryPlayerDies");
        assertEquals(3, getEntities(res, "invisibility_potion").size());
        assertEquals(1, getEntities(res, "mercenary").size());
        assertEquals(1, getEntities(res, "player").size());
        // player walks into invincibility potion
        res = dmc.tick(Direction.DOWN);
        assertEquals(3, getInventory(res, "invisibility_potion").size());
        assertEquals(0, getEntities(res, "invisibility_potion").size());
        String invisibilityPotion = getInventory(res, "invisibility_potion").get(0).getId();
        try {
            res = dmc.tick(invisibilityPotion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }

        dmc.tick(Direction.NONE);
        assertEquals(1, countEntityOfType(res, "player"));
        dmc.tick(Direction.NONE);
        dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        assertEquals(0, countEntityOfType(res, "player"));
    }

    @Test
    @DisplayName("Test invincibility potion can be picked up and consumed, potion expires, but another one is in queue.")
    public void invincibilityQueue() {
        /*                      wall            wall
         *         wall        player        mercenary  wall
         *         wall  invincibility_potion    wall
         *                      wall
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_SurroundedWalls", "c_battleTests_basicMercenaryPlayerDies");
        assertEquals(3, getEntities(res, "invincibility_potion").size());
        assertEquals(1, getEntities(res, "mercenary").size());
        // player walks into three invincibility potions
        res = dmc.tick(Direction.DOWN);
        assertEquals(3, getInventory(res, "invincibility_potion").size());
        assertEquals(0, getEntities(res, "invincibility_potion").size());
        String potionOne = getInventory(res, "invincibility_potion").get(0).getId();
        String potionTwo = getInventory(res, "invincibility_potion").get(1).getId();
        String potionThree = getInventory(res, "invincibility_potion").get(2).getId();
        try {
            res = dmc.tick(potionOne);
            res = dmc.tick(potionTwo);
            res = dmc.tick(potionThree);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        assertTrue(getGoals(res).contains(":enemies"));
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test three invisibility potions can be picked up and consumed, and mercenary cannot kill player")
    public void invisibilityQueue() {
        /*                      wall            wall
         *         wall        player        mercenary  wall
         *         wall  invisibility_potion    wall
         *                      wall
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_SurroundedWalls", "c_battleTests_basicMercenaryPlayerDies");
        assertEquals(3, getEntities(res, "invisibility_potion").size());
        assertEquals(1, getEntities(res, "mercenary").size());
        // player walks into three invisibility potions
        res = dmc.tick(Direction.DOWN);
        assertEquals(3, getInventory(res, "invisibility_potion").size());
        assertEquals(0, getEntities(res, "invisibility_potion").size());
        String potionOne = getInventory(res, "invisibility_potion").get(0).getId();
        String potionTwo = getInventory(res, "invisibility_potion").get(1).getId();
        String potionThree = getInventory(res, "invisibility_potion").get(2).getId();
        try {
            res = dmc.tick(potionOne);
            res = dmc.tick(potionTwo);
            res = dmc.tick(potionThree);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        assertTrue(getGoals(res).contains(":enemies"));
        dmc.tick(Direction.NONE);
        dmc.tick(Direction.NONE);
        res = dmc.tick(Direction.NONE);
        // last tick when invisibility potion is still working
        assertTrue(getGoals(res).contains(":enemies"));
        assertEquals(1, getEntities(res, "mercenary").size());
        assertEquals(1, getEntities(res, "player").size());
        res = dmc.tick(Direction.NONE);
        assertTrue(getGoals(res).contains(":enemies"));
        assertEquals(1, getEntities(res, "mercenary").size());
        assertEquals(0, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test when invincibility potion is consumed, mercenary moves away")
    public void invincibilityMovementMercenary() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_Invincibility", "c_battleTests_basicMercenaryPlayerDies");
        assertEquals(1, getEntities(res, "invincibility_potion").size());
        assertEquals(1, getEntities(res, "mercenary").size());
        // player walks into an invincibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(0, getEntities(res, "invincibility_potion").size());
        String potion = getInventory(res, "invincibility_potion").get(0).getId();
        try {
            res = dmc.tick(potion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        Position mercenary = getEntities(res, "mercenary").get(0).getPosition();
        res = dmc.tick(Direction.LEFT);
        Position position = movementAway(res, mercenary);
        // mercenary now moves away from player
        mercenary = getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(position, mercenary);
        assertTrue(entityInPosition(getEntities(res, "mercenary"), "mercenary", position));
    }

    @Test
    @DisplayName("Test when invincibility potion is consumed, zombie toast moves away")
    public void invincibilityMovementZombieToast() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_InvincibilityZombieToast", "c_battleTests_basicMercenaryPlayerDies");
        assertEquals(1, getEntities(res, "invincibility_potion").size());
        assertEquals(1, getEntities(res, "zombie_toast").size());
        // player walks into an invincibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(0, getEntities(res, "invincibility_potion").size());
        String potion = getInventory(res, "invincibility_potion").get(0).getId();
        try {
            res = dmc.tick(potion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        // original position of zombie before it moves away
        Position zombieToast = getEntities(res, "zombie_toast").get(0).getPosition();
        res = dmc.tick(Direction.LEFT);
        Position position = movementAway(res, zombieToast);
        // zombie now moves away from player
        assertTrue(entityInPosition(getEntities(res, "zombie_toast"), "zombie_toast", position));
    }
    @Test
    @DisplayName("Test when invisibility potion is consumed, mercenary moves like zombie")
    public void invisibilityMovementMercenary() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_SurroundedWalls", "c_battleTests_basicPotionLastLong");
        assertEquals(3, getEntities(res, "invisibility_potion").size());
        assertEquals(1, getEntities(res, "mercenary").size());
        // player walks into an invisibility potion
        res = dmc.tick(Direction.DOWN);
        assertEquals(3, getInventory(res, "invisibility_potion").size());
        assertEquals(0, getEntities(res, "invisibility_potion").size());
        String potion = getInventory(res, "invisibility_potion").get(0).getId();
        try {
            res = dmc.tick(potion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        Position position = getEntities(res, "mercenary").get(0).getPosition();
        res = dmc.tick(Direction.NONE);
        // mercenary now moves randomly not stationary
        assertFalse(entityInPosition(getEntities(res, "mercenary"), "mercenary", position));
        Position secondPosition = getEntities(res, "mercenary").get(0).getPosition();
        res = dmc.tick(Direction.NONE);
        assertFalse(entityInPosition(getEntities(res, "mercenary"), "mercenary", secondPosition));
    }

    
    @Test
    @DisplayName("Test when invisibility potion is consumed, assassin moves like zombie")
    public void invisibilityMovementAssassin() {
        /*                      wall            wall
         *         wall        player        mercenary  wall
         *         wall  invisibility_potion    wall
         *                      wall
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest_assassinKills", "c_battleTests_assassinKills");
        assertEquals(3, getEntities(res, "invisibility_potion").size());
        assertEquals(1, getEntities(res, "assassin").size());

        // player walks into an invisibility potion
        res = dmc.tick(Direction.DOWN);
        Position lastPosition = getPlayer(res).get().getPosition();

        assertEquals(3, getInventory(res, "invisibility_potion").size());
        assertEquals(0, getEntities(res, "invisibility_potion").size());
        String potion = getInventory(res, "invisibility_potion").get(0).getId();
        try {
            res = dmc.tick(potion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }

        // assassin still kills player since they are in recon radius
        Position position = getEntities(res, "assassin").get(0).getPosition();
        assertEquals(lastPosition, position);
        assertEquals(1, countEntityOfType(res, "assassin"));
        assertEquals(0, countEntityOfType(res, "player"));
    }
}

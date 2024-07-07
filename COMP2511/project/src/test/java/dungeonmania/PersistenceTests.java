package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getGoals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PersistenceTests {
    @Test
    @DisplayName("Test save game is saving the game")
    public void testSaveGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        int savedGames = dmc.allGames().size();
        dmc.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryPlayerDies");
        String dungeonName = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> dmc.saveGame(dungeonName));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(savedGames + 1, dmc.allGames().size());
        assertTrue(dmc.allGames().contains(dungeonName));
    }

    @Test
    @DisplayName("Test load game throws exception if id is not a valid game name")
    public void testLoadGameInvalidName() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.loadGame("incorrectid"));
    }
}

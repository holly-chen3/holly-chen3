package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dungeonmania.TestUtils.countEntityOfType;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class PlayerDeathTests {
    @Test
    @DisplayName("Test player starts with 1 health and is next to zombie")
    public void testPlayerDeath() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_playerDeathTest", "c_playerDeathTests");

        res = dmc.tick(Direction.DOWN);
        Position posPlayer = getEntities(res, "player").get(0).getPosition();
        Position posZomb = getEntities(res, "zombie_toast").get(0).getPosition();
        assertNotEquals(posZomb, posPlayer);

        res = dmc.tick(Direction.DOWN);
        
        assertEquals(0, countEntityOfType(res, "player"));
    }
} 

package random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameTest {
    // test that hero has equally likely chance to win or lose
    @Test
    public void basicBattle() {
        Game game = new Game(4);
        int win = 0;
        int lose = 0;
        for (int i = 0; i < 8; i++) {
            if (game.battle()) {
                win++;
            } else {
                lose++;
            }
        }
        assertEquals(win, 4);
        assertEquals(lose, 4);
        
    }

    @Test
    public void basicBattleTwo() {
        Game game = new Game(0);
        int win = 0;
        int lose = 0;
        for (int i = 0; i < 8; i++) {
            if (game.battle()) {
                win++;
            } else {
                lose++;
            }
        }
        assertEquals(win, 4);
        assertEquals(lose, 4);
    }
}
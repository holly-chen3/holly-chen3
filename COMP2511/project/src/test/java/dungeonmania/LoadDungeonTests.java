package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class LoadDungeonTests {
/* 
    static Stream<Arguments> stringArrayProvider() {
        return Stream.of(
                Arguments.of((Object) new String[]{"d_DoorsKeysTest_useKeyWalkThroughOpenDoor", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor", "player", "key", "door", "exit"}),
                Arguments.of((Object) new String[]{"d_spiderTest_basicMovement", "c_spiderTest_basicMovement", "player", "wall", "spider", "exit"})
        );
    }

    @ParameterizedTest
    @MethodSource("oneStr")
    @DisplayName("Test load dungeon loads the correct entities")
    public void testLoadDungeonExpectedNumberOne(String[] argument) { 
        DungeonManiaController dmc = new DungeonManiaController();
        
        DungeonResponse res = dmc.newGame(argument[0], argument[1]);

        // expected number for all the entities in the dungeon
        
        assertEquals(1, countEntityOfType(res, argument[2]));
        assertEquals(1, countEntityOfType(res, argument[3]));
        assertEquals(1, countEntityOfType(res, argument[4]));
        assertEquals(1, countEntityOfType(res, argument[5]));
    }
*/
    @Test
    @DisplayName("Test load dungeon loads the correct entities when there are more than one of that entity")
    public void testLoadDungeonExpectedNumberMany() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_battleTests_basicMercenaryMercenaryDies");

        // expected number for all the entities in the dungeon
        
        assertEquals(1, countEntityOfType(res, "player"));
        assertEquals(1, countEntityOfType(res, "bomb"));
        assertEquals(1, countEntityOfType(res, "switch"));
        assertEquals(1, countEntityOfType(res, "boulder"));
        assertEquals(1, countEntityOfType(res, "exit"));
        assertEquals(2, countEntityOfType(res, "wall"));
        assertEquals(2, countEntityOfType(res, "treasure"));
    }

    @Test
    @DisplayName("Test load dungeon loads the mercenary")
    public void testLoadDungeonMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryMercenaryDies");

        // expected number for all the entities in the dungeon
        
        assertEquals(1, countEntityOfType(res, "player"));
        assertEquals(1, countEntityOfType(res, "mercenary"));
        assertEquals(1, countEntityOfType(res, "exit"));
        assertEquals(8, countEntityOfType(res, "wall"));
    }

    @Test
    @DisplayName("Test load dungeon d_spiderTest_basicMovement.json")
    public void testLoadDungeonSpider() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        List<EntityResponse> expected = new ArrayList<>();
        expected.add(new EntityResponse(res.getEntities().get(0).getId(), "player", new Position(1, 1), false));
        expected.add(new EntityResponse(res.getEntities().get(1).getId(), "wall", new Position(1,0), false));
        expected.add(new EntityResponse(res.getEntities().get(2).getId(), "spider", new Position(5, 5), false));
        expected.add(new EntityResponse(res.getEntities().get(3).getId(), "exit", new Position(5,1), false));
        assertEquals(res.getEntities(), expected);
    }

    @Test 
    @DisplayName("Test dungeon basic goals are correctly loaded")
    public void testLoadDungeonCorrectBasicGoals() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");
        assertEquals(":exit", getGoals(res));
    }

    @Test
    @DisplayName("Test dungeon complex goals are correctly loaded")
    public void testLoadDungeonCorrectComplexGoals() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_andAll", "c_complexGoalsTest_andAll");
        assertEquals("(:exit AND :treasure) AND (:boulders AND :enemies)", getGoals(res));
    }
 
}

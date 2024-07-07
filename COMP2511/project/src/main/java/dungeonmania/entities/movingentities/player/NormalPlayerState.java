package dungeonmania.entities.movingentities.player;

import dungeonmania.battle.Battle;

public class NormalPlayerState implements PlayerState{

    public NormalPlayerState() {
    }

    @Override
    public void updateState() {
        
    }

    @Override
    public void battle(Battle battle) {
        // for any enemies in the same tile, 
        // create a battle instance for them. 
        battle.normalBattle();
    }

    @Override
    public int getTicksLeft() {
        return 0;
    }

    @Override
    public void setTicksLeft() {
        // Do nothing
    }  
}

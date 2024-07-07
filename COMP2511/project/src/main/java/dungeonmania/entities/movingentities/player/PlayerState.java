package dungeonmania.entities.movingentities.player;

import dungeonmania.battle.Battle;

public interface PlayerState {
    public void updateState();
    public void battle(Battle battle);
    public int getTicksLeft();
    public void setTicksLeft();
}

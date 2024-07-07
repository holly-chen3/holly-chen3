package dungeonmania.goals;

import dungeonmania.Dungeon;

public class EnemyGoal extends LeafGoal{
    private int enemyGoal;

    public EnemyGoal(int enemyGoal) {
        super("enemies");
        this.enemyGoal = enemyGoal;
    }
    // at least x amount of enemies must be killed in order to win
    @Override
    public int count(Dungeon dungeon) {
        return dungeon.getDefeatedEnemies().size();
    }

    @Override
    public boolean isCompleted(Dungeon dungeon) {
        return count(dungeon) >= enemyGoal;
    }
    
}

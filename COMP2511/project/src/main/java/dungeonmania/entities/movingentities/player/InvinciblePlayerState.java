package dungeonmania.entities.movingentities.player;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.battle.Battle;
import dungeonmania.entities.items.collectables.potions.Potion;
import dungeonmania.entities.items.collectables.potions.Queue;

public class InvinciblePlayerState implements PlayerState{
    private Dungeon dungeon;
    private Player player;
    private int ticksLeft;
    private Queue queue;
    private Config config;
    private int duration;

    public InvinciblePlayerState(Dungeon dungeon, Queue queue) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();
        this.queue = queue;
        this.config = dungeon.getConfig();
        this.duration = config.getConfigMap().get("invincibility_potion_duration");
        dungeon.runAway();
        setTicksLeft();
    }

    @Override
    public void updateState() {
        if (ticksLeft <= 0) {
            player.changeState(new NormalPlayerState());
            dungeon.vincibleEnemy();
            queue.removeFromQueue();
            // consume next potion in queue
            if (!queue.getPotions().isEmpty()) {
                Potion potion = queue.getNextPotion();
                potion.consumePotion(player, queue, config, dungeon);
            }
        }
        ticksLeft--;
    }

    @Override
    public void battle(Battle battle) {
        // any battles that occur end 
        // immediately after first round
        // with Player winning
        battle.invincibleBattle(queue.getNextPotion());
    }

    @Override
    public int getTicksLeft() {
        return ticksLeft;
    }

    @Override
    public void setTicksLeft() {
        this.ticksLeft = duration;
    }
}

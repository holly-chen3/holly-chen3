package dungeonmania.entities.movingentities.player;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.battle.Battle;
import dungeonmania.entities.items.collectables.potions.Potion;
import dungeonmania.entities.items.collectables.potions.Queue;

public class InvisiblePlayerState implements PlayerState{
    private Dungeon dungeon;
    private Player player;
    private int ticksLeft;
    private Queue queue;
    private Config config;
    private int duration;

    public InvisiblePlayerState(Dungeon dungeon, Queue queue) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();
        this.queue = queue;
        this.config = dungeon.getConfig();
        this.duration = config.getConfigMap().get("invisibility_potion_duration");
        dungeon.invisibleMercenary();
        setTicksLeft();
    }

    @Override
    public void updateState() {
        dungeon.invisibleMercenary();

        if (ticksLeft <= 0) {
            player.changeState(new NormalPlayerState());
            dungeon.visibleMercenary();
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
        // When invisible, only assassins can battle player
        if (battle.getEnemy().getType().equals("assassin")) {
            battle.normalBattle();
        }
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

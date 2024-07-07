package dungeonmania.battle;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.movingentities.player.Player;
import dungeonmania.entities.items.ItemEntity;
import dungeonmania.entities.items.collectables.potions.Potion;
import dungeonmania.entities.movingentities.enemies.Enemy;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.RoundResponse;

public class Battle {
    private Enemy enemy;
    private Player player;
    private double initialPlayerHealth;
    private double initialEnemyHealth;
    private List<Round> rounds = new ArrayList<>();

    public Battle(Enemy enemy, Player player) {
        this.enemy = enemy;
        this.player = player;
        this.initialPlayerHealth = player.getHealth();
        this.initialEnemyHealth = enemy.getHealth();
    }

    public double getInitialPlayerHealth() {
        return initialPlayerHealth;
    }

    public double getInitialEnemyHealth() {
        return initialEnemyHealth;
    }

    public BattleResponse getBattleResponse() {
        return new BattleResponse(enemy.getType(), getRounds(), 
            initialPlayerHealth, initialEnemyHealth);
    }

    public Enemy getEnemy() {
        return enemy;
    }
 
    public List<RoundResponse> getRounds() {
        List<RoundResponse> roundResponses = new ArrayList<>();
        for (Round round : rounds) {
            roundResponses.add(round.getRoundResponse());
        }
        return roundResponses;
    }
    
    public void addRounds(Round round) {
        rounds.add(round);
    }

    public void normalBattle() {
        List<ItemEntity> weaponryUsed = player.getWeaponryUsed();
        double deltaPlayerHealth = -(player.defenceAgainstEnemy(enemy) / 10.00);
        double deltaEnemyHealth = -(player.getTotalAttack(enemy) / 5.00);
        while (player.getHealth() > 0 && enemy.getHealth() > 0 && enemy.isHostile()) {
            Round round = new Round(deltaPlayerHealth, deltaEnemyHealth, weaponryUsed);
            addRounds(round);
            player.setHealth(player.getHealth() + deltaPlayerHealth);
            enemy.setHealth(enemy.getHealth() + deltaEnemyHealth);
        }
    }

    public void invincibleBattle(Potion potion) {
        double deltaPlayerHealth = 0;
        double deltaEnemyHealth = -enemy.getHealth();
        List<ItemEntity> weaponryUsed = new ArrayList<>();
        weaponryUsed.add(potion);
        Round round = new Round(deltaPlayerHealth, deltaEnemyHealth, weaponryUsed);
        addRounds(round);
        enemy.setHealth(0);
    }
}

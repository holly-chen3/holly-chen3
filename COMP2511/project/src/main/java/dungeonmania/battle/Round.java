package dungeonmania.battle;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.items.ItemEntity;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;

public class Round {
    private double deltaPlayerHealth;
    private double deltaEnemyHealth;
    private List<ItemEntity> weaponryUsed = new ArrayList<>();
    
    public Round(double deltaPlayerHealth, double deltaEnemyHealth, List<ItemEntity> weapons) {
        this.deltaPlayerHealth = deltaPlayerHealth;
        this.deltaEnemyHealth = deltaEnemyHealth;
        this.weaponryUsed = weapons;
    }

    public double getDeltaPlayerHealth() {
        return deltaPlayerHealth;
    }
    
    public double getDeltaEnemyHealth() {
        return deltaEnemyHealth;
    }

    public List<ItemResponse> getWeaponryUsed() {
        List<ItemResponse> weaponry = new ArrayList<>();
        for (ItemEntity weapon: weaponryUsed) {
            weaponry.add(weapon.getItemResponse());
        }
        return weaponry;
    }

    public RoundResponse getRoundResponse() {
        return new RoundResponse(deltaPlayerHealth, deltaEnemyHealth, 
            getWeaponryUsed());
    }

    public void setDeltaPlayerHealth(double deltaPlayerHealth) {
        this.deltaPlayerHealth = deltaPlayerHealth;
    }

    public void setDeltaEnemyHealth(double deltaEnemyHealth) {
        this.deltaEnemyHealth = deltaEnemyHealth;
    }

    public void addWeaponryUsed(ItemEntity weaponUsed) {
        weaponryUsed.add(weaponUsed);
    }
}

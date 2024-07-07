package dungeonmania;

import java.util.Map;

public class Config {
    private Map<String, Integer> configMap;

    public Config(Map<String, Integer> configMap) {
        this.configMap = configMap;
    }

    public Map<String, Integer> getConfigMap() {
        return configMap;
    }
    /* 
    public void setPlayerHealthAttack (Player player) {
        player.setHealth(configMap.get("player_health"));
        player.setAttack(configMap.get("player_attack"));
    } */
     
    /**
     * Sets the zombie's health and attack
     * @param zToast
     */
    /*public void setZombieHealthAttack (ZombieToast zToast) {
        zToast.setHealth(configMap.get("zombie_health"));
        zToast.setAttack(configMap.get("zombie_attack"));
    } */

    public int getZombieSpawnRate () {
        return configMap.get("zombie_spawn_rate");
    }

    public int getSpiderSpawnRate() {
        return configMap.get("spider_spawn_rate");
    }
}

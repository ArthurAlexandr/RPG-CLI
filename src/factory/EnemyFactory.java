package factory;

import model.Enemy;
import model.Character;
import model.Race;
import java.util.Random;

public class EnemyFactory {
    private static final Random random = new Random();

    private static final String[] MELEE_NAMES = {"Esqueleto Guerreiro", "Saqueador Orc", "Bandido do Machado", "Guerreiro Morto-Vivo"};
    private static final String[] MAGE_NAMES = {"Cultista Sombrio", "Necromante", "Feiticeiro Renegado", "Mago de Sangue"};
    private static final String[] RANGED_NAMES = {"Arqueiro Goblin", "Atirador de Elite Esqueleto", "Salteador da Floresta"};
    private static final String[] TANK_NAMES = {"Golem de Pedra", "Zumbi Robusto", "Abominação de Carne", "Gárgula de Ferro"};

    public static Enemy createScaledEnemy(Character player) {
        int enemyCategory = random.nextInt(4); // 0: Melee, 1: Mage, 2: Ranged, 3: Tank
        int playerLevel = player.getLevel();
        
        // Atributos base do jogador para escalonamento
        int playerMaxHp = player.getStats().getMaxHealth();
        int playerDefense = player.getStats().getDefense();
        int playerSpeed = player.getStats().getSpeed();
        int playerOffense = Math.max(player.getStats().getStrength(), player.getStats().getPower());
        int playerMaxMana = player.getStats().getMaxMana();

        String name;
        String enemyType;
        Race race;
        int maxHealth;
        int power = 0;
        int strength = 0;
        int defense;
        int speed;
        int maxMana = 0;

        // Variação estatística aleatória individual (±15%)
        double variance = 0.85 + (random.nextDouble() * 0.30);

        switch (enemyCategory) {
            case 0: // MELEE / GUERREIRO
                name = MELEE_NAMES[random.nextInt(MELEE_NAMES.length)];
                enemyType = "Guerreiro";
                race = Race.ORC;
                maxHealth = (int) (playerMaxHp * 0.95 * variance);
                strength = (int) (playerOffense * 0.9 * variance);
                defense = (int) (playerDefense * 0.9 * variance);
                speed = (int) (playerSpeed * 0.85 * variance);
                break;

            case 1: // MAGO
                name = MAGE_NAMES[random.nextInt(MAGE_NAMES.length)];
                enemyType = "Mago";
                race = Race.ELF;
                maxHealth = (int) (playerMaxHp * 0.75 * variance);
                power = (int) (playerOffense * 1.1 * variance);
                defense = (int) (playerDefense * 0.6 * variance);
                speed = (int) (playerSpeed * 1.0 * variance);
                maxMana = (int) ((playerMaxMana > 0 ? playerMaxMana : 50) * 1.0 * variance);
                break;

            case 2: // RANGED / ARQUEIRO
                name = RANGED_NAMES[random.nextInt(RANGED_NAMES.length)];
                enemyType = "Arqueiro";
                race = Race.HUMAN;
                maxHealth = (int) (playerMaxHp * 0.85 * variance);
                strength = (int) (playerOffense * 0.85 * variance);
                defense = (int) (playerDefense * 0.7 * variance);
                speed = (int) (playerSpeed * 1.2 * variance);
                break;

            case 3: // TANK
            default:
                name = TANK_NAMES[random.nextInt(TANK_NAMES.length)];
                enemyType = "Tanque";
                race = Race.DWARF;
                maxHealth = (int) (playerMaxHp * 1.35 * variance);
                strength = (int) (playerOffense * 0.65 * variance);
                defense = (int) (playerDefense * 1.3 * variance);
                speed = (int) (playerSpeed * 0.6 * variance);
                break;
        }

        // Garante valores mínimos para evitar inimigos inúteis ou quebrados
        maxHealth = Math.max(20, maxHealth);
        strength = Math.max(3, strength);
        power = Math.max(0, power);
        defense = Math.max(1, defense);
        speed = Math.max(3, speed);
        maxMana = Math.max(0, maxMana);

        // Remove os modificadores de raça que o construtor base do Character aplica
        // para manter as estatísticas escaladas perfeitamente.
        // O construtor faz: finalHealth = maxHealth + race.getHealthModifier()
        // Então subtraímos aqui para que ao somar no construtor fique o valor escalado correto!
        int adjustedMaxHealth = maxHealth - race.getHealthModifier();
        int adjustedStrength = strength - race.getStrengthModifier();
        int adjustedDefense = defense - race.getDefenseModifier();
        int adjustedSpeed = speed - race.getSpeedModifier();
        int adjustedMaxMana = maxMana - race.getManaModifier();

        return new Enemy(
            name, 
            enemyType, 
            race, 
            playerLevel, 
            Math.max(1, adjustedMaxHealth), 
            power, 
            Math.max(1, adjustedStrength), 
            Math.max(1, adjustedDefense), 
            Math.max(1, adjustedSpeed), 
            Math.max(0, adjustedMaxMana)
        );
    }
}

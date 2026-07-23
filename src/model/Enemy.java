package model;

import java.util.Random;

public class Enemy extends Character {
    private final Random random = new Random();
    private String enemyType;

    public Enemy(String name, String enemyType, Race race, int level, int maxHealth, int power, int strength, int defense, int speed, int maxMana) {
        super(name, race, level, maxHealth, power, strength, defense, speed, maxMana);
        this.enemyType = enemyType;
    }

    public String getEnemyType() {
        return enemyType;
    }

    @Override
    protected String getPrimaryStat() {
        if (stats.getPower() > stats.getStrength()) {
            return "Poder: " + stats.getPower();
        }
        return "Força: " + stats.getStrength();
    }

    @Override
    public void weakAttack(Character target) {
        int chance = random.nextInt(100);
        if (chance < 85) {
            int statToUse = Math.max(stats.getStrength(), stats.getPower());
            int attackDamage = (int) (statToUse * 1.1);
            int finalDamage = Math.max(statToUse / 2, attackDamage - target.getStats().getDefense());
            System.out.println(this.name + " (" + enemyType + ") realizou um ataque rápido em " + target.getName() + " causando " + finalDamage + " de dano!");
            target.receiveDamage(finalDamage);
        } else {
            System.out.println(this.name + " (" + enemyType + ") errou o ataque em " + target.getName() + "!");
        }
    }

    @Override
    public void strongAttack(Character target) {
        int chance = random.nextInt(100);
        if (chance < 60) {
            int statToUse = Math.max(stats.getStrength(), stats.getPower());
            int attackDamage = (int) (statToUse * 1.8);
            int finalDamage = Math.max(statToUse / 2, attackDamage - target.getStats().getDefense());
            System.out.println(this.name + " (" + enemyType + ") desferiu um golpe devastador em " + target.getName() + " causando " + finalDamage + " de dano!");
            target.receiveDamage(finalDamage);
        } else {
            System.out.println(this.name + " (" + enemyType + ") tentou um golpe pesado, mas " + target.getName() + " se esquivou!");
        }
    }
}

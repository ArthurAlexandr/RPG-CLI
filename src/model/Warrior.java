package model;

import java.util.Random;

public class Warrior extends Character{

    private final Random random = new Random();

    public Warrior(String name) {
        // Classe construtora de Guerreiro, ja comeca nvl 3
        super(name, 3, 120, 0, 15, 12, 8, 0);
    }

    @Override
    public void weakAttack(Character target) {
        int chance = random.nextInt(100);

        // Chance de 90% de acertar o ataque
        if (chance < 90) {
            int attackDamage = (int) (stats.getStrength() * 1.5);

            // Calcula o dano que vai dar dependendo da defesa do oponente
            int finalDamage = Math.max(stats.getStrength() / 2, attackDamage - target.stats.getDefense());

            System.out.println(this.name + " landed a quick blow on " + target.name + " that dealt " + finalDamage + "damage!");
            target.receiveDamage(finalDamage);
        }
        else {
            System.out.println(this.name + " swung for a quick attack, but " + target.name + " dodged it!");
        }
    }

    @Override
    public void strongAttack(Character target) {
        int chance = random.nextInt(100);

        // Chance de 60% de acertar o ataque
        if (chance < 60) {
            int attackDamage = (int) (stats.getStrength() * 2.5);

            // Calcula o dano que vai dar dependendo da defesa do oponente
            int finalDamage = Math.max(attackDamage / 2, attackDamage - target.stats.getDefense());

            System.out.println(this.name + " unleashed a devastating strike on " + target.name + " dealing " + finalDamage + " damage!");
            target.receiveDamage(finalDamage);
        }
        else {
            System.out.println(this.name + " attempted a heavy strike, but lost their balance and missed!");
        }
    }
}

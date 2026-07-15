package model;

import java.util.Random;

public class Archer extends Character{

    private final Random random = new Random();

    public Archer(String name, Race race) {
        super(name, race, 3, 100, 0, 20, 5, 12, 0);
    }

    @Override
    public void weakAttack(Character target) {
        int chance = random.nextInt(100);

        // Arqueiros são muito precisos: 95% de chance de acertar o ataque básico
        if (chance < 95) {
            int attackDamage = (int) (stats.getStrength() * 1.4);

            // Calcula o dano mitigado pela defesa do oponente
            int finalDamage = Math.max(stats.getStrength() / 2, attackDamage - target.stats.getDefense());

            System.out.println(this.name + " fired a quick shot at " + target.name + " that dealt " + finalDamage + " damage!");
            target.receiveDamage(finalDamage);
        } else {
            System.out.println(this.name + " shot a quick arrow, but it whistled past " + target.name + "!");
        }
    }

    @Override
    public void strongAttack(Character target) {
        int chance = random.nextInt(100);

        // Um tiro carregado e perigoso: 70% de chance de acerto (maior que os 60% do guerreiro)
        if (chance < 70) {
            int attackDamage = (int) (stats.getStrength() * 2.3);

            // Flechas perfurantes ignoram um pedaço da armadura (target defense / 1.5)
            int effectiveDefense = (int) (target.stats.getDefense() / 1.5);
            int finalDamage = Math.max(attackDamage / 2, attackDamage - effectiveDefense);

            System.out.println(this.name + " unleashed a piercing shot through " + target.name + "'s armor, dealing " + finalDamage + " damage!");
            target.receiveDamage(finalDamage);
        } else {
            System.out.println(this.name + " carefully aimed a piercing shot, but the target moved at the last second!");
        }
    }
}

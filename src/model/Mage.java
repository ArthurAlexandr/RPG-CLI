package model;

import java.util.Random;

public class Mage extends Character{

    private final Random random = new Random();

    public Mage(String name) {
        // Classe construtora de Guerreiro, ja comeca nvl 3
        super(name, 3, 90, 22, 5, 8, 10, 60);

    }

    @Override
    public void weakAttack(Character target) {
        int chance = random.nextInt(100);

        // Chance de 90% de acertar o ataque
        if (chance < 90) {
            int attackDamage = (int) (stats.getPower() * 1.3);

            // Calcula o dano que vai dar dependendo da defesa do oponente
            int finalDamage = Math.max(stats.getPower() / 2, attackDamage - (target.stats.getDefense() / 2));

            System.out.println(this.name + " cast a Magic Bolt on " + target.name + " dealing " + finalDamage + " magic damage!");
            target.receiveDamage(finalDamage);
        }
        else {
            System.out.println(this.name + " channeled a Magic Bolt, but " + target.name + " resisted the spell!");
        }
    }
    @Override
    public void strongAttack(Character target) {
        int manaCost = 15;

        if (stats.getMana() < manaCost){
            System.out.println(this.name + " tried to cast Fireball, but does not have enough Mana! (" + stats.getMana() + "/" + manaCost + " MP)");
            return;
        }

        int chance = random.nextInt(100);

        // Chance de 60% de acertar o ataque
        if (chance < 60) {
            stats.setMana(stats.getMana() - manaCost);

            int attackDamage = (int) (stats.getPower() * 2.8);

            // Calcula o dano que vai dar dependendo da defesa do oponente
            int finalDamage = Math.max(attackDamage / 2, attackDamage - (target.stats.getDefense() / 3));

            System.out.println(this.name + " unleashed a devastating Fireball on " + target.name + " dealing " + finalDamage + " fire damage! (-" + manaCost + " MP)");
            target.receiveDamage(finalDamage);
        } else {
            // Even if the spell misses, the mana is still consumed because the spell was cast
            stats.setMana(stats.getMana() - manaCost);
            System.out.println(this.name + " unleashed a massive Fireball, but " + target.name + " managed to dive out of the explosion! (-" + manaCost + " MP)");
        }
    }
}
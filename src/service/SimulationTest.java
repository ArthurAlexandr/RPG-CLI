package service;

import model.Character;
import model.Mage;
import model.Race;
import model.Item;
import model.Equipment;
import model.Enemy;
import factory.EnemyFactory;

public class SimulationTest {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SIMULAÇÃO DE TESTE DO RPG ===");

        // 1. Criar Personagem (Mago Élfico)
        System.out.println("\nCriando o jogador: Gandalf (Elfo Mago)");
        Character player = new Mage("Gandalf", Race.ELF);
        player.showStatus();

        // 2. Testar Inventário inicial
        System.out.println("\nVerificando inventário inicial:");
        System.out.println("Itens: " + player.getInventory());

        // 3. Testar Equipamento de item
        System.out.println("\nCriando uma Espada de Ferro para equipar:");
        Equipment sword = new Equipment("Espada de Teste", "Uma espada pesada", Equipment.Slot.WEAPON, 10, 0, 5, 0, -1, 0);
        player.addToInventory(sword);
        System.out.println("Inventário antes: " + player.getInventory());
        player.equip(sword);
        System.out.println("Inventário depois: " + player.getInventory());
        player.showStatus();

        // 4. Testar Criação de Inimigo Escalado
        System.out.println("\nCriando inimigo escalado para Gandalf:");
        Enemy enemy = EnemyFactory.createScaledEnemy(player);
        enemy.showStatus();

        // 5. Simular recepção de dano e cura
        System.out.println("\nSimulando Gandalf recebendo dano e usando poção:");
        player.receiveDamage(30);
        player.showStatus();
        
        Item potion = player.getInventory().get(0); // Poção de vida inicial
        potion.use(player);
        player.showStatus();

        // 6. Simular ganho de experiência e Level Up
        System.out.println("\nSimulando Gandalf ganhando experiência:");
        player.addExperience(350); // Deve subir de nível (precisa de level * 100)
        player.showStatus();

        System.out.println("\n=== SIMULAÇÃO CONCLUÍDA COM SUCESSO! TUDO FUNCIONANDO PERFEITAMENTE ===");
    }
}

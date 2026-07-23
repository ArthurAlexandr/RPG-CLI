package service;

import model.Character;
import model.Enemy;
import model.Item;
import model.Equipment;
import model.Potion;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CombatService {
    private static final Scanner sc = new Scanner(System.in);
    private static final Random random = new Random();

    /**
     * Inicia o combate em turno entre o Jogador e o Inimigo.
     * @return true se o jogador venceu, false se fugiu ou morreu.
     */
    public static boolean startCombat(Character player, Enemy enemy) {
        System.out.println("\n========================================================");
        System.out.println("          ⚔️  O COMBATE COMEÇOU! ⚔️");
        System.out.println("========================================================");
        System.out.println("Você foi atacado por um " + enemy.getName() + " (" + enemy.getEnemyType() + ") Nível " + enemy.getLevel() + "!");
        System.out.println("Preparar para a batalha!\n");

        int turn = 1;
        boolean playerGoesFirst = player.getStats().getSpeed() >= enemy.getStats().getSpeed();

        if (playerGoesFirst) {
            System.out.println("Sua velocidade é maior! Você tem a iniciativa do primeiro ataque.");
        } else {
            System.out.println("O " + enemy.getName() + " é mais rápido que você e ataca primeiro!");
        }

        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("\n--------------------------------------------------------");
            System.out.println("                   TURNO " + turn);
            System.out.println("--------------------------------------------------------");
            
            // Mostrar HP e Mana simplificados de ambos
            System.out.printf("  %-30s VS  %-30s\n", 
                player.getName() + " (Nvl " + player.getLevel() + ")", 
                enemy.getName() + " (Nvl " + enemy.getLevel() + ")"
            );
            System.out.printf("  HP: %d/%d | MP: %d/%d               HP: %d/%d | MP: %d/%d\n",
                player.getStats().getHealth(), player.getStats().getMaxHealth(),
                player.getStats().getMana(), player.getStats().getMaxMana(),
                enemy.getStats().getHealth(), enemy.getStats().getMaxHealth(),
                enemy.getStats().getMana(), enemy.getStats().getMaxMana()
            );
            System.out.println("--------------------------------------------------------");

            if (playerGoesFirst) {
                // Turno do jogador
                boolean turnConsumed = executePlayerTurn(player, enemy);
                if (!player.isAlive()) break; // No caso improvável do player morrer no próprio turno (ex: fuga perigosa se adicionado)
                if (!enemy.isAlive()) break; // Inimigo derrotado

                if (turnConsumed) {
                    // Turno do inimigo
                    executeEnemyTurn(enemy, player);
                }
            } else {
                // Turno do inimigo primeiro
                executeEnemyTurn(enemy, player);
                if (!player.isAlive()) break; // Jogador derrotado

                // Turno do jogador
                executePlayerTurn(player, enemy);
                if (!enemy.isAlive()) break; // Inimigo derrotado
            }

            turn++;
        }

        // Fim de combate
        if (player.isAlive() && !enemy.isAlive()) {
            System.out.println("\n========================================================");
            System.out.println("            🎉 VITÓRIA! 🎉");
            System.out.println("========================================================");
            System.out.println("Você derrotou o " + enemy.getName() + "!");
            
            // RECOMPENSAS
            int expReward = enemy.getLevel() * 35;
            int goldReward = 15 + random.nextInt(enemy.getLevel() * 12);
            
            player.addExperience(expReward);
            player.addGold(goldReward);

            // DROP DE ITEM (45% de chance)
            if (random.nextInt(100) < 45) {
                Item droppedItem = generateRandomLoot(player.getLevel());
                player.addToInventory(droppedItem);
                System.out.println("\n🎁 O inimigo deixou cair um item: " + droppedItem.getName() + "!");
                System.out.println("   (" + droppedItem.getDescription() + ") - Adicionado ao seu inventário.");
            }
            System.out.println("========================================================\n");
            return true;
        } else if (!player.isAlive()) {
            System.out.println("\n========================================================");
            System.out.println("            💀 GAME OVER! 💀");
            System.out.println("========================================================");
            System.out.println("Você foi derrotado por " + enemy.getName() + "...");
            System.out.println("Seu corpo cai desfalecido na masmorra.");
            System.out.println("========================================================\n");
            return false;
        }

        return false; // Retorna falso no caso de fuga bem-sucedida
    }

    private static boolean executePlayerTurn(Character player, Enemy enemy) {
        while (true) {
            System.out.println("\nEscolha sua ação:");
            System.out.println("1. Ataque Rápido ⚔️");
            System.out.println("2. Ataque Forte ✨");
            System.out.println("3. Abrir Inventário / Usar Item 🎒");
            System.out.println("4. Tentar Fugir 🏃");
            System.out.print("Sua escolha: ");
            
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.println();
                    player.weakAttack(enemy);
                    return true; // Turno consumido
                case "2":
                    System.out.println();
                    player.strongAttack(enemy);
                    return true; // Turno consumido
                case "3":
                    boolean itemUsed = openInventoryInCombat(player);
                    if (itemUsed) {
                        return true; // Turno consumido ao usar item
                    }
                    // Se não usou item (voltou), o menu principal de combate reabre sem gastar o turno
                    break;
                case "4":
                    System.out.println("\nTentando escapar...");
                    double fleeChance = 0.5 + ((double)(player.getStats().getSpeed() - enemy.getStats().getSpeed()) / 25.0);
                    // Garante limites aceitáveis (mínimo 15%, máximo 90% de chance de fuga)
                    fleeChance = Math.max(0.15, Math.min(0.90, fleeChance));

                    if (random.nextDouble() < fleeChance) {
                        System.out.println("Sucesso! Você conseguiu despistar o inimigo e fugir da batalha!");
                        enemy.receiveDamage(enemy.getStats().getHealth()); // Mata o loop encerrando a luta
                        return false; // Turno finalizado sem que o inimigo ataque
                    } else {
                        System.out.println("Falhou! O " + enemy.getName() + " bloqueou sua rota de fuga!");
                        return true; // Turno consumido tentando fugir, agora o inimigo bate
                    }
                default:
                    System.out.println("Opção inválida! Escolha um número de 1 a 4.");
            }
        }
    }

    private static void executeEnemyTurn(Enemy enemy, Character player) {
        System.out.println("\nTurno de " + enemy.getName() + ":");
        
        boolean useStrong = false;
        // Se for do tipo Mago, tenta usar ataque forte se tiver mana
        if (enemy.getEnemyType().equals("Mago")) {
            if (enemy.getStats().getMana() >= 15 && random.nextInt(100) < 60) {
                useStrong = true;
                enemy.getStats().setMana(enemy.getStats().getMana() - 15);
            }
        } else {
            // Outros tipos têm 30% de chance de usar o ataque forte
            if (random.nextInt(100) < 30) {
                useStrong = true;
            }
        }

        if (useStrong) {
            enemy.strongAttack(player);
        } else {
            enemy.weakAttack(player);
        }
    }

    private static boolean openInventoryInCombat(Character player) {
        List<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("\n[🎒 Seu inventário está completamente vazio!]");
            return false;
        }

        while (true) {
            System.out.println("\n========================================");
            System.out.println("              INVENTÁRIO");
            System.out.println("========================================");
            for (int i = 0; i < inventory.size(); i++) {
                System.out.println((i + 1) + ". " + inventory.get(i).toString());
            }
            System.out.println("0. Voltar ao Combate");
            System.out.println("========================================");
            System.out.print("Escolha o item para usar/equipar: ");

            try {
                int itemChoice = Integer.parseInt(sc.nextLine().trim());
                if (itemChoice == 0) {
                    return false; // Não consumiu turno, voltou
                }

                if (itemChoice > 0 && itemChoice <= inventory.size()) {
                    Item selectedItem = inventory.get(itemChoice - 1);
                    System.out.println();
                    selectedItem.use(player);
                    return true; // Consumiu turno usando ou equipando o item
                } else {
                    System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        }
    }

    private static Item generateRandomLoot(int level) {
        int choice = random.nextInt(4);
        if (choice == 0) {
            return new Potion("Poção de Vida Menor", "Restaura 30 HP", Potion.PotionType.HEALTH, 30);
        } else if (choice == 1) {
            return new Potion("Poção de Mana Menor", "Restaura 20 Mana", Potion.PotionType.MANA, 20);
        } else if (choice == 2) {
            // GERAR ARMA ALEATÓRIA
            int strengthBonus = 2 + (level / 2);
            int powerBonus = 2 + (level / 2);
            int speedBonus = 1 + (level / 3);
            int itemType = random.nextInt(3);
            
            if (itemType == 0) {
                return new Equipment("Espada Curta de Ferro nvl " + level, "Uma lâmina resistente de aço comum", Equipment.Slot.WEAPON, 0, 0, strengthBonus + 2, 0, 0, 0);
            } else if (itemType == 1) {
                return new Equipment("Cajado Sônico nvl " + level, "Canaliza as energias elementais", Equipment.Slot.WEAPON, 0, powerBonus + 2, 0, 0, 0, level * 5);
            } else {
                return new Equipment("Arco Curto nvl " + level, "Arco flexível ideal para disparos ágeis", Equipment.Slot.WEAPON, 0, 0, strengthBonus + 1, 0, speedBonus + 1, 0);
            }
        } else {
            // GERAR ARMADURA ALEATÓRIA
            int defenseBonus = 2 + (level / 2);
            int healthBonus = 10 + (level * 3);
            int speedBonus = 1 + (level / 4);
            int itemType = random.nextInt(3);
            
            if (itemType == 0) {
                return new Equipment("Manto de Tecido Élfico nvl " + level, "Leve e embebido em magia protetora", Equipment.Slot.ARMOR, healthBonus - 5, level, 0, defenseBonus, speedBonus + 1, level * 4);
            } else if (itemType == 1) {
                return new Equipment("Corselete de Couro nvl " + level, "Oferece boa mobilidade e proteção básica", Equipment.Slot.ARMOR, healthBonus, 0, 0, defenseBonus + 1, speedBonus, 0);
            } else {
                return new Equipment("Armadura de Placas de Ferro nvl " + level, "Muito pesada e altamente defensiva", Equipment.Slot.ARMOR, healthBonus + 10, 0, 0, defenseBonus + 3, -1, 0);
            }
        }
    }
}

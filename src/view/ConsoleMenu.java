package view;

import model.Character;
import model.Enemy;
import model.Item;
import model.Potion;
import model.Equipment;
import factory.EnemyFactory;
import service.CombatService;
import java.util.Scanner;
import java.util.List;

public class ConsoleMenu {
    private static final Scanner sc = new Scanner(System.in);

    public static void startAdventure(Character player) {
        int choice = 0;
        do {
            System.out.println("\n========================================================");
            System.out.println("                MENU DE AVENTURA - RPG CLI");
            System.out.println("========================================================");
            System.out.println("1. ⚔️ Explorar Masmorra (Batalhar)");
            System.out.println("2. 🎒 Abrir Inventário & Status");
            System.out.println("3. 🏪 Visitar Mercador (Loja)");
            System.out.println("4. 💤 Descansar na Estalagem (10g - Cura total)");
            System.out.println("0. 🚪 Voltar ao Menu Principal");
            System.out.println("========================================================");
            System.out.print("Escolha sua ação: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1:
                        explore(player);
                        if (!player.isAlive()) {
                            System.out.println("\n[💀 Você faleceu e sua jornada chegou ao fim. Retornando ao menu principal...] ");
                            return; // Encerra a aventura se o player morrer
                        }
                        break;
                    case 2:
                        showInventoryAndStatus(player);
                        break;
                    case 3:
                        visitShop(player);
                        break;
                    case 4:
                        restAtInn(player);
                        break;
                    case 0:
                        System.out.println("\nRetornando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        } while (choice != 0);
    }

    private static void explore(Character player) {
        System.out.println("\nVocê se esgueira pelos corredores escuros da masmorra...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        // Gera um inimigo escalado
        Enemy enemy = EnemyFactory.createScaledEnemy(player);
        CombatService.startCombat(player, enemy);
    }

    private static void showInventoryAndStatus(Character player) {
        int choice = 0;
        do {
            player.showStatus();
            List<Item> inventory = player.getInventory();
            System.out.println("\n========================================");
            System.out.println("               INVENTÁRIO");
            System.out.println("========================================");
            if (inventory.isEmpty()) {
                System.out.println("[ Seu inventário está vazio ]");
            } else {
                for (int i = 0; i < inventory.size(); i++) {
                    System.out.println((i + 1) + ". " + inventory.get(i).toString());
                }
            }
            System.out.println("----------------------------------------");
            System.out.println("Opções:");
            System.out.println("1. Usar / Equipar Item");
            System.out.println("2. Desequipar Arma");
            System.out.println("3. Desequipar Armadura");
            System.out.println("0. Voltar ao Menu de Aventura");
            System.out.println("========================================");
            System.out.print("Escolha uma opção: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1:
                        if (inventory.isEmpty()) {
                            System.out.println("Nenhum item para usar.");
                            break;
                        }
                        System.out.print("Digite o número do item que deseja usar: ");
                        int itemIndex = Integer.parseInt(sc.nextLine().trim()) - 1;
                        if (itemIndex >= 0 && itemIndex < inventory.size()) {
                            Item item = inventory.get(itemIndex);
                            System.out.println();
                            item.use(player);
                        } else {
                            System.out.println("Item inválido!");
                        }
                        break;
                    case 2:
                        player.unequip(Equipment.Slot.WEAPON);
                        break;
                    case 3:
                        player.unequip(Equipment.Slot.ARMOR);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        } while (choice != 0);
    }

    private static void visitShop(Character player) {
        int choice = 0;
        // Itens à venda com base no nível do jogador para ficarem atraentes
        int lvl = player.getLevel();
        Potion minorHP = new Potion("Poção de Vida Menor", "Restaura 30 HP", Potion.PotionType.HEALTH, 30);
        Potion standardHP = new Potion("Poção de Vida Média", "Restaura 60 HP", Potion.PotionType.HEALTH, 60);
        Potion minorMP = new Potion("Poção de Mana Menor", "Restaura 20 Mana", Potion.PotionType.MANA, 20);
        
        // Equipamentos legais
        Equipment shopWeapon;
        Equipment shopArmor;
        if (player instanceof model.Warrior) {
            shopWeapon = new Equipment("Espada Longa de Aço nvl " + lvl, "Uma lâmina afiada para guerreiros", Equipment.Slot.WEAPON, 0, 0, 5 + lvl * 2, 0, 0, 0);
            shopArmor = new Equipment("Armadura de Cota de Malha nvl " + lvl, "Oferece excelente resistência", Equipment.Slot.ARMOR, 30 + lvl * 10, 0, 0, 6 + lvl, -1, 0);
        } else if (player instanceof model.Mage) {
            shopWeapon = new Equipment("Cajado Arcano nvl " + lvl, "Canaliza grandes magias", Equipment.Slot.WEAPON, 0, 5 + lvl * 2, 0, 0, 0, 10 + lvl * 5);
            shopArmor = new Equipment("Manto do Conjurador nvl " + lvl, "Feito de tecido místico protetor", Equipment.Slot.ARMOR, 20 + lvl * 5, lvl * 2, 0, 4 + lvl, 1, 15 + lvl * 5);
        } else { // Archer
            shopWeapon = new Equipment("Arco Composto nvl " + lvl, "Um arco refinado de alta precisão", Equipment.Slot.WEAPON, 0, 0, 4 + lvl * 2, 0, 2 + lvl / 2, 0);
            shopArmor = new Equipment("Manto do Caçador nvl " + lvl, "Camuflagem e agilidade", Equipment.Slot.ARMOR, 25 + lvl * 8, 0, 0, 5 + lvl, 2 + lvl / 2, 0);
        }

        int costMinorHP = 15;
        int costStandardHP = 30;
        int costMinorMP = 15;
        int costWeapon = 40 + lvl * 15;
        int costArmor = 40 + lvl * 15;

        do {
            System.out.println("\n========================================================");
            System.out.println("                   MERCADOR DA MASMORRA");
            System.out.println("========================================================");
            System.out.println("Ouro Atual: " + player.getGold() + "g");
            System.out.println("--------------------------------------------------------");
            System.out.println("1. Compre " + minorHP.getName() + " (" + minorHP.getDescription() + ") - Custo: " + costMinorHP + "g");
            System.out.println("2. Compre " + standardHP.getName() + " (" + standardHP.getDescription() + ") - Custo: " + costStandardHP + "g");
            System.out.println("3. Compre " + minorMP.getName() + " (" + minorMP.getDescription() + ") - Custo: " + costMinorMP + "g");
            System.out.println("4. Compre " + shopWeapon.getName() + " (" + shopWeapon.getDescription() + ") - Custo: " + costWeapon + "g");
            System.out.println("5. Compre " + shopArmor.getName() + " (" + shopArmor.getDescription() + ") - Custo: " + costArmor + "g");
            System.out.println("0. Sair da Loja");
            System.out.println("========================================================");
            System.out.print("Escolha o número do item para comprar: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                Item toBuy = null;
                int price = 0;

                switch (choice) {
                    case 1: toBuy = minorHP; price = costMinorHP; break;
                    case 2: toBuy = standardHP; price = costStandardHP; break;
                    case 3: toBuy = minorMP; price = costMinorMP; break;
                    case 4: toBuy = shopWeapon; price = costWeapon; break;
                    case 5: toBuy = shopArmor; price = costArmor; break;
                    case 0: break;
                    default: System.out.println("Opção inválida!");
                }

                if (toBuy != null) {
                    if (player.spendGold(price)) {
                        player.addToInventory(toBuy);
                        System.out.println("\n🎉 Você comprou " + toBuy.getName() + " por " + price + "g!");
                        // Recria o item comprado para que o mercador possa vender outro se o jogador quiser
                        if (choice == 1) minorHP = new Potion("Poção de Vida Menor", "Restaura 30 HP", Potion.PotionType.HEALTH, 30);
                        if (choice == 2) standardHP = new Potion("Poção de Vida Média", "Restaura 60 HP", Potion.PotionType.HEALTH, 60);
                        if (choice == 3) minorMP = new Potion("Poção de Mana Menor", "Restaura 20 Mana", Potion.PotionType.MANA, 20);
                    } else {
                        System.out.println("\n❌ Ouro insuficiente para realizar esta compra!");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        } while (choice != 0);
    }

    private static void restAtInn(Character player) {
        System.out.println("\n========================================");
        System.out.println("              ESTALAGEM");
        System.out.println("========================================");
        System.out.println("Deseja descansar e recuperar suas forças?");
        System.out.println("Preço: 10 moedas de ouro");
        System.out.println("Ouro atual: " + player.getGold() + "g");
        System.out.println("----------------------------------------");
        System.out.println("1. Sim, por favor.");
        System.out.println("0. Não, obrigado.");
        System.out.println("========================================");
        System.out.print("Sua escolha: ");

        try {
            int option = Integer.parseInt(sc.nextLine().trim());
            if (option == 1) {
                if (player.spendGold(10)) {
                    player.heal(player.getStats().getMaxHealth());
                    player.restoreMana(player.getStats().getMaxMana());
                    System.out.println("\n💤 Você teve uma noite de sono maravilhosa! HP e Mana restaurados ao máximo!");
                } else {
                    System.out.println("\n❌ Você não tem moedas de ouro suficientes!");
                }
            } else {
                System.out.println("\nVocê decide economizar suas moedas.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida!");
        }
    }
}

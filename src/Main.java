import model.Character;
import model.Archer;
import model.Mage;
import model.Warrior;

import java.util.Scanner;

public class Main {
    public static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int choice = 0;
        Character player = null;
        do {
            menu();
            System.out.print("Choose an option: ");
            choice = sc.nextInt();
            sc.nextLine(); // Limpa o buffer do teclado

            switch (choice) {
                case 1:
                    player = createCharacter();
                    if (player != null) {
                        System.out.println("\nReady for adventure, " + player.isAlive() + "? Wait, let's see your stats:");
                        player.showStatus();
                    }
                    break;
                case 2:
                    System.out.println("\nLoading save... (Feature coming soon!)");
                    break;
                case 0:
                    System.out.println("\nThank you for playing RPG - CLI! Goodbye.");
                    break;
                default:
                    System.out.println("\nInvalid option! Try again.");
            }

            System.out.println(); // Apenas quebra uma linha entre ações
        } while (choice != 0);
    }

    public static void menu(){
        System.out.println("============================");
        System.out.println("         RPG - CLI");
        System.out.println("============================");
        System.out.println("1. Start New Game");
        System.out.println("2. Load Save");
        System.out.println("0. Exit");
        System.out.println("============================");
    }

    public static Character createCharacter(){
        System.out.println("============================\n");
        System.out.println("1. Warrior ⚔\uFE0F");
        System.out.println("2. Mage \uD83D\uDD2E");
        System.out.println("3. Archer \uD83C\uDFF9");
        System.out.print("Select yout character's class: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Limpar buffer de entrada

        System.out.print("\nEnter the character's name: ");
        String name = sc.nextLine();

        Character createdCharacter = null;

        switch (choice) {
            case 1:
                createdCharacter = new Warrior(name);
                System.out.println("\nWarrior " + name + " created successfully!");
                break;
            case 2:
                createdCharacter = new Mage(name);
                System.out.println("\nMage " + name + " created successfully!");
                break;
            case 3:
                createdCharacter = new Archer(name);
                System.out.println("\nArcher " + name + " created successfully!");
                break;
            default:
                System.out.println("\nInvalid class selected! Character creation failed.");
        }

        return createdCharacter; // Retorna o personagem instanciado
    }
}


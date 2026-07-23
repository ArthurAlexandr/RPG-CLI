package model;

public class Potion extends Item {
    public enum PotionType {
        HEALTH, MANA
    }

    private PotionType type;
    private int restoreAmount;

    public Potion(String name, String description, PotionType type, int restoreAmount) {
        super(name, description);
        this.type = type;
        this.restoreAmount = restoreAmount;
    }

    public PotionType getType() {
        return type;
    }

    public int getRestoreAmount() {
        return restoreAmount;
    }

    @Override
    public void use(Character character) {
        if (type == PotionType.HEALTH) {
            character.heal(restoreAmount);
            System.out.println(character.getName() + " bebeu " + getName() + " e recuperou " + restoreAmount + " HP!");
        } else if (type == PotionType.MANA) {
            character.restoreMana(restoreAmount);
            System.out.println(character.getName() + " bebeu " + getName() + " e recuperou " + restoreAmount + " Mana!");
        }
        character.removeFromInventory(this);
    }
}

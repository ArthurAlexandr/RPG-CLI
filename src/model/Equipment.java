package model;

public class Equipment extends Item {
    public enum Slot {
        WEAPON, ARMOR
    }

    private Slot slot;
    private int healthBonus;
    private int powerBonus;
    private int strengthBonus;
    private int defenseBonus;
    private int speedBonus;
    private int manaBonus;

    public Equipment(String name, String description, Slot slot, int healthBonus, int powerBonus, int strengthBonus, int defenseBonus, int speedBonus, int manaBonus) {
        super(name, description);
        this.slot = slot;
        this.healthBonus = healthBonus;
        this.powerBonus = powerBonus;
        this.strengthBonus = strengthBonus;
        this.defenseBonus = defenseBonus;
        this.speedBonus = speedBonus;
        this.manaBonus = manaBonus;
    }

    public Slot getSlot() {
        return slot;
    }

    public int getHealthBonus() {
        return healthBonus;
    }

    public int getPowerBonus() {
        return powerBonus;
    }

    public int getStrengthBonus() {
        return strengthBonus;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public int getSpeedBonus() {
        return speedBonus;
    }

    public int getManaBonus() {
        return manaBonus;
    }

    @Override
    public void use(Character character) {
        character.equip(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" [").append(slot).append("] (");
        boolean first = true;
        if (healthBonus != 0) { first = false; sb.append("HP: ").append(healthBonus > 0 ? "+" : "").append(healthBonus); }
        if (powerBonus != 0) { if (!first) sb.append(", "); first = false; sb.append("Poder: ").append(powerBonus > 0 ? "+" : "").append(powerBonus); }
        if (strengthBonus != 0) { if (!first) sb.append(", "); first = false; sb.append("Força: ").append(strengthBonus > 0 ? "+" : "").append(strengthBonus); }
        if (defenseBonus != 0) { if (!first) sb.append(", "); first = false; sb.append("Def: ").append(defenseBonus > 0 ? "+" : "").append(defenseBonus); }
        if (speedBonus != 0) { if (!first) sb.append(", "); first = false; sb.append("Vel: ").append(speedBonus > 0 ? "+" : "").append(speedBonus); }
        if (manaBonus != 0) { if (!first) sb.append(", "); sb.append("Mana: ").append(manaBonus > 0 ? "+" : "").append(manaBonus); }
        sb.append(") - ").append(description);
        return sb.toString();
    }
}

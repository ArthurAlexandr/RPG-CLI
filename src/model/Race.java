package model;

public enum Race {
    HUMAN("Human", 0, 0, 0, 0, 0),
    ELF("Elf", -10, 0, -2, 5, 20),
    DWARF("Dwarf", 20, 5, 5, -5, -10),
    ORC("Orc", 15, 10, 0, -2, -20);

    private final String name;
    private final int healthModifier;
    private final int strengthModifier;
    private final int defenseModifier;
    private final int speedModifier;
    private final int manaModifier;

    Race(String name, int healthModifier, int strengthModifier, int defenseModifier, int speedModifier, int manaModifier) {
        this.name = name;
        this.healthModifier = healthModifier;
        this.strengthModifier = strengthModifier;
        this.defenseModifier = defenseModifier;
        this.speedModifier = speedModifier;
        this.manaModifier = manaModifier;
    }

    public String getName() {
        return name;
    }

    public int getHealthModifier() {
        return healthModifier;
    }

    public int getStrengthModifier() {
        return strengthModifier;
    }

    public int getDefenseModifier() {
        return defenseModifier;
    }

    public int getSpeedModifier() {
        return speedModifier;
    }

    public int getManaModifier() {
        return manaModifier;
    }
}

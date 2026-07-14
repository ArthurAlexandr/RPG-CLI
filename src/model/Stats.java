package model;

public class Stats {

    private int health;
    private int maxHealth;
    private int power;
    private int strength;
    private int defense;
    private int speed;
    private int mana;
    private int maxMana;


    public Stats(int maxHealth, int power, int strength, int defense, int speed, int maxMana) {
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.power = power;
        this.strength = strength;
        this.defense = defense;
        this.speed = speed;
        this.mana = maxMana;
        this.maxMana = maxMana;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    @Override
    public String toString() {
        return "HP: " + health + "/" + maxHealth +
                " | Força: " + strength +
                " | Defesa: " + defense +
                " | Velocidade: " + speed +
                " | Mana: " + mana + "/" + maxMana;
    }
}
package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    protected String name;
    protected int level;
    protected int experience;
    protected Race race;
    protected Stats stats;
    protected boolean alive;
    
    // SISTEMA DE INVENTARIO E OURO
    protected int gold;
    protected List<Item> inventory;
    protected Equipment equippedWeapon;
    protected Equipment equippedArmor;

    // CONSTRUTOR PADRAO PERSONAGEM
    public Character(String name, Race race, int level, int maxHealth, int power, int strength, int defense, int speed, int maxMana) {
        this.name = name;
        this.race = race;
        this.level = level;
        this.experience = 0;
        
        // Aplica os modificadores da raça nos atributos base
        int finalHealth = maxHealth + race.getHealthModifier();
        int finalStrength = strength + race.getStrengthModifier();
        int finalDefense = defense + race.getDefenseModifier();
        int finalSpeed = speed + race.getSpeedModifier();
        int finalMana = maxMana + race.getManaModifier();
        
        this.stats = new Stats(finalHealth, power, finalStrength, finalDefense, finalSpeed, finalMana);
        this.alive = finalHealth > 0;
        
        this.gold = 50; // Começa com 50 de ouro por padrão
        this.inventory = new ArrayList<>();
        
        // Dá alguns itens iniciais (uma poção de vida)
        this.inventory.add(new Potion("Poção de Vida Menor", "Restaura 30 HP", Potion.PotionType.HEALTH, 30));
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public Stats getStats() {
        return stats;
    }

    public Race getRace() {
        return race;
    }

    public int getExperience() {
        return experience;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
            System.out.println(this.name + " ganhou " + amount + " moedas de ouro!");
        }
    }

    public boolean spendGold(int amount) {
        if (amount <= 0) return false;
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public Equipment getEquippedWeapon() {
        return equippedWeapon;
    }

    public Equipment getEquippedArmor() {
        return equippedArmor;
    }

    public void addToInventory(Item item) {
        if (item != null) {
            this.inventory.add(item);
        }
    }

    public void removeFromInventory(Item item) {
        if (item != null) {
            this.inventory.remove(item);
        }
    }

    // EQUIPAR ITENS
    public void equip(Equipment equipment) {
        if (equipment == null) return;

        Equipment oldEquipment = null;
        if (equipment.getSlot() == Equipment.Slot.WEAPON) {
            oldEquipment = this.equippedWeapon;
            this.equippedWeapon = equipment;
        } else if (equipment.getSlot() == Equipment.Slot.ARMOR) {
            oldEquipment = this.equippedArmor;
            this.equippedArmor = equipment;
        }

        // Se já tinha algo equipado nesse slot, removemos os atributos dele e devolvemos ao inventário
        if (oldEquipment != null) {
            applyEquipmentStats(oldEquipment, false);
            this.inventory.add(oldEquipment);
            System.out.println(this.name + " desequipou " + oldEquipment.getName() + ".");
        }

        // Aplica os atributos do novo equipamento e remove do inventário
        applyEquipmentStats(equipment, true);
        this.inventory.remove(equipment);
        System.out.println(this.name + " equipou " + equipment.getName() + "!");
    }

    // DESEQUIPAR ITENS
    public void unequip(Equipment.Slot slot) {
        Equipment equipment = null;
        if (slot == Equipment.Slot.WEAPON) {
            equipment = this.equippedWeapon;
            this.equippedWeapon = null;
        } else if (slot == Equipment.Slot.ARMOR) {
            equipment = this.equippedArmor;
            this.equippedArmor = null;
        }

        if (equipment != null) {
            applyEquipmentStats(equipment, false);
            this.inventory.add(equipment);
            System.out.println(this.name + " desequipou " + equipment.getName() + " e guardou no inventário.");
        } else {
            System.out.println("Nenhum equipamento neste slot.");
        }
    }

    private void applyEquipmentStats(Equipment eq, boolean add) {
        int mod = add ? 1 : -1;
        stats.setMaxHealth(stats.getMaxHealth() + (eq.getHealthBonus() * mod));
        if (add) {
            stats.setHealth(stats.getHealth() + (eq.getHealthBonus() * mod));
        } else {
            stats.setHealth(Math.max(1, stats.getHealth() + (eq.getHealthBonus() * mod)));
        }
        stats.setPower(stats.getPower() + (eq.getPowerBonus() * mod));
        stats.setStrength(stats.getStrength() + (eq.getStrengthBonus() * mod));
        stats.setDefense(stats.getDefense() + (eq.getDefenseBonus() * mod));
        stats.setSpeed(stats.getSpeed() + (eq.getSpeedBonus() * mod));
        stats.setMaxMana(stats.getMaxMana() + (eq.getManaBonus() * mod));
        if (add) {
            stats.setMana(stats.getMana() + (eq.getManaBonus() * mod));
        } else {
            stats.setMana(Math.max(0, stats.getMana() + (eq.getManaBonus() * mod)));
        }
    }

    public void addExperience(int amount) {
        if (amount > 0) {
            this.experience += amount;
            System.out.println(this.name + " ganhou " + amount + " de experiência!");
            checkLevelUp();
        }
    }

    protected void checkLevelUp() {
        int experienceNeeded = level * 100;
        if (this.experience >= experienceNeeded) {
            this.experience -= experienceNeeded;
            levelUp();
            // Verifica recursivamente se ganhou experiência suficiente para subir mais de um nível
            checkLevelUp();
        }
    }

    // METODO RESPONSAVEL POR CALCULAR DANO RECEBIDO
    public void receiveDamage(int damage){
        // VERIFICA SE DANO E MAIOR QUE 0, EVITANDO QUE O ATAQUE CURE O PERSONAGEM
        if (damage < 0) return;

        int currentHp = stats.getHealth();
        int newHP = Math.max(0, currentHp - damage);

        stats.setHealth(newHP);

        if (newHP <= 0){
            this.alive = false;
        }
    }

    // METODO RESPONSAVEL PELA CURA DO PERSONAGEM
    public void heal(int amount){
        if (amount < 0) return;

        int currentHp = stats.getHealth();
        int maxHp = stats.getMaxHealth();

        // EVITA QUE O PERSONAGEM SEJA CURADO AO PONTO DE TER MAIS VIDA DO QUE O PERMITIDO NO MAXHEALTH
        stats.setHealth(Math.min(maxHp, currentHp + amount));
    }

    // METODO RESPONSAVEL POR RECUPERAR MANA DO PERSONAGEM
    public void restoreMana(int amount) {
        if (amount < 0) return;

        int currentMana = stats.getMana();
        int maxMana = stats.getMaxMana();

        stats.setMana(Math.min(maxMana, currentMana + amount));
    }

    //METODO QUE VERIFICA SE O PERSONAGEM ESTA VIVO
    public boolean isAlive(){
        return alive;
    }

    // METODO RESPONSAVEL POR UPAR O PERSONAGEM
    public void levelUp(){
        level++;
        System.out.println("\n========================================");
        System.out.println("Parabéns! " + name + " alcançou o nível " + level + "!");
        System.out.println("========================================\n");
        
        // Aumenta os atributos ao subir de nível
        stats.setMaxHealth(stats.getMaxHealth() + 10);
        stats.setHealth(stats.getMaxHealth());
        stats.setStrength(stats.getStrength() + 2);
        stats.setDefense(stats.getDefense() + 2);
        stats.setSpeed(stats.getSpeed() + 1);
        stats.setMaxMana(stats.getMaxMana() + 5);
        stats.setMana(stats.getMaxMana());
    }

    public void showStatus(){
        System.out.println("=================================================================================");
        System.out.println("Nome: " + name + " | Raça: " + race.getName() + " | Nível: " + level + " (Exp: " + experience + "/" + (level * 100) + ") | Ouro: " + gold + "g");
        System.out.println("HP: " + stats.getHealth() + "/" + stats.getMaxHealth() +
                " | " + getPrimaryStat() +
                " | Defesa: " + stats.getDefense() +
                " | Velocidade: " + stats.getSpeed() +
                " | Mana: " + stats.getMana() + "/" + stats.getMaxMana());
        System.out.println("Arma: " + (equippedWeapon != null ? equippedWeapon.getName() : "Nenhuma") +
                " | Armadura: " + (equippedArmor != null ? equippedArmor.getName() : "Nenhuma"));
        System.out.println("=================================================================================");
    }

    // Método protegido que permite o Polimorfismo nas subclasses
    protected String getPrimaryStat() {
        return "Força: " + stats.getStrength();
    }

    public abstract void strongAttack(Character target);

    public abstract void weakAttack(Character target);
}

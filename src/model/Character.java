package model;

public abstract class Character {
    protected String name;
    protected int level;
    protected int experience;
    protected Race race;
    protected Stats stats;

    protected boolean alive;

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
    }

    public Race getRace() {
        return race;
    }

    public int getExperience() {
        return experience;
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

    //METODO QUE VERIFICA SE O PERSONAGEM ESTA VIVO
    public boolean isAlive(){
        return alive;
    }


    // METODO RESPONSAVEL POR UPAR O PERSONAGEM
    public void levelUp(){
        level++;
        System.out.println("Parabéns! " + name + " alcançou o nível " + level + "!");
        
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
        System.out.println("Nome: " + name + " | Raça: " + race.getName() + " | Nível: " + level + " (Exp: " + experience + "/" + (level * 100) + ")");
        System.out.println("HP: " + stats.getHealth() + "/" + stats.getMaxHealth() +
                " | " + getPrimaryStat() +
                " | Defesa: " + stats.getDefense() +
                " | Velocidade: " + stats.getSpeed() +
                " | Mana: " + stats.getMana() + "/" + stats.getMaxMana());
    }

    // Método protegido que permite o Polimorfismo nas subclasses
    protected String getPrimaryStat() {
        return "Força: " + stats.getStrength();
    }

    public void strongAttack(Character target){}

    public void weakAttack(Character target){}
}

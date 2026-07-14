package model;

public abstract class Character {
    protected String name;
    protected int level;
    protected Stats stats;

    protected boolean alive;

    // CONSTRUTOR PADRAO PERSONAGEM
    public Character(String name, int level, int maxHealth, int power, int strength, int defense, int speed, int maxMana) {
        this.name = name;
        this.level = level;
        this.stats = new Stats(maxHealth, power, strength, defense, speed, maxMana);
        this.alive = maxHealth > 0;
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
    }

    public void showStatus(){
        System.out.println(stats);
    }

    public void strongAttack(Character target){}

    public void weakAttack(Character target){}
}

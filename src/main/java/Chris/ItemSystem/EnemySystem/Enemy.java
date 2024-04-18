package Chris.ItemSystem.EnemySystem;

import Chris.*;
import Chris.ItemSystem.*;

public class Enemy {
    public String name;
    public int health;
    int maxHealth;
    int attackDamage;
    boolean healer;

    public Enemy(String enemyName, int enemyMaxHealth, int enemyAttackDamage) {
        name = enemyName;
        health = enemyMaxHealth;
        maxHealth = enemyMaxHealth;
        attackDamage = enemyAttackDamage;
        healer = false;
    }

    public Enemy(String enemyName, int enemyMaxHealth, int enemyAttackDamage, boolean isHealer) {
        name = enemyName;
        health = enemyMaxHealth;
        maxHealth = enemyMaxHealth;
        attackDamage = enemyAttackDamage;
        healer = isHealer;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            GUI.println(name + " has been slain!");
            int weight = (int)(2 * (health * 0.75 + damage * 1.5 + 1));
            Inventory.current.addExperience(weight);
            GUI.println("Got " + weight + " XP!");
            double cashToAdd = 0;
            for (int i = 0; i < weight / 5 + 1; i++)
            {
                cashToAdd += (((double)((int)((Math.random() + 1.5) * 245))) / 100) + 0.5;
            }
            Inventory.current.addCredits(cashToAdd);
            GUI.println("Got " + Inventory.current.getCreditsText(cashToAdd) + " credits!");
        }
    }

    public void attack() {
        GUI.print(name + " attacks for " + attackDamage + " damage");
        if (Math.random() < 0.02)
        {
            GUI.println(" twice! CRITICAL HIT");
            Inventory.current.health -= attackDamage * 2;
        }
        else if (Math.random() < 0.9)
        {
            GUI.println("!");
            Inventory.current.health -= attackDamage;
        }
        else
        {
            GUI.println(", but misses!");
        }
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth)
        {
            health = maxHealth;
        }
        GUI.println(name + " heals for " + amount + " health points.");
    }

    public boolean isHealer()
    {
        return healer;
    }
}
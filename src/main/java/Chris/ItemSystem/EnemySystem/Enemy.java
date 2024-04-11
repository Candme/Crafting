package Chris.ItemSystem.EnemySystem;

import Chris.*;
import Chris.ItemSystem.*;

public class Enemy {
    public String name;
    public int health;
    int maxHealth;
    int attackDamage;

    public Enemy(String enemyName, int enemyMaxHealth, int enemyAttackDamage) {
        name = enemyName;
        health = enemyMaxHealth;
        maxHealth = enemyMaxHealth;
        attackDamage = enemyAttackDamage;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            GUI.println(name + " has been slain!");
            int weight = (int)(2 * (health * 0.2) + (damage * 0.25) + 1);
            Inventory.current.addExperience(weight);
            GUI.println("Got " + weight + " XP!");
Inventory.current.addItem(Inventory.current.registry.findItem("Wallet"), weight);
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
        System.out.println(name + " heals for " + amount + " health points.");
    }
}
package Chris.ItemSystem.EnemySystem;

import java.util.*;

import Chris.*;
import Chris.ItemSystem.*;
import Chris.ItemSystem.ItemTypes.*;

public class GroupFight
{
    Enemy[] enemies;
    
    public GroupFight(Enemy[] opponents)
    {
        enemies = opponents;
        GUI.println("A group of enemies appear!");
        for (int i = 0; i < enemies.length; i++)
        {
            GUI.println("[" + i + "] " + enemies[i].name);
        }
        GUI.println("[Type 'attack' or 'use' to take action]");
        Runner.inputQueue = this::takeTurn;
    }

    //Needs to be done still    
    public void takeTurn(String action)
    {
        action = action.toLowerCase();
        switch (action)
        {
            case "attack":
            {
                GUI.println("Which enemy would you like to attack?");
                Runner.inputQueue = this::attack;
                break;
            }
            case "use":
            {
                Runner.inputQueue = this::heal;
                GUI.println("What item would you like to use?");
                break;
            }
            default:
            {
                GUI.println("Invalid Input");
                break;
            }    
        }
        
    }

    public void attack(String intBuffer)
    {
        int dex = -1;
        
        try
        {
            dex = Integer.parseInt(intBuffer);
        }
        catch (Exception e)
        {
            dex = -1;
        }

        if (dex == -1 || dex >= enemies.length || enemies[dex].health <= 0)
        {
            GUI.println("Invalid input.");
            Runner.inputQueue = this::takeTurn;
        }
        else
        {
            int damage = 1;
            if (Inventory.current.equipped != null)
            {
                damage = Inventory.current.equipped.amount;
                Inventory.current.equipped.use();
            }
            if (Math.random() < 0.02)
            {
                GUI.println("Did " + damage + " damage to the " + enemies[dex].name + " twice! CRITICAL HIT");
                damage *= 2;
            }
            else if (Math.random() < 0.9)
            {
                GUI.println("Did " + damage + " damage to the " + enemies[dex].name + "!");
            }
            else
            {
                GUI.println("You tripped.");
                damage = 0;
            }

            enemies[dex].takeDamage(damage);
            resolve();
        }
    }

    public void heal(String itemString)
    {
        Item item = Inventory.current.getItem(itemString);
        HealingItem h;
        if (item == null)
        {
            GUI.println("You do not have a " + itemString);
            Runner.inputQueue = this::takeTurn;
        }
        else
        {
            try 
            {
                h = (HealingItem)item;
                h.use();
                Runner.inputQueue = this::takeTurn;
                resolve();
            }
            catch (Exception e)
            {
                try
                {
                    Weapon w = (Weapon)item;
                    w.use();
                    GUI.println("Equipped a " + itemString + ".");
                    Runner.inputQueue = this::takeTurn;
                    resolve();
                }
                catch (Exception e2)
                {
                    GUI.println(itemString + " is not a valid item.");
                    Runner.inputQueue = this::takeTurn;
                }
            }
        }
    }

    public void resolve()
    {
        boolean t = true;
        for (Enemy e: enemies)
        {
            if (e.health > 0)
            {
                t = false;
                if (e.isHealer() && Math.random() > 0.93)
                {
                    e.heal(e.maxHealth / 10);
                }
                else
                {
                    e.attack();
                }
            }
        }
        if (t)
        {
            Runner.inputQueue = null;
        }
        else if (Inventory.current.health <= 0)
        {
            GUI.println("YOU DIED");
            Inventory.current.die();
            Runner.inputQueue = null;
        }
        else
        {
            Runner.inputQueue = this::takeTurn;
        }
    }
}
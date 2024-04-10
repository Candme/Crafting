package Chris.ItemSystem.EnemySystem;

import Chris.*;
import Chris.ItemSystem.*;
import Chris.ItemSystem.ItemTypes.*;

public class Fight
{
    Enemy enemy;
    
    public Fight(Enemy opponent)
    {
        enemy = opponent;
        GUI.println("A " + enemy.name + " appears!");
        GUI.println("[Type 'attack' or 'use' to take action]");
        Runner.inputQueue = this::takeTurn;
    }

    public void takeTurn(String action)
    {
        action = action.toLowerCase();
        switch (action)
        {
            case "attack":
            {
                int damage = 1;
                if (Inventory.current.equipped != null)
                {
                    damage = Inventory.current.equipped.amount;
                    Inventory.current.equipped.use();
                }
                if (Math.random() < 0.02)
                {
                    GUI.println("Did " + damage + " damage twice! CRITICAL HIT");
                    damage *= 2;
                }
                else if (Math.random() < 0.9)
                {
                    GUI.println("Did " + damage + " damage!");
                }
                else
                {
                    GUI.println("You tripped.");
                    damage = 0;
                }
                
                enemy.takeDamage(damage);
                resolve();
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
                GUI.println(itemString + " is not a healing item.");
                Runner.inputQueue = this::takeTurn;
            }
        }
    }

    public void resolve()
    {
        if (enemy.health <= 0)
        {
            Runner.inputQueue = null;
        }
        else
        {
            enemy.attack();
            GUI.println();
            if (Inventory.current.health <= 0)
            {
                GUI.println("YOU DIED");
                Inventory.current.die();
                Runner.inputQueue = null;
            }
        }
    }
}
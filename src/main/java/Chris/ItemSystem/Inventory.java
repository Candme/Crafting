package Chris.ItemSystem;

import java.util.ArrayList;

import Chris.*;
import Chris.ItemSystem.ItemTypes.*;

public class Inventory implements java.io.Serializable {
    public static Inventory current = null;

    double money;
    ArrayList<Item> items;
    ArrayList<Integer> itemAmounts;
    public ItemRegistry registry;

    public Weapon equipped;

    public int health;
    public int maxHealth;

    public int experience;
    public int maxExperience;
    public int level;

    public Inventory() {
        money = 5;
        
        items = new ArrayList<Item>();
        itemAmounts = new ArrayList<Integer>();
        registry = new ItemRegistry();

        current = this;

        health = 3;
        maxHealth = 3;

        experience = 0;
        maxExperience = 50;
        level = 1;
    }

    // Returns item if in inventory
    public Item getItem(String itemName) {
        if (itemName != null) {
            for (int i = 0; i < items.size(); i++) {
                if (itemName.equals(items.get(i).getName())) {
                    return items.get(i);
                }
            }
            return null;
        }
        return null;
    }

    public void addItem(Item item) {
        if (item != null) {
            for (int i = 0; i < items.size(); i++) {
                if (item.getName().equals(items.get(i).getName())) {
                    itemAmounts.set(i, itemAmounts.get(i) + 1);
                    return;
                }
            }
            items.add(item);
            itemAmounts.add(1);
        }
    }

    public void addItem(Item item, int amount) {
        if (item != null && amount > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (item.getName().equals(items.get(i).getName())) {
                    itemAmounts.set(i, itemAmounts.get(i) + amount);
                    return;
                }
            }
            items.add(item);
            itemAmounts.add(amount);
        }
    }

    // Returns first tool of proper type, if not null
    public Tool findToolOfType(Tool.ToolType toolType) {
        for (Item i : items) {
            try {
                Tool tool = (Tool) i;
                if (tool.getToolType() == toolType) {
                    return tool;
                }
            } catch (Exception e) {

            }
        }
        return null;
    }

    // Returns true if item successfully removed
    public boolean removeItem(Item item) {
        if (item != null) {
            for (int i = 0; i < items.size(); i++) {
                if (item.getName().equals(items.get(i).getName())) {
                    itemAmounts.set(i, itemAmounts.get(i) - 1);
                    if (itemAmounts.get(i) <= 0) {
                        items.remove(i);
                        itemAmounts.remove(i);
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    // Returns true if item successfully removed
    public boolean removeItem(Item item, int amount) {
        if (item != null && amount > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (item.getName().equals(items.get(i).getName())) {
                    itemAmounts.set(i, itemAmounts.get(i) - amount);
                    if (itemAmounts.get(i) <= 0) {
                        items.remove(i);
                        itemAmounts.remove(i);
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    // Returns true if item is in inventory
    public boolean hasItem(Item item) {
        if (item != null) {
            for (int i = 0; i < items.size(); i++) {
                if (item.getName().equals(items.get(i).getName())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    // Returns true if item is in inventory
    public boolean hasItem(Item item, int amount) {
        if (item != null & amount > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (item.getName().equals(items.get(i).getName()) && itemAmounts.get(i) >= amount) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    // Returns true if item successfully crafted
    public boolean craft(Recipe recipe) {
        if (recipe != null) {
            for (int i = 0; i < recipe.ingredients.size(); i++) {
                if (!hasItem(recipe.ingredients.get(i), recipe.ingredientAmounts.get(i))) {
                    return false;
                }
            }
            for (int i = 0; i < recipe.ingredients.size(); i++) {
                removeItem(recipe.ingredients.get(i), recipe.ingredientAmounts.get(i));
            }
            addItem(recipe.result, recipe.yield);
            return true;
        }
        return false;
    }

    // Returns recipe used to successfully craft, otherwise returns -1
    public int craft(Item item) {
        if (item != null) {
            for (int recipeInt = 0; recipeInt < item.getRecipes().size(); recipeInt++) {
                boolean truth = true;
                Recipe recipe = item.getRecipes().get(recipeInt);
                for (int i = 0; i < recipe.ingredients.size(); i++) {
                    if (!hasItem(recipe.ingredients.get(i), recipe.ingredientAmounts.get(i))) {
                        truth = false;
                    }
                }
                if (truth) {
                    for (int i = 0; i < recipe.ingredients.size(); i++) {
                        removeItem(recipe.ingredients.get(i), recipe.ingredientAmounts.get(i));
                    }
                    addItem(recipe.result, recipe.yield);
                    return recipeInt;
                }
            }
            return -1;
        }
        return -1;
    }

    // Returns true if item successfully crafted with recipe
    public boolean craft(Item item, int recipeInt) {
        if (item != null) {
            boolean truth = true;
            Recipe recipe = item.getRecipes().get(recipeInt);
            for (int i = 0; i < recipe.ingredients.size(); i++) {
                if (!hasItem(recipe.ingredients.get(i), recipe.ingredientAmounts.get(i))) {
                    truth = false;
                }
            }
            if (truth) {
                for (int i = 0; i < recipe.ingredients.size(); i++) {
                    removeItem(recipe.ingredients.get(i), recipe.ingredientAmounts.get(i));
                }
                addItem(recipe.result, recipe.yield);
                return true;
            }
            return false;
        }
        return false;
    }

    public void printInventoryList() {
        if (items.size() > 0) {
            GUI.println("\nInventory contents:");
            for (int i = 0; i < items.size(); i++) {
                GUI.println(itemAmounts.get(i) + "x " + items.get(i).getName());
            }
        } else {
            GUI.println("Inventory empty.");
        }
    }

    public double getCredits()
    {
        return money;
    }

    public String getCreditsText()
    {
        String returnSt = "" + money;
        int index = returnSt.indexOf(".");
        String subst = returnSt.substring(index + 1);
        if (subst.length() > 2)
        {
            return returnSt.substring(0, index + 3);
        }
        else if (subst.length() < 2)
        {
            return returnSt + "0";
        }
        return returnSt;
    }

    public String getCreditsText(double override)
    {
        String returnSt = "" + override;
        int index = returnSt.indexOf(".");
        String subst = returnSt.substring(index + 1);
        if (subst.length() > 2)
        {
            return returnSt.substring(0, index + 3);
        }
        else if (subst.length() < 2)
        {
            return returnSt + "0";
        }
        return returnSt;
    }

    public void addCredits(double amount)
    {
        money += amount;
    }

    public ItemRegistry getItemRegistry() {
        return registry;
    }

    public static void importSave(Inventory inventory) {
        current = inventory;
    }

    public String toString()
    {
        if (items.size() > 0) {
            String returnSt = "";
            for (int i = 0; i < items.size(); i++) {
                returnSt += (itemAmounts.get(i) + "x " + items.get(i).getName() + "\n");
            }
            return returnSt;
        } else {
            return "Inventory empty.";
        }
    }

    public void addExperience(int amount)
    {
        experience += amount;
        if (experience >= maxExperience)
        {
            experience -= maxExperience;
            level ++;
            maxExperience *= 1.5;
            int toAdd = level * 2;
            //To make health 72 instead of 73
            if (level == 8)
            {
                toAdd --;
            }
            maxHealth += toAdd;
            health += toAdd;
            addItem(registry.findItem("Wallet"), level * 2);
            while (hasItem(registry.findItem("Wallet")))
            {
                ((Wallet)Inventory.current.getItem("Wallet")).use(false);  
            }
        }
    }

    public void die()
    {
        addCredits(getCredits() / 2.25);
        experience = 0;
        for (int i = 0; i < items.size(); i ++)
        {
            if (Math.random() > 0.6)
            {
                removeItem(items.get(i), (int)(Math.random() * itemAmounts.get(i) + 1));
            }
        }
        health = maxHealth/2 + 1;
    }
}
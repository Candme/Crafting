package Chris.ItemSystem;

import Chris.*;
import Chris.ItemSystem.ItemTypes.*;

import java.util.ArrayList;

public class Recipe implements java.io.Serializable {
    public Item result;
    public Tool catalyst;
    public int yield;
    public ArrayList<Integer> ingredientAmounts;
    public ArrayList<Item> ingredients;

    public Recipe(Item item) {
        result = item;
        yield = 1;
        ingredientAmounts = new ArrayList<Integer>();
        ingredients = new ArrayList<Item>();
        item.addRecipe(this);
    }

    public Recipe(Item item, int amount) {
        result = item;
        yield = amount;
        ingredientAmounts = new ArrayList<Integer>();
        ingredients = new ArrayList<Item>();
        item.addRecipe(this);
    }

    public boolean hasItems() {
        for (int i = 0; i < ingredients.size(); i++) {
            if (!Inventory.current.hasItem(ingredients.get(i), ingredientAmounts.get(i))) {
                return false;
            }
        }
        if (catalyst == null) {
            return true;
        } else {
            if (Inventory.current.hasItem(catalyst)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean hasItems(int amount) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (!Inventory.current.hasItem(ingredients.get(i), ingredientAmounts.get(i) * amount)) {
                return false;
            }
        }
        return true;
    }

    public void addIngredient(Item component) {
        ingredientAmounts.add(1);
        ingredients.add(component);
    }

    public void addIngredient(Item component, Integer amount) {
        ingredientAmounts.add(amount);
        ingredients.add(component);
    }

    public void addCatalyst(Item catalyst) {
        try {
            Tool tol = (Tool) catalyst;
            if (tol.getToolType() == Tool.ToolType.CRAFTING) {
                this.catalyst = tol;
            }
        } catch (Exception ex) {

        }

    }

    public String toString() {
        String returnString = yield + " " + result.getName();
        if (yield > 1) {
            returnString += "s are made ";
        } else {
            returnString += " is made ";
        }
        if (catalyst != null) {
            returnString += "using a " + catalyst.getName() + " ";
        }
        returnString += "with:\n    ";
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredientAmounts.get(i) > 1) {
                returnString += ingredientAmounts.get(i) + " " + ingredients.get(i).getName() + "s\n    ";
            } else {
                returnString += ingredientAmounts.get(i) + " " + ingredients.get(i).getName() + "\n    ";
            }
        }
        return returnString;
    }
}
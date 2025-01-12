package Chris.ItemSystem;

import java.util.ArrayList;
import Chris.*;

public class Item implements java.io.Serializable
{
    String name;
    int tier;
    ArrayList<Recipe> recipes;

    public Item(String itemName, int itemTier)
    {
        recipes = new ArrayList<Recipe>();
        name = itemName;
        tier = itemTier;
    }

    public String getName()
    {
        return name;
    }

   public int getTier()
    {
        return tier;
    }

    public void addRecipe(Recipe recipe)
    {
        recipes.add(recipe);
    }

    public ArrayList<Recipe> getRecipes()
    {
        return recipes;
    }

    public void printRecipes()
    {
        for (int i = 0; i < recipes.size(); i++)
        {
            GUI.println("Recipe " + i + ":");
            GUI.print("  ");
            GUI.println(recipes.get(i).toString());
        }
        if (recipes.size() <= 0)
        {
            GUI.println("No recipes exist for this item.");
        }
    }

    public String getItemType()
    {
        return "Generic";
    }
}
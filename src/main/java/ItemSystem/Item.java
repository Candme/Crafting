package ItemSystem;

import java.util.ArrayList;

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
            System.out.println("Recipe " + i + ":");
            System.out.print("  ");
            System.out.println(recipes.get(i));
        }
        if (recipes.size() <= 0)
        {
            System.out.println("No recipes exist for this item.");
        }
    }

    public String getItemType()
    {
        return "Generic";
    }
}
package Chris.ItemSystem;

import java.util.ArrayList;

import Chris.*;
import Chris.ItemSystem.ItemTypes.*;
import Chris.ItemSystem.GatheringAreas.*;

public class ItemRegistry implements java.io.Serializable {
    public ArrayList<Item> itemList;
    public ArrayList<Recipe> recipeList;
    public ArrayList<Object> gatheringAreas;

    public ItemPool shopItems;

    public ItemRegistry() {
        itemList = new ArrayList<Item>();
        recipeList = new ArrayList<Recipe>();
        gatheringAreas = new ArrayList<Object>();

        createItems();
        createRecipes();
        createGatheringAreas();
    }

    private void createItems() {
        itemList.add(new Item("Log", 0));
        itemList.add(new Item("Plank", 0));
        itemList.add(new Item("Stick", 0));
        itemList.add(new Item("Plant Fibers", 0));
        itemList.add(new Item("String", 1));

        itemList.add(new Item("Primative Handle", 1));
        itemList.add(new Item("Basic Handle", 2));

        itemList.add(new Wallet());

        itemList.add(new Ore("Stone", 0));
        itemList.add(new Ore("Coal Ore", 1));
        itemList.add(new Ore("Copper Ore", 1));
        itemList.add(new Ore("Tin Ore", 1));
        itemList.add(new Ore("Iron Ore", 1));
        itemList.add(new Ore("Gold Ore", 2));
        itemList.add(new Ore("Silver Ore", 2));
        itemList.add(new Ore("Diamond Ore", 3));
        
        itemList.add(new Item("Copper Bar", 2));
        itemList.add(new Item("Tin Bar", 2));
        itemList.add(new Item("Iron Bar", 2));
        itemList.add(new Item("Gold Bar", 3));
        itemList.add(new Item("Silver Bar", 3));
        
        itemList.add(new Item("Steel Bar", 3));
        itemList.add(new Item("Bronze Bar", 4));
        itemList.add(new Item("Damascus Steel", 5));

        itemList.add(new Tool("Wooden Pickaxe", 1, 3, Tool.ToolType.PICKAXE));
        itemList.add(new Tool("Stone Pickaxe", 2, 8, Tool.ToolType.PICKAXE));
        itemList.add(new Tool("Iron Pickaxe", 3, 30, Tool.ToolType.PICKAXE));
        itemList.add(new Tool("Steel Pickaxe", 4, 85, Tool.ToolType.PICKAXE));
        itemList.add(new Tool("Damascus Steel Pickaxe", 6, 300, Tool.ToolType.PICKAXE));

        itemList.add(new Item("Iron Pickaxe Head", 3));
        itemList.add(new Item("Steel Pickaxe Head", 4));
        itemList.add(new Item("Damascus Steel Pickaxe Head", 6));

        itemList.add(new Tool("Wooden Axe", 1, 3, Tool.ToolType.AXE));
        itemList.add(new Tool("Stone Axe", 2, 7, Tool.ToolType.AXE));
        itemList.add(new Tool("Iron Axe", 3, 25, Tool.ToolType.AXE));
        itemList.add(new Tool("Steel Axe", 4, 75, Tool.ToolType.AXE));
        itemList.add(new Tool("Damascus Steel Axe", 6, 200, Tool.ToolType.AXE));

        itemList.add(new Item("Iron Axe Head", 3));
        itemList.add(new Item("Steel Axe Head", 4));
        itemList.add(new Item("Damascus Steel Axe Head", 6));

        itemList.add(new Weapon("Stone Sword", 2, 10, 2));
        itemList.add(new Weapon("Iron Sword", 3, 30, 5));
        itemList.add(new Weapon("Steel Sword", 4, 70, 8));

        itemList.add(new Item("Iron Blade", 3));
        itemList.add(new Item("Steel Blade", 4));

        itemList.add(new Tool("Furnace", 1, 10, Tool.ToolType.CRAFTING));
        itemList.add(new Tool("Anvil", 3, 30, Tool.ToolType.CRAFTING));

        itemList.add(new HealingItem("Small Healing Potion", 1, 3));
        itemList.add(new HealingItem("Healing Potion", 2, 5));
        itemList.add(new HealingItem("Large Healing Potion", 3, 10));
        itemList.add(new HealingItem("Huge Healing Potion", 4, 30));
    }

    private void createRecipes() {
        Recipe temp = new Recipe(findItem("Plank"), 4);
        temp.addIngredient(findItem("Log"), 1);

        temp = new Recipe(findItem("Stick"), 4);
        temp.addIngredient(findItem("Plank"), 2);

        temp = new Recipe(findItem("Stick"), 8);
        temp.addIngredient(findItem("Log"), 1);
        
        temp = new Recipe(findItem("String"), 1);
        temp.addIngredient(findItem("Plant Fiber"), 3);

        temp = new Recipe(findItem("Primative Handle"), 1);
        temp.addIngredient(findItem("Stick"), 2);

        temp = new Recipe(findItem("Basic Handle"), 1);
        temp.addIngredient(findItem("Primative Handle"), 2);
        temp.addIngredient(findItem("String"), 1);

        temp = new Recipe(findItem("Copper Bar"), 2);
        temp.addIngredient(findItem("Copper Ore"), 1);
        temp.addIngredient(findItem("Coal Ore"), 1);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Copper Bar"), 2);
        temp.addIngredient(findItem("Copper Ore"), 1);
        temp.addIngredient(findItem("Plank"), 3);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Tin Bar"), 2);
        temp.addIngredient(findItem("Tin Ore"), 1);
        temp.addIngredient(findItem("Coal Ore"), 1);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Tin Bar"), 2);
        temp.addIngredient(findItem("Tin Ore"), 1);
        temp.addIngredient(findItem("Plank"), 3);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Iron Bar"), 2);
        temp.addIngredient(findItem("Iron Ore"), 1);
        temp.addIngredient(findItem("Coal Ore"), 1);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Iron Bar"), 2);
        temp.addIngredient(findItem("Iron Ore"), 1);
        temp.addIngredient(findItem("Plank"), 3);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Gold Bar"), 2);
        temp.addIngredient(findItem("Gold Ore"), 1);
        temp.addIngredient(findItem("Gold Ore"), 1);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Gold Bar"), 2);
        temp.addIngredient(findItem("Gold Ore"), 1);
        temp.addIngredient(findItem("Plank"), 3);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Silver Bar"), 2);
        temp.addIngredient(findItem("Silver Ore"), 1);
        temp.addIngredient(findItem("Coal Ore"), 1);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Silver Bar"), 2);
        temp.addIngredient(findItem("Silver Ore"), 1);
        temp.addIngredient(findItem("Plank"), 3);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Steel Bar"), 1);
        temp.addIngredient(findItem("Iron Bar"), 4);
        temp.addIngredient(findItem("Coal Ore"), 6);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Bronze Bar"), 1);
        temp.addIngredient(findItem("Copper Bar"), 2);
        temp.addIngredient(findItem("Tin Bar"), 1);
        temp.addIngredient(findItem("Coal Ore"), 2);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Damascus Steel"), 1);
        temp.addIngredient(findItem("Iron Bar"), 4);
        temp.addIngredient(findItem("Steel Bar"), 4);
        temp.addCatalyst(findItem("Furnace"));

        temp = new Recipe(findItem("Wooden Pickaxe"), 1);
        temp.addIngredient(findItem("Plank"), 3);
        temp.addIngredient(findItem("Primative Handle"), 1);
        temp.addIngredient(findItem("Plant Fiber"), 1);

        temp = new Recipe(findItem("Stone Pickaxe"), 1);
        temp.addIngredient(findItem("Stone"), 3);
        temp.addIngredient(findItem("Primative Handle"), 1);
        temp.addIngredient(findItem("Plant Fiber"), 1);

        temp = new Recipe(findItem("Iron Pickaxe Head"), 1);
        temp.addIngredient(findItem("Iron Bar"), 3);
        temp.addCatalyst(findItem("Anvil"));

        temp = new Recipe(findItem("Steel Pickaxe Head"), 1);
        temp.addIngredient(findItem("Steel Bar"), 3);
        temp.addCatalyst(findItem("Anvil"));

        temp = new Recipe(findItem("Damascus Steel Pickaxe Head"), 1);
        temp.addIngredient(findItem("Damascus Steel Bar"), 3);
        temp.addCatalyst(findItem("Anvil"));

        temp = new Recipe(findItem("Iron Pickaxe"), 1);
        temp.addIngredient(findItem("Iron Pickaxe Head"), 1);
        temp.addIngredient(findItem("Basic Handle"), 1);
        temp.addIngredient(findItem("String"), 1);

        temp = new Recipe(findItem("Steel Pickaxe"), 1);
        temp.addIngredient(findItem("Steel Pickaxe Head"), 1);
        temp.addIngredient(findItem("Basic Handle"), 1);
        temp.addIngredient(findItem("String"), 1);

        temp = new Recipe(findItem("Damascus Steel Pickaxe"), 1);
        temp.addIngredient(findItem("Damascus Steel Pickaxe Head"), 1);
        temp.addIngredient(findItem("Basic Handle"), 1);
        temp.addIngredient(findItem("String"), 1);

        temp = new Recipe(findItem("Wooden Axe"), 1);
        temp.addIngredient(findItem("Plank"), 3);
        temp.addIngredient(findItem("Primative Handle"), 1);
        temp.addIngredient(findItem("Plant Fiber"), 1);

        temp = new Recipe(findItem("Stone Axe"), 1);
        temp.addIngredient(findItem("Stone"), 3);
        temp.addIngredient(findItem("Primative Handle"), 1);
        temp.addIngredient(findItem("Plant Fiber"), 1);

        temp = new Recipe(findItem("Iron Axe Head"), 1);
        temp.addIngredient(findItem("Iron Bar"), 3);
        temp.addCatalyst(findItem("Anvil"));

        temp = new Recipe(findItem("Steel Axe Head"), 1);
        temp.addIngredient(findItem("Steel Bar"), 3);
        temp.addCatalyst(findItem("Anvil"));

        temp = new Recipe(findItem("Damascus Steel Axe Head"), 1);
        temp.addIngredient(findItem("Damascus Steel Bar"), 3);
        temp.addCatalyst(findItem("Anvil"));

        temp = new Recipe(findItem("Iron Axe"), 1);
        temp.addIngredient(findItem("Iron Axe Head"), 1);
        temp.addIngredient(findItem("Basic Handle"), 1);
        temp.addIngredient(findItem("String"), 1);

        temp = new Recipe(findItem("Steel Axe"), 1);
        temp.addIngredient(findItem("Steel Axe Head"), 1);
        temp.addIngredient(findItem("Basic Handle"), 1);
        temp.addIngredient(findItem("String"), 1);
        
        temp = new Recipe(findItem("Damascus Steel Axe"), 1);
        temp.addIngredient(findItem("Damascus Steel Axe Head"), 1);
        temp.addIngredient(findItem("Basic Handle"), 1);
        temp.addIngredient(findItem("String"), 1);
        
        temp = new Recipe(findItem("Iron Blade"), 1);
        temp.addIngredient(findItem("Iron Bar"), 5);
        temp.addCatalyst(findItem("Anvil"));

        temp = new Recipe(findItem("Steel Blade"), 1);
        temp.addIngredient(findItem("Steel Bar"), 5);
        temp.addCatalyst(findItem("Anvil"));

        temp = new Recipe(findItem("Stone Sword"), 1);
        temp.addIngredient(findItem("Stone"), 4);
        temp.addIngredient(findItem("Primative Handle"), 1);
        temp.addIngredient(findItem("Plant Fiber"), 1);

        temp = new Recipe(findItem("Iron Sword"), 1);
        temp.addIngredient(findItem("Iron Blade"), 1);
        temp.addIngredient(findItem("Basic Handle"), 1);
        temp.addIngredient(findItem("String"), 1);

        temp = new Recipe(findItem("Steel Sword"), 1);
        temp.addIngredient(findItem("Steel Blade"), 1);
        temp.addIngredient(findItem("Basic Handle"), 1);
        temp.addIngredient(findItem("String"), 1);

        temp = new Recipe(findItem("Furnace"), 1);
        temp.addIngredient(findItem("Stone"), 8);

        temp = new Recipe(findItem("Anvil"));
        temp.addIngredient(findItem("Iron Bar"), 10);
    }

    private void createGatheringAreas() {
        ItemPool mine = new ItemPool();
        mine.addItem(findItem("Stone"), 75);
        mine.addItem(findItem("Coal Ore"), 25);
        mine.addItem(findItem("Copper Ore"), 10);
        mine.addItem(findItem("Iron Ore"), 10);
        mine.addItem(findItem("Tin Ore"), 10);
        mine.addItem(findItem("Gold Ore"), 5);
        mine.addItem(findItem("Silver Ore"), 5);
        mine.addItem(findItem("Diamond Ore"), 1);
        gatheringAreas.add(mine);

        ItemPool forest = new ItemPool();
        forest.addItem(findItem("Stick"), 400);
        forest.addItem(findItem("Plant Fiber"), 275);
        forest.addItem(findItem("Plank"), 50);
        forest.addItem(findItem("Log"), 25);
        forest.addItem(findItem("Wallet"), 10);
        forest.addItem(findItem("Iron Bar"), 2);
        forest.addItem(findItem("Steel Bar"), 2);
        forest.addItem(findItem("Bronze Bar"), 2);
        forest.addItem(findItem("Diamond Ore"), 1);
        gatheringAreas.add(forest);

        shopItems = new ItemPool();
        shopItems.addItem(findItem("Log"), 10);
        shopItems.addItem(findItem("Iron Bar"), 30);
        shopItems.addItem(findItem("Stone Pickaxe"), 20);
        shopItems.addItem(findItem("Gold Ore"), 10);
        shopItems.addItem(findItem("Bronze Bar"), 5);
        shopItems.addItem(findItem("Steel Bar"), 5);
        shopItems.addItem(findItem("Damascus Steel"), 1);
        shopItems.addItem(findItem("Small Healing Potion"), 20);
        shopItems.addItem(findItem("Healing Potion"), 10);
        shopItems.addItem(findItem("Large Healing Potion"), 5);
        shopItems.addItem(findItem("Huge Healing Potion"), 2);
        
        shopItems.addItem(findItem("Stone Sword"), 10);
        shopItems.addItem(findItem("Iron Sword"), 5);
        shopItems.addItem(findItem("Steel Sword"), 2);
    }

    // Returns an item by name, if not found returns a null object
    public Item findItem(String itemName) {
        for (Item i : itemList) {
            if (i.getName().toLowerCase().equals(itemName.toLowerCase())) {
                return i;
            }
            if (i.getName().toLowerCase().equals(itemName.toLowerCase() + "s"))
            {
                return i;
            }
            if ((i.getName().toLowerCase() + "s").equals(itemName.toLowerCase()))
            {
                return i;
            }
        }
        return null;
    }

    public void printItems() {
        for (Item i : itemList) {
            GUI.println(i.getName());
        }
    }

    public ItemPool getShopItems() {
        return shopItems;
    }
}
package Chris;

import java.util.*;
import java.util.function.*;

import java.io.*;
import java.nio.file.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import Chris.ItemSystem.*;
import Chris.ItemSystem.ItemRegistry;

import Chris.ItemSystem.ItemTypes.*;
import Chris.ItemSystem.GatheringAreas.*;

import Chris.ItemSystem.EnemySystem.*;

import java.time.*;

public class Runner
{
    public static Scanner input;
    public static ItemRegistry reg;

    static Instant lastShop;
    static Shop currentShop;

    static ItemPool forest;
    static ItemPool mine;

    public static GUI userInterface;

    public static Consumer<String> inputQueue = null;

    static Item tempItem = null;

    public Runner()
    {
        input = new Scanner(System.in);

        new Inventory();
        reg = Inventory.current.getItemRegistry();

        mine = (ItemPool) reg.gatheringAreas.get(0);
        forest = (ItemPool) reg.gatheringAreas.get(1);

        userInterface = new GUI();
        
        while (true)
        {
            String consoleInput = input.nextLine();
            consoleInput = consoleInput.toLowerCase();
            processInput(consoleInput);
        }
    }

    public enum HelpPages
    {
        DEFAULT,
        ALL,
        INVENTORY,
        MINING
    }

    public static void printHelpPage(HelpPages page)
    {
        switch (page)
        {
            case ALL:
                printHelpPage(HelpPages.DEFAULT);
                GUI.println();
                printHelpPage(HelpPages.INVENTORY);
                GUI.println();
                printHelpPage(HelpPages.MINING);
                break;

            case DEFAULT:
                GUI.println("    Help & Settings");
                GUI.println("───────────────────");
                GUI.println("help / ?:");
                GUI.println("    Shows a list of basic commands for settings and information");
                GUI.println();
                GUI.println("help (category) / (category)?:");
                GUI.println("    Shows a list of all commands in a specific catergory including:");
                GUI.println("    all, inventory / inv, gathering");
                GUI.println();
                GUI.println("suggest:");
                GUI.println("    Allows you to suggest something for the program");
                break;

            case INVENTORY:
                GUI.println("    Inventory & Items");
                GUI.println("─────────────────────");
                GUI.println("stats:");
                GUI.println("    Shows basic stats");
                GUI.println();
                GUI.println("inv / inventory / show inventory / list inventory:");
                GUI.println("    Shows a list of all items and their amounts in the inventory");
                GUI.println();
                GUI.println("money / credits:");
                GUI.println("    Shows the amount of credits you posess");
                GUI.println();
                GUI.println("save:");
                GUI.println("    Prompts for a name to save the current inventory to in order to load later");
                GUI.println();
                GUI.println("load:");
                GUI.println("    Prompts for a name to load a previously saved inventory");
                GUI.println();
                GUI.println("list items / item list:");
                GUI.println(
                        "    Shows a list of every item");
                GUI.println();
                GUI.println("show recipes / recipes:");
                GUI.println(
                        "    Prompts you to input an item's name, then displays the recipes of the item in order of priority when a recipe is not specified");
                GUI.println();
                GUI.println("show recipes (item) / recipes (item):");
                GUI.println("    Shows the recipes of the item whose name was given in order of priority when a recipe is not specified");
                GUI.println();
                GUI.println("craft (item):");
                GUI.println("    Attempts to craft the item with the given name");
                break;

            case MINING:
                GUI.println("    Resource Gathering");
                GUI.println("──────────────────────");
                GUI.println("forage:");
                GUI.println("    Finds random things in the forest");
                GUI.println();
                GUI.println("lumber / chop:");
                GUI.println("    If you have an axe, chops logs and uses one durability of your axe");
                GUI.println();
                GUI.println("mine:");
                GUI.println("    If you have a pickaxe, mines a random ore and uses one durability of your pickaxe");
                GUI.println();
                GUI.println("shop / store:");
                GUI.println("    Allows you to spend your credits to purchase items");
        }
    }
    
    public static void saveHandle(String saveName)
    {
        String filename = "src/main/java/Chris/SaveData/" + saveName + ".srl";

        try
        {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Inventory.current);
            oos.close();
            GUI.println("Succesfully saved under name '" + saveName + "'");
        }
        catch (IOException ex)
        {
            GUI.println("ERROR SAVING:\n" + ex);
        }
        inputQueue = null;
    }

    public static void loadHandle(String saveName)
    {
        String filename = "src/main/java/Chris/SaveData/" + saveName + ".srl";

        try
        {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fin);
            Inventory invL = (Inventory) ois.readObject();
            ois.close();
            Inventory.current = invL;
            reg = Inventory.current.getItemRegistry();
            GUI.println("Succesfully loaded file under name '" + saveName + "'");
        }
        catch (IOException ex)
        {
            GUI.println("ERROR LOADING SAVE:\n" + ex);
        }
        catch (ClassNotFoundException ex)
        {
            GUI.println("PROGRAM MISSING FILES:\n" + ex);
        }
        inputQueue = null;
    }

    public static void suggestHandle(String textToAdd)
    {
        try
        {
                Files.write(Paths.get("src/main/java/Chris/SaveData/suggestions.md"), (textToAdd + "\n").getBytes(), StandardOpenOption.APPEND);
            GUI.println("Suggestion taken!");
        }
        catch (IOException e)
        {
            GUI.println("An error occurred:");
            e.printStackTrace();
        }
        inputQueue = null;
    }
    
    public static void craft(String toCraft, int amount)
    {        
        if (reg.findItem(toCraft) != null)
        {            
            Item item = reg.findItem(toCraft);

            Recipe r = item.getRecipes().get(0);

            if (r.hasItems(amount))
            {
                for (int i = 0; i < amount; i++)
                {
                    Inventory.current.craft(r);
                    Inventory.current.addExperience(1);
                }
                
                if (amount > 1 || r.yield > 1)
                {
                    GUI.println("Crafted " + (amount * r.yield) + " " + reg.findItem(toCraft).getName() + "s");
                }
                else
                {
                    GUI.println("Crafted a " + reg.findItem(toCraft).getName());
                }
            }
            else
            {
                if (amount > 1 || r.yield > 1)
                {
                    GUI.println("You do not have enough materials to craft " + (amount * r.yield) + " " + reg.findItem(toCraft).getName() + "s");
                }
                else
                {
                    GUI.println("You do not have enough materials to craft a " + reg.findItem(toCraft).getName());
                }
            }
        }
        else
        {
            GUI.println("Item does not exist.");
        }
    }

    public static void craftCheck(String inputText)
    {
        if (inputText.length() > 4 && inputText.substring(0,5).toLowerCase().equals("craft"))
        {
            if (inputText.length() < 7)
            {
                craft(tempItem.getName(), 1);
            }
            else
            {
                try
                {
                    int number = Integer.parseInt(inputText.substring(6)); 
                    craft(tempItem.getName(), number);
                }
                catch (Exception e)
                {
                    craft(tempItem.getName(), 1);
                }
            }
        }
        else if (inputText.length() > 2 && inputText.substring(0,3).toLowerCase().equals("use"))
        {
            try 
            {
                HealingItem h = (HealingItem)tempItem;
                h.use();
            }
            catch (Exception e)
            {
                try
                {
                    Weapon w = (Weapon)tempItem;
                    w.use();
                    GUI.println("Equipped a " + tempItem.getName() + ".");
                }
                catch (Exception e2)
                {
                    GUI.println(tempItem.getName() + " cannot be used.");
                }
            }
        }
        else
        {
            tempItem = null;
            inputQueue = null;
            processInput(inputText);
        }
    }

    public static void recipes(String toFind)
    {        
        Item search = reg.findItem(toFind);
        if (search != null)
        {
            GUI.println();
            search.printRecipes();
        }
        else
        {
            GUI.println("Item not found.");
        }
    }
    
    public static void createShop()
    {
        currentShop = new Shop();
        int itemAmounts = (int)(Math.random() * 4 + 5);
        ItemPool pool = reg.getShopItems();
        for (int i = 0; i < itemAmounts; i++)
        {
            Item item = pool.getItem();
            int amount = (int)(Math.random() * 4 + 1);
            double price = (((double)((int)((Math.random() + 1.5) * 245 *(item.getTier() + 1)))) / 100) * amount + 0.5 + item.getTier() * 4;
            currentShop.addListing(item, amount, price);
        }
    }

    public static void processInput(String consoleInput)
    {
        if (inputQueue != null)
        {
            inputQueue.accept(consoleInput);
        }
        else
        {
        GUI.clear();
                consoleInput = consoleInput.toLowerCase();
                switch (consoleInput)
                {
                    // Help & Settings
                    case "save":
                    {
                        GUI.println("Choose a name for your savefile:");
                        inputQueue = Runner::saveHandle;
                        break;
                    }
                    case "load":
                    {
                        GUI.println("Enter the name of the savefile to load:");
                        inputQueue = Runner::loadHandle;  
                        break;
                    }
                    case "help all":
                    case "all?":
                    {
                        printHelpPage(HelpPages.ALL);
                        break;
                    }
                    case "help":
                    case "?":
                    {
                        printHelpPage(HelpPages.DEFAULT);
                        break;
                    }
                    case "help inventory":
                    case "inventory?":
                    case "help inv":
                    case "inv?":
                    {
                        printHelpPage(HelpPages.INVENTORY);
                        break;
                    }
                    case "help gathering":
                    case "gathering?":
                    {
                        printHelpPage(HelpPages.MINING);
                        break;
                    }
                    case "suggest":
                    {
                        GUI.println("Type your suggestion");
                        inputQueue = Runner::suggestHandle;
                        break;
                    }
                    // Inventory & Items
                    case "stats":
                    {
                        GUI.println("Health:");
                        GUI.println(Inventory.current.health + "/" + Inventory.current.maxHealth);
                    }
                    case "inv":
                    case "inventory":
                    case "show inventory":
                    case "list inventory":
                    {
                        Inventory.current.printInventoryList();
                        break;
                    }
                    case "list items":
                    case "item list":
                    {
                        reg.printItems();
                        break;
                    }
                    case "credits":
                    case "money":
                    {
                        GUI.println("You have: " + Inventory.current.getCreditsText() + " Credits.");
                        break;
                    }
                    // Resource Gathering
                    case "mine":
                    {
                        Tool pick = Inventory.current.findToolOfType(Tool.ToolType.PICKAXE);
                        if (pick != null)
                        {
                            if (Math.random() > 0.995)
                            {
                                new Fight(new Enemy("Cave Monster", 20, 3));
                            }
                            else
                            {
                                Item mined = mine.getItem();
                                Inventory.current.addItem(mined);
                                GUI.println("Mined a " + mined.getName() + ".");
                                Inventory.current.addExperience(1);
                            }
                            pick.use();
                        }
                        else
                        {
                            GUI.println("No pickaxe in inventory.");
                        }
                        break;
                    }
                    case "chop":
                    case "lumber":
                    {
                        Tool axe = Inventory.current.findToolOfType(Tool.ToolType.AXE);
                        if (axe != null)
                        {
                            if (Math.random() > 0.995)
                            {
                                new Fight(new Enemy("Ent", 10, 2));
                            }
                            else
                            {
                                int ra = (int)(Math.random() * 4 + 1);
                                Inventory.current.addItem(reg.findItem("Log"), ra);
                                Inventory.current.addExperience(1);
                                if (ra > 1)
                                {
                                    GUI.println("Chopped " + ra + " logs.");
                                }
                                else
                                {
                                    GUI.println("Chopped a log.");
                                }
                            }
                            axe.use();
                        }
                        else
                        {
                            GUI.println("No axe in inventory.");
                        }
                        break;
                    }
                    case "forage":
                    {
                        if (Math.random() > 0.965)
                        {
                            new Fight(new Enemy("Goblin", 5, 1));
                        }
                        else
                        {
                            Item gotten = forest.getItem();
                            Inventory.current.addItem(gotten);
                            GUI.println("Found a " + gotten.getName() + ".");
                            Inventory.current.addExperience(1);
                        }
                        break;
                    }
                    case "shop":
                    case "store":
                    {
                        GUI.clear();
                        if (lastShop == null)
                        {
                            lastShop = Instant.now();
                        }
                        if (currentShop == null)
                        {
                            createShop();
                        }

                        long diff = lastShop.getEpochSecond() + 600 - Instant.now().getEpochSecond();

                        if ((int)diff < 0)
                        {
                            lastShop = Instant.now();
                            createShop();
                        }

                        if ((int)diff < 0)
                        {
                            GUI.println("Shop has reset");
                        }
                        else if (diff % 60 < 10)
                        {
                            GUI.println("Shop will reset in " + (diff / 60) + ":0" + (diff % 60));
                        }
                        else
                        {
                            GUI.println("Shop will reset in " + (diff / 60) + ":" + (diff % 60));
                        }
                        currentShop.printShop();
                        GUI.println("What would you like to buy?");
                        inputQueue = currentShop::buy;
                        break;
                    }
                    // Fighting
                    case "hunt":
                    {
                        EnemyPool pool = new EnemyPool();
                        pool.addEnemy(new Enemy("Demon Lord", 250, 12, true), 1);
                        pool.addEnemy(new Enemy("Goblin", 5, 1), 400);
                        pool.addEnemy(new Enemy("Slime", 20, 1, true), 200);
                        pool.addEnemy(new Enemy("Skeleton Archer", 5, 3), 200);
                        pool.addEnemy(new Enemy("Giant", 45, 5, true), 50);

                        int enemies = 1;
                        
                        if (Math.random() > 0.95)
                        {
                            enemies = (int)((Math.random() * 3 - 1) + 2);
                        }

                        Enemy[] listE = new Enemy[enemies];

                        for (int i = 0; i < enemies; i++)
                        {
                            listE[i] = pool.getEnemy();
                        }

                        if (enemies == 1)
                        {
                            new Fight(listE[0]);
                        }
                        else
                        {
                            new GroupFight(listE);
                        }
                        
                        break;
                    }
                    // Unknown
                    default: {
                        if (reg.findItem(consoleInput) != null)
                        {
                            Item i = reg.findItem(consoleInput);
                            inputQueue = Runner::craftCheck;
                            tempItem = i;
                            GUI.println("Tier: " + i.getTier());
                            GUI.println(i.getName());
                            GUI.println(i.getItemType());
                            if (i.getItemType().equals("Tool"))
                            {
                                Tool t = (Tool)i;
                                GUI.println(t.getToolType().toString());
                                if (t.getToolType() != Tool.ToolType.CONSUMABLE)
                                {
                                    GUI.println("Durability: " + t.getMaxDurability());
                                }
                            }
                            i.printRecipes();
                        }
                        else
                        {
                            GUI.println("Command not recognised.");
                        }
                    }
                }
            }
        while (Inventory.current.hasItem(reg.findItem("Wallet")))
        {
                ((Wallet)Inventory.current.getItem("Wallet")).use();  
        }
    }
}
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

import java.time.*;

public class Runner {
    public static Scanner input;
    public static ItemRegistry reg;

    static Instant lastShop;
    static Shop currentShop;

    static ItemPool forest;
    static ItemPool mine;

    public static GUI userInterface;

    static Consumer<String> inputQueue = null;

    public Runner() {
        input = new Scanner(System.in);

        new Inventory();
        reg = Inventory.current.getItemRegistry();

        Inventory.current.addItem(reg.findItem("Stick"), 2);
        Inventory.current.addItem(reg.findItem("Plank"), 3);

        mine = (ItemPool) reg.gatheringAreas.get(0);
        forest = (ItemPool) reg.gatheringAreas.get(1);

        userInterface = new GUI();
        
        while (true) {
            String consoleInput = input.nextLine();
            consoleInput = consoleInput.toLowerCase();
            processInput(consoleInput);
       }
    }

    public enum HelpPages {
        DEFAULT,
        ALL,
        INVENTORY,
        MINING
    }

    public static void printHelpPage(HelpPages page) {
        switch (page) {
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
                GUI.println("    all, inventory / inv, mining");
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
    
    public static void saveHandle(String saveName) {
        String filename = "src/main/java/SaveData/" + saveName + ".srl";

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Inventory.current);
            oos.close();
            GUI.println("Succesfully saved under name '" + saveName + "'");
        } catch (IOException ex) {
            GUI.println("ERROR SAVING:\n" + ex);
        }
    }

    public static void loadHandle(String saveName) {
        String filename = "src/main/java/SaveData/" + saveName + ".srl";

        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fin);
            Inventory invL = (Inventory) ois.readObject();
            ois.close();
            Inventory.current = invL;
            reg = Inventory.current.getItemRegistry();
            GUI.println("Succesfully loaded file under name '" + saveName + "'");
            Inventory.current.printInventoryList();
        } catch (IOException ex) {
            GUI.println("ERROR LOADING SAVE:\n" + ex);
        } catch (ClassNotFoundException ex) {
            GUI.println("PROGRAM MISSING FILES:\n" + ex);
        }
    }

    public static void suggestHandle(String textToAdd) {
        try {
                Files.write(Paths.get("src/main/java/SaveData/suggestions.md"), (textToAdd + "\n").getBytes(), StandardOpenOption.APPEND);
            GUI.println("Suggestion taken!");
        } catch (IOException e) {
            GUI.println("An error occurred:");
            e.printStackTrace();
        }
    }
    
    public static void craft(String itemToCraft) {
        String toCraft = itemToCraft;
        
        if (reg.findItem(toCraft) != null) {
            int recipeIndex;
            
            int amount = 1;

            Item item = reg.findItem(toCraft);

            Recipe r = item.getRecipes().get(0);

            if (r.hasItems(amount))
            {
                Inventory.current.craft(r);
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
        } else {
            GUI.println("Item does not exist.");
        }
    }

    public static void recipes(String itemToRecipe) {
        String toFind;
        if (itemToRecipe == "")
        {
            GUI.println("Enter the name of the item you would like to view the recipes of");
            toFind = input.nextLine();
        }
        else
        {
            toFind = itemToRecipe;
        }
        
        
        Item search = reg.findItem(toFind);
        if (search != null) {
            GUI.println();
            search.printRecipes();
        } else {
            GUI.println("Item not found.");
        }
    }
    
    public static void createShop() {
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

    public static void processInput(String consoleInput) {
        if (inputQueue != null)
        {
            inputQueue.accept(consoleInput);
            inputQueue = null;
        }
        else
        {
        GUI.clear();
            if(consoleInput.length() >= 5 && consoleInput.substring(0,5).equals("craft"))
            {
                if (consoleInput.length() >= 6)
                {
                    String itemToCraft = consoleInput.substring(6);
                    craft(itemToCraft);
                }
                else
                {
                    craft("");
                }
            }
            else if(consoleInput.length() >= 12 && consoleInput.substring(0,12).equals("show recipes"))
            {
                if (consoleInput.length() >= 13)
                {
                    String itemToRecipe = consoleInput.substring(13);
                    recipes(itemToRecipe);
                }
                else
                {
                    recipes("");
                }
            }
            else if(consoleInput.length() >= 7 && consoleInput.substring(0,7).equals("recipes"))
            {
                if (consoleInput.length() >= 8)
                {
                    String itemToRecipe = consoleInput.substring(8);
                    recipes(itemToRecipe);
                }
                else
                {
                    recipes("");
                }
            }
            else
            {
                switch (consoleInput) {
                    // Help & Settings
                    case "save": {
                        GUI.println("Choose a name for your savefile:");
                        inputQueue = Runner::saveHandle;
                        break;
                    }
                    case "load": {
                        GUI.println("Enter the name of the savefile to load:");
                        inputQueue = Runner::loadHandle;  
                        break;
                    }
                    case "help all":
                    case "all?": {
                        printHelpPage(HelpPages.ALL);
                        break;
                    }
                    case "help":
                    case "?": {
                        printHelpPage(HelpPages.DEFAULT);
                        break;
                    }
                    case "help inventory":
                    case "inventory?":
                    case "help inv":
                    case "inv?": {
                        printHelpPage(HelpPages.INVENTORY);
                        break;
                    }
                    case "help gathering":
                    case "gathering?": {
                        printHelpPage(HelpPages.MINING);
                        break;
                    }
                    case "suggest": {
                        GUI.println("Type your suggestion");
                        inputQueue = Runner::suggestHandle;
                        break;
                    }
                    // Inventory & Items
                    case "stats": {
                        GUI.println("Health:");
                        GUI.println(Inventory.current.health + "/" + Inventory.current.maxHealth);
                    }
                    case "inv":
                    case "inventory":
                    case "show inventory":
                    case "list inventory": {
                        Inventory.current.printInventoryList();
                        break;
                    }
                    case "list items":
                    case "item list": {
                        reg.printItems();
                        break;
                    }
                    case "credits":
                    case "money": {
                        GUI.println("You have: " + Inventory.current.getCreditsText() + " Credits.");
                        break;
                    }
                    // Resource Gathering
                        case "mine": {
                            Tool pick = Inventory.current.findToolOfType(Tool.ToolType.PICKAXE);
                            if (pick != null) {
                                Item mined = mine.getItem();
                                Inventory.current.addItem(mined);
                                GUI.println("Mined a " + mined.getName() + ".");
                                pick.use();
                            } else {
                                GUI.println("No pickaxe in inventory.");
                            }
                            break;
                        }
                        case "chop":
                        case "lumber": {
                            Tool axe = Inventory.current.findToolOfType(Tool.ToolType.AXE);
                            if (axe != null) {
                                int ra = (int)(Math.random() * 5 + 2);
                                Inventory.current.addItem(reg.findItem("Log"), ra);
                                if (ra > 1)
                                {
                                    GUI.println("Chopped " + ra + " logs.");
                                }
                                else
                                {
                                    GUI.println("Chopped a log.");
                                }

                                axe.use();
                            } else {
                                GUI.println("No axe in inventory.");
                            }
                            break;
                        }
                        case "forage": {
                            Item gotten = forest.getItem();
                            Inventory.current.addItem(gotten);
                            GUI.println("Found a " + gotten.getName() + ".");
                            break;
                        }
                        case "shop":
                        case "store": {
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
                            else
                            {
                                if (diff % 60 < 10)
                                {
                                    GUI.println("Shop will reset in " + (diff / 60) + ":0" + (diff % 60));
                                }
                                else
                                {
                                    GUI.println("Shop will reset in " + (diff / 60) + ":" + (diff % 60));
                                }
                            }
                            currentShop.printShop();
                            GUI.println("What would you like to buy?");
                            inputQueue = currentShop::buy;
                            break;
                        }
        // Unknown
                    default: {
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
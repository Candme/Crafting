import java.util.*;
import java.util.function.*;

import java.io.*;
import java.nio.file.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import Color.*;

import ItemSystem.*;
import ItemSystem.ItemRegistry;

import ItemSystem.ItemTypes.*;
import ItemSystem.GatheringAreas.*;

import java.time.*;



public class Main {
    public static Scanner input;
    public static ItemRegistry reg;

    static Instant lastShop;
    static Shop currentShop;

    static ItemPool forest;
    static ItemPool mine;

    static GUI userInterface;

    static Consumer<String> inputQueue = null;

    public static void main(String[] args) {
        input = new Scanner(System.in);

        new Inventory();
        reg = Inventory.current.getItemRegistry();

        Inventory.current.addItem(reg.findItem("Plank"), 3);
        Inventory.current.addItem(reg.findItem("Stick"), 2);
        Inventory.current.printInventoryList();

        mine = (ItemPool) reg.gatheringAreas.get(0);
        forest = (ItemPool) reg.gatheringAreas.get(1);

        userInterface = new GUI();
        
        while (true) {
            userInterface.print(C.GREEN_BRIGHT);
            String consoleInput = input.nextLine();
            consoleInput = consoleInput.toLowerCase();
            userInterface.print(C.RESET);
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
        userInterface.clear();
        switch (page) {
            case ALL:
                printHelpPage(HelpPages.DEFAULT);
                userInterface.println();
                printHelpPage(HelpPages.INVENTORY);
                userInterface.println();
                printHelpPage(HelpPages.MINING);
                break;

            case DEFAULT:
                userInterface.println("    Help & Settings");
                userInterface.println("───────────────────");
                userInterface.println("help / ?:");
                userInterface.println("    Shows a list of basic commands for settings and information");
                userInterface.println();
                userInterface.println("help (category) / (category)?:");
                userInterface.println("    Shows a list of all commands in a specific catergory including:");
                userInterface.println("    all, inventory / inv, mining");
                userInterface.println();
                userInterface.println("suggest:");
                userInterface.println("    Allows you to suggest something for the program");
                break;

            case INVENTORY:
                userInterface.println("    Inventory & Items");
                userInterface.println("─────────────────────");
                userInterface.println("stats:");
                userInterface.println("    Shows basic stats");
                userInterface.println();
                userInterface.println("inv / inventory / show inventory / list inventory:");
                userInterface.println("    Shows a list of all items and their amounts in the inventory");
                userInterface.println();
                userInterface.println("money / credits:");
                userInterface.println("    Shows the amount of credits you posess");
                userInterface.println();
                userInterface.println("save:");
                userInterface.println("    Prompts for a name to save the current inventory to in order to load later");
                userInterface.println();
                userInterface.println("load:");
                userInterface.println("    Prompts for a name to load a previously saved inventory");
                userInterface.println();
                userInterface.println("list items / item list:");
                userInterface.println(
                        "    Shows a list of every item");
                userInterface.println();
                userInterface.println("show recipes / recipes:");
                userInterface.println(
                        "    Prompts you to input an item's name, then displays the recipes of the item in order of priority when a recipe is not specified");
                userInterface.println();
                userInterface.println("show recipes (item) / recipes (item):");
                userInterface.println("    Shows the recipes of the item whose name was given in order of priority when a recipe is not specified");
                userInterface.println();
                userInterface.println("craft:");
                userInterface.println("    Prompts you to input an item's name, then attempts to craft the item with a given recipe");
                userInterface.println();
                userInterface.println("craft (item):");
                userInterface.println("    Attempts to craft the item whose name was given");
                break;

            case MINING:
                userInterface.println("    Resource Gathering");
                userInterface.println("──────────────────────");
                userInterface.println("forage:");
                userInterface.println("    Finds random things in the forest");
                userInterface.println();
                userInterface.println("lumber / chop:");
                userInterface.println("    If you have an axe, chops logs and uses one durability of your axe");
                userInterface.println();
                userInterface.println("mine:");
                userInterface.println("    If you have a pickaxe, mines a random ore and uses one durability of your pickaxe");
                userInterface.println();
                userInterface.println("shop / store:");
                userInterface.println("    Allows you to spend your credits to purchase items");
        }
    }
    
    public static void saveHandle(String saveName) {
        String filename = "src/main/java/SaveData/" + saveName + ".srl";

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Inventory.current);
            oos.close();
            userInterface.println("Succesfully saved under name '" + saveName + "'");
        } catch (IOException ex) {
            userInterface.println("ERROR SAVING:\n" + ex);
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
            userInterface.println("Succesfully loaded file under name '" + saveName + "'");
            Inventory.current.printInventoryList();
        } catch (IOException ex) {
            userInterface.println("ERROR LOADING SAVE:\n" + ex);
        } catch (ClassNotFoundException ex) {
            userInterface.println("PROGRAM MISSING FILES:\n" + ex);
        }
    }

    public static void suggestHandle(String textToAdd) {
    try {
        Files.write(Paths.get("src/main/java/SaveData/suggestions.md"), (textToAdd + "\n").getBytes(), StandardOpenOption.APPEND);
        userInterface.println("Suggestion taken!");
    } catch (IOException e) {
        userInterface.println("An error occurred:");
        e.printStackTrace();
        userInterface.println(C.RESET);
    }
}
    
    public static void craft(String itemToCraft) {
        String toCraft;
        if (itemToCraft == "")
        {
            userInterface.println(C.CYAN_BRIGHT + "Enter the name of the item you would like to craft.");
            userInterface.print(C.GREEN_BRIGHT);
            toCraft = input.nextLine();
            userInterface.print(C.RESET);
        }
        else
        {
            toCraft = itemToCraft;
        }
        
        if (reg.findItem(toCraft) != null) {
            int recipeIndex;
            if (reg.findItem(toCraft).getRecipes().size() > 1) {
                userInterface.println(C.CYAN_BRIGHT + "Enter the number of the recipe you would like to use (use recipes command to view recipes)");
                userInterface.print(C.GREEN_BRIGHT);
                String recipeIndexString = input.nextLine();
                userInterface.print(C.RESET);
                try {
                    recipeIndex = Integer.parseInt(recipeIndexString);
                } catch (NumberFormatException e) {
                    recipeIndex = 0;
                }

                if (reg.findItem(toCraft).getRecipes().size() < recipeIndex + 1) {
                    recipeIndex = 0;
                }
            } else {
                recipeIndex = 0;
            }

            userInterface.println(C.CYAN_BRIGHT + "Enter the amount of times you would like to craft (or -1 to cancel)");
            userInterface.print(C.GREEN_BRIGHT);
            String recipeAmountString = input.nextLine();
            userInterface.print(C.RESET);
            int amount;
            try {
                amount = Integer.parseInt(recipeAmountString);
            } catch (NumberFormatException e) {
                amount = 1;
            }

            Item item = reg.findItem(toCraft);

            Recipe r = item.getRecipes().get(recipeIndex);

            if (r.hasItems(amount))
            {
                Inventory.current.craft(r);
                if (amount > 1 || r.yield > 1)
                {
                    userInterface.println("Crafted " + (amount * r.yield) + " " + reg.findItem(toCraft).getName() + "s");
                }
                else
                {
                    userInterface.println("Crafted a " + reg.findItem(toCraft).getName());
                }
            }
            else
            {
                if (amount > 1 || r.yield > 1)
                {
                    userInterface.println("You do not have enough materials to craft " + (amount * r.yield) + " " + reg.findItem(toCraft).getName() + "s");
                }
                else
                {
                    userInterface.println("You do not have enough materials to craft a " + reg.findItem(toCraft).getName());
                }
            }
        } else {
            userInterface.println("Item does not exist.");
        }
    }

    public static void recipes(String itemToRecipe) {
        String toFind;
        if (itemToRecipe == "")
        {
            userInterface.println(C.CYAN_BRIGHT + "Enter the name of the item you would like to view the recipes of");
            userInterface.print(C.GREEN_BRIGHT);
            toFind = input.nextLine();
            userInterface.print(C.RESET);
        }
        else
        {
            toFind = itemToRecipe;
        }
        
        
        Item search = reg.findItem(toFind);
        if (search != null) {
            userInterface.println();
            search.printRecipes();
        } else {
            userInterface.println("Item not found.");
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
                        userInterface.println("Choose a name for your savefile:");
                        inputQueue = Main::saveHandle;
                        break;
                    }
                    case "load": {
                        userInterface.println("Enter the name of the savefile to load:");
                        inputQueue = Main::loadHandle;  
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
                        userInterface.println("Type your suggestion");
                        inputQueue = Main::suggestHandle;
                        break;
                    }
                    // Inventory & Items
                    case "stats": {
                        userInterface.println("Health:");
                        userInterface.println(Inventory.current.health + "/" + Inventory.current.maxHealth);
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
                        userInterface.println("You have: " + Inventory.current.getCreditsText() + " Credits.");
                        break;
                    }
                    // Resource Gathering
                        case "mine": {
                            Tool pick = Inventory.current.findToolOfType(Tool.ToolType.PICKAXE);
                            if (pick != null) {
                                Item mined = mine.getItem();
                                Inventory.current.addItem(mined);
                                userInterface.println("Mined a " + mined.getName() + ".");
                                pick.use();
                            } else {
                                userInterface.println("No pickaxe in inventory.");
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
                                    userInterface.println("Chopped " + ra + " logs.");
                                }
                                else
                                {
                                    userInterface.println("Chopped a log.");
                                }

                                axe.use();
                            } else {
                                userInterface.println("No axe in inventory.");
                            }
                            break;
                        }
                        case "forage": {
                            Item gotten = forest.getItem();
                            Inventory.current.addItem(gotten);
                            userInterface.println("Found a " + gotten.getName() + ".");
                            break;
                        }
                        case "shop":
                        case "store": {
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
                                userInterface.println("Shop has reset");
                            }
                            else
                            {
                                if (diff % 60 < 10)
                                {
                                    userInterface.println("Shop will reset in " + (diff / 60) + ":0" + (diff % 60));
                                }
                                else
                                {
                                    userInterface.println("Shop will reset in " + (diff / 60) + ":" + (diff % 60));
                                }
                            }

                            currentShop.printShop();
                            userInterface.println(C.CYAN_BRIGHT + "What would you like to buy?");
                            userInterface.print(C.GREEN_BRIGHT);
                            String toBuy = input.nextLine();
                            userInterface.print(C.RESET);
                            currentShop.buy(toBuy);
                            break;
                        }
        // Unknown
                    default: {
                        userInterface.println("Command not recognised.");
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
import java.util.*;

import java.io.*;
import java.nio.file.*;

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

    public static void main(String[] args) {
        input = new Scanner(System.in);

        new Inventory();
        reg = Inventory.current.getItemRegistry();
        System.out.println(C.PURPLE_BOLD + "Granting starter kit." + C.RESET);

        Inventory.current.addItem(reg.findItem("Plank"), 3);
        Inventory.current.addItem(reg.findItem("Stick"), 2);
        Inventory.current.printInventoryList();

        ItemPool mine = (ItemPool) reg.gatheringAreas.get(0);
        ItemPool forest = (ItemPool) reg.gatheringAreas.get(1);

        System.out.println(C.PURPLE_BOLD + "\nType '?' or 'help' for help.\nThis program does not automatically save." + C.RESET);
        
        while (true) {
            System.out.print(C.GREEN_BRIGHT);
            String consoleInput = input.nextLine();
            consoleInput = consoleInput.toLowerCase();
            System.out.print(C.RESET);
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
                        save();
                        break;
                    }
                    case "load": {
                        load();
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
                        System.out.println(C.CYAN_BRIGHT + "Type your suggestion");
                        System.out.print(C.GREEN_BRIGHT);
                        String textToAdd = input.nextLine();
                        System.out.print(C.RESET);
                        try {
                            Files.write(Paths.get("src/main/java/SaveData/suggestions.md"), (textToAdd + "\n").getBytes(), StandardOpenOption.APPEND);
                            System.out.println("Suggestion taken!");
                        } catch (IOException e) {
                            System.out.println(C.RED + "An error occurred:");
                            e.printStackTrace();
                            System.out.println(C.RESET);
                        }
                        break;
                    }
                    // Inventory & Items
                    case "stats": {
                        System.out.println(C.BLUE_BOLD + "Health:" + C.RESET);
                        System.out.println(Inventory.current.health + "/" + Inventory.current.maxHealth);
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
                        System.out.println("You have: " + Inventory.current.getCreditsText() + " Credits.");
                        break;
                    }
                    // Resource Gathering
                        case "mine": {
                            Tool pick = Inventory.current.findToolOfType(Tool.ToolType.PICKAXE);
                            if (pick != null) {
                                Item mined = mine.getItem();
                                Inventory.current.addItem(mined);
                                System.out.println("Mined a " + mined.getName() + ".");
                                pick.use();
                            } else {
                                System.out.println(C.RED + "No pickaxe in inventory." + C.RESET);
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
                                    System.out.println("Chopped " + ra + " logs.");
                                }
                                else
                                {
                                    System.out.println("Chopped a log.");
                                }
                                
                                axe.use();
                            } else {
                                System.out.println("No axe in inventory.");
                            }
                            break;
                        }
                        case "forage": {
                            Item gotten = forest.getItem();
                            Inventory.current.addItem(gotten);
                            System.out.println("Found a " + gotten.getName() + ".");
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
                                System.out.println("Shop has reset");
                            }
                            else
                            {
                                if (diff % 60 < 10)
                                {
                                    System.out.println("Shop will reset in " + (diff / 60) + ":0" + (diff % 60));
                                }
                                else
                                {
                                    System.out.println("Shop will reset in " + (diff / 60) + ":" + (diff % 60));
                                }
                            }
                            
                            currentShop.printShop();
                            System.out.println(C.CYAN_BRIGHT + "What would you like to buy?");
                            System.out.print(C.GREEN_BRIGHT);
                            String toBuy = input.nextLine();
                            System.out.print(C.RESET);
                            currentShop.buy(toBuy);
                            break;
                        }
// Unknown
                    default: {
                        System.out.println(C.RED + "Command not recognised." + C.RESET);
                    }
                }
            }

            while (Inventory.current.hasItem(reg.findItem("Wallet")))
            {
                ((Wallet)Inventory.current.getItem("Wallet")).use();  
            }
       }
    }

    public enum HelpPages {
        DEFAULT,
        ALL,
        INVENTORY,
        MINING
    }

    public static void printHelpPage(HelpPages page) {
        System.out.println();
        switch (page) {
            case ALL:
                printHelpPage(HelpPages.DEFAULT);
                System.out.println();
                printHelpPage(HelpPages.INVENTORY);
                System.out.println();
                printHelpPage(HelpPages.MINING);
                break;

            case DEFAULT:
                System.out.println(C.PURPLE_BOLD + "    Help & Settings");
                System.out.println("───────────────────" + C.RESET);
                System.out.println(C.BLUE_BOLD + "help / ?:" + C.RESET);
                System.out.println("    Shows a list of basic commands for settings and information");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "help (category) / (category)?:" + C.RESET);
                System.out.println("    Shows a list of all commands in a specific catergory including:");
                System.out.println("    all, inventory / inv, mining");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "suggest:" + C.RESET);
                System.out.println("    Allows you to suggest something for the program");
                break;

            case INVENTORY:
                System.out.println(C.PURPLE_BOLD + "    Inventory & Items");
                System.out.println("─────────────────────" + C.RESET);
                System.out.println(C.BLUE_BOLD + "stats:" + C.RESET);
                System.out.println("    Shows basic stats");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "inv / inventory / show inventory / list inventory:" + C.RESET);
                System.out.println("    Shows a list of all items and their amounts in the inventory");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "money / credits:" + C.RESET);
                System.out.println("    Shows the amount of credits you posess");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "save:" + C.RESET);
                System.out.println("    Prompts for a name to save the current inventory to in order to load later");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "load:" + C.RESET);
                System.out.println("    Prompts for a name to load a previously saved inventory");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "list items / item list:" + C.RESET);
                System.out.println(
                        "    Shows a list of every item");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "show recipes / recipes:" + C.RESET);
                System.out.println(
                        "    Prompts you to input an item's name, then displays the recipes of the item in order of priority when a recipe is not specified");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "show recipes (item) / recipes (item):" + C.RESET);
                System.out.println("    Shows the recipes of the item whose name was given in order of priority when a recipe is not specified");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "craft:" + C.RESET);
                System.out.println("    Prompts you to input an item's name, then attempts to craft the item with a given recipe");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "craft (item):" + C.RESET);
                System.out.println("    Attempts to craft the item whose name was given");
                break;

            case MINING:
                System.out.println(C.PURPLE_BOLD + "    Resource Gathering");
                System.out.println("──────────────────────" + C.RESET);
                System.out.println(C.BLUE_BOLD + "forage:" + C.RESET);
                System.out.println("    Finds random things in the forest");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "lumber / chop:" + C.RESET);
                System.out.println("    If you have an axe, chops logs and uses one durability of your axe");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "mine:" + C.RESET);
                System.out.println("    If you have a pickaxe, mines a random ore and uses one durability of your pickaxe");
                System.out.println();
                System.out.println(C.BLUE_BOLD + "shop / store:" + C.RESET);
                System.out.println("    Allows you to spend your credits to purchase items");
        }
    }

    public static void save() {
        System.out.println(C.CYAN_BRIGHT + "Choose a name for your savefile:");
        System.out.print(C.GREEN_BRIGHT);
        String saveName = input.nextLine();
        System.out.print(C.RESET);

        String filename = "src/main/java/SaveData/" + saveName + ".srl";

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Inventory.current);
            oos.close();
            System.out.println("Succesfully saved under name '" + saveName + "'");
        } catch (IOException ex) {
            System.out.println(C.RED + "ERROR SAVING:\n" + ex + C.RESET);
        }
    }

    public static void load() {
        System.out.println(C.CYAN_BRIGHT + "Enter the name of the savefile to load:");
        System.out.print(C.GREEN_BRIGHT);
        String saveName = input.nextLine();
        System.out.print(C.RESET);

        String filename = "src/main/java/SaveData/" + saveName + ".srl";

        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fin);
            Inventory invL = (Inventory) ois.readObject();
            ois.close();
            Inventory.current = invL;
            reg = Inventory.current.getItemRegistry();
            System.out.println("Succesfully loaded file under name '" + saveName + "'");
            Inventory.current.printInventoryList();
        } catch (IOException ex) {
            System.out.println(C.RED + "ERROR LOADING SAVE:\n" + ex + C.RESET);
        } catch (ClassNotFoundException ex) {
            System.out.println(C.RED + "PROGRAM MISSING FILES:\n" + ex + C.RESET);
        }
    }

    public static void craft(String itemToCraft) {
        String toCraft;
        if (itemToCraft == "")
        {
            System.out.println(C.CYAN_BRIGHT + "Enter the name of the item you would like to craft.");
            System.out.print(C.GREEN_BRIGHT);
            toCraft = input.nextLine();
            System.out.print(C.RESET);
        }
        else
        {
            toCraft = itemToCraft;
        }
        
        if (reg.findItem(toCraft) != null) {
            int recipeIndex;
            if (reg.findItem(toCraft).getRecipes().size() > 1) {
                System.out.println(C.CYAN_BRIGHT + "Enter the number of the recipe you would like to use (use recipes command to view recipes)");
                System.out.print(C.GREEN_BRIGHT);
                String recipeIndexString = input.nextLine();
                System.out.print(C.RESET);
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

            System.out.println(C.CYAN_BRIGHT + "Enter the amount of times you would like to craft (or -1 to cancel)");
            System.out.print(C.GREEN_BRIGHT);
            String recipeAmountString = input.nextLine();
            System.out.print(C.RESET);
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
                    System.out.println("Crafted " + (amount * r.yield) + " " + reg.findItem(toCraft).getName() + "s");
                }
                else
                {
                    System.out.println("Crafted a " + reg.findItem(toCraft).getName());
                }
            }
            else
            {
                if (amount > 1 || r.yield > 1)
                {
                    System.out.println(C.RED + "You do not have enough materials to craft " + (amount * r.yield) + " " + reg.findItem(toCraft).getName() + "s" + C.RESET);
                }
                else
                {
                    System.out.println(C.RED + "You do not have enough materials to craft a " + reg.findItem(toCraft).getName() + C.RESET);
                }
            }
        } else {
            System.out.println(C.RED + "Item does not exist." + C.RESET);
        }
    }

    public static void recipes(String itemToRecipe) {
        String toFind;
        if (itemToRecipe == "")
        {
            System.out.println(C.CYAN_BRIGHT + "Enter the name of the item you would like to view the recipes of");
            System.out.print(C.GREEN_BRIGHT);
            toFind = input.nextLine();
            System.out.print(C.RESET);
        }
        else
        {
            toFind = itemToRecipe;
        }
        
        
        Item search = reg.findItem(toFind);
        if (search != null) {
            System.out.println();
            search.printRecipes();
        } else {
            System.out.println(C.RED + "Item not found." + C.RESET);
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
}
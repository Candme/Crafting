package Chris.ItemSystem.GatheringAreas;

import java.util.*;
import Chris.ItemSystem.*;
import Chris.*;

public class Shop implements java.io.Serializable
{
   ArrayList<Item> items;
   ArrayList<Integer> amounts;
   ArrayList<Double> prices;

   public Shop()
   {
      items = new ArrayList<Item>();
      amounts = new ArrayList<Integer>();
      prices = new ArrayList<Double>();
   }

   public void addListing(Item item, int amount, double price)
   {
      items.add(item);
      amounts.add(amount);
      prices.add(price);
   }

   public void buy(String item)
   {
      Item toBuy = Inventory.current.registry.findItem(item);
      boolean itemFlag = false;
      if (toBuy != null)
      {
         int index = -1;
         for (int i = 0; i < items.size(); i++)
         {
            if (items.get(i) != null && toBuy.getName().equals(items.get(i).getName()))
            {
               index = i;
               if (Inventory.current.getCredits() >= prices.get(index))
               {
                  Inventory.current.addItem(items.get(index), amounts.get(index));
                  Inventory.current.addCredits(-1 * prices.get(index));

                  if (amounts.get(index) > 1)
                  {
                     Main.userInterface.println("Bought " + amounts.get(index) + " " + items.get(index).getName() + "s");
                  }
                  else
                  {
                     Main.userInterface.println("Bought a " + items.get(index).getName());
                  }
                  Main.userInterface.println("You have " + Inventory.current.getCreditsText() + " credits remaining");

                  items.set(index, null);
                  amounts.set(index, -1);
                  prices.set(index, -1.0);
                  itemFlag = false;
                  break;
               }
               else
               {
                  itemFlag = true;
               }
            }
         }
         if (index != -1)
         {
            if (itemFlag)
            {
               Main.userInterface.println("You do not have enough money");
            }
         }
         else
         {
            Main.userInterface.println("Item not in stock"); 
         }
      }
      else
      {
         Main.userInterface.println("Item does not exist");
      }
   }

   public void printShop()
   {
      Main.userInterface.println("Shop:");
      for (int i = 0; i < items.size(); i++)
      {
         if (items.get(i) != null)
         {
            if (amounts.get(i) > 1)
            {
               Main.userInterface.println(amounts.get(i) + "x " + items.get(i).getName() + "s\n    ₡ " + Inventory.current.getCreditsText(prices.get(i)));
            }
            else
            {
               Main.userInterface.println("1x " + items.get(i).getName() + "\n    ₡ " + Inventory.current.getCreditsText(prices.get(i)));
            }
         }
         else
         {
            Main.userInterface.println("Sold!\n    ₡ 0.00");
         }
      }
   }

   
}
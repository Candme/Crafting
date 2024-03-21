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
                     GUI.println("Bought " + amounts.get(index) + " " + items.get(index).getName() + "s");
                  }
                  else
                  {
                     GUI.println("Bought a " + items.get(index).getName());
                  }
                  GUI.println("You have " + Inventory.current.getCreditsText() + " credits remaining");

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
               GUI.println("You do not have enough money");
            }
         }
         else
         {
            GUI.println("Item not in stock"); 
         }
      }
      else
      {
         GUI.println("Item does not exist");
      }
   }

   public void printShop()
   {
      GUI.println("Shop:");
      for (int i = 0; i < items.size(); i++)
      {
         if (items.get(i) != null)
         {
            if (amounts.get(i) > 1)
            {
               GUI.println(amounts.get(i) + "x " + items.get(i).getName() + "s\n    ₡ " + Inventory.current.getCreditsText(prices.get(i)));
            }
            else
            {
               GUI.println("1x " + items.get(i).getName() + "\n    ₡ " + Inventory.current.getCreditsText(prices.get(i)));
            }
         }
         else
         {
            GUI.println("Sold!\n    ₡ 0.00");
         }
      }
   }
}
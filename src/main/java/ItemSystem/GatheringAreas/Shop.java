package ItemSystem.GatheringAreas;

import java.util.*;
import ItemSystem.*;

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
                     System.out.println("Bought " + amounts.get(index) + " " + items.get(index).getName() + "s");
                  }
                  else
                  {
                     System.out.println("Bought a " + items.get(index).getName());
                  }
                  System.out.println("You have " + Inventory.current.getCreditsText() + " credits remaining");

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
               System.out.println("You do not have enough money");
            }
         }
         else
         {
            System.out.println("Item not in stock"); 
         }
      }
      else
      {
         System.out.println("Item does not exist");
      }
   }

   public void printShop()
   {
      System.out.println("Shop:");
      for (int i = 0; i < items.size(); i++)
      {
         if (items.get(i) != null)
         {
            if (amounts.get(i) > 1)
            {
               System.out.println(amounts.get(i) + "x " + items.get(i).getName() + "s\n    ₡ " + Inventory.current.getCreditsText(prices.get(i)));
            }
            else
            {
               System.out.println("1x " + items.get(i).getName() + "\n    ₡ " + Inventory.current.getCreditsText(prices.get(i)));
            }
         }
         else
         {
            System.out.println("Sold!\n    ₡ 0.00");
         }
      }
   }

   
}
package Chris.ItemSystem.GatheringAreas;

import java.util.ArrayList;

import Chris.ItemSystem.*;

import Chris.ItemSystem.ItemTypes.*;

public class ItemPool implements java.io.Serializable 
{
   private ArrayList<Item> items;
   private ArrayList<Integer> itemWeights;
   private int totalWeight;
   
   public ItemPool()
   {
      items = new ArrayList<Item>();
      itemWeights = new ArrayList<Integer>();
      totalWeight = 0;
   }

   public void addItem(Item item, int weight)
    {
      if (item != null && weight > 0)
      {
         items.add(item);
         itemWeights.add(weight);
         totalWeight += weight;
      }
    }
   
   public Item getItem()
   {
      int rand = (int)(Math.random() * totalWeight) + 1;
      for (int i = 0; i < items.size() - 1; i++)
      {
            if (itemWeights.get(i) < rand)
            {
                  rand -= itemWeights.get(i);
            }
            else
            {
                  return items.get(i);
            }
      }
      return items.get(items.size() - 1);
   }
}
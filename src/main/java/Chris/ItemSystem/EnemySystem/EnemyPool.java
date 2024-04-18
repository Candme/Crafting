package Chris.ItemSystem.EnemySystem;

import java.util.ArrayList;

import Chris.ItemSystem.*;
import Chris.ItemSystem.ItemTypes.*;

public class EnemyPool implements java.io.Serializable 
{
   private ArrayList<Enemy> items;
   private ArrayList<Integer> itemWeights;
   private int totalWeight;
   
   public EnemyPool()
   {
      items = new ArrayList<Enemy>();
      itemWeights = new ArrayList<Integer>();
      totalWeight = 0;
   }

   public void addEnemy(Enemy item, int weight)
    {
      if (item != null && weight > 0)
      {
         items.add(item);
         itemWeights.add(weight);
         totalWeight += weight;
      }
    }
   
   public Enemy getEnemy()
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
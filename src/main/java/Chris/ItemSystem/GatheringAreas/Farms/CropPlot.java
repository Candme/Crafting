package Chris.ItemSystem.GatheringAreas.Farms;

import Chris.ItemSystem.ItemTypes.Crop;

import java.time.*; 

public class CropPlot implements java.io.Serializable 
{
   Instant timePlanted;
   Crop cropPlanted;
   int amount;

   public CropPlot(Crop crop)
   {
      amount = 1;
      cropPlanted = crop;
      timePlanted = Instant.now();
   }

   public void calculateGrowth()
   {
      Instant now = Instant.now();
      int difference = (int)(now.getEpochSecond() - timePlanted.getEpochSecond());
      difference /= 60;
      amount = (int)(cropPlanted.getGrowthTime() / difference + 1);
      if (amount > cropPlanted.getMaxAmount())
      {
         amount = cropPlanted.getMaxAmount();
      }
   }

   public void plant(Crop crop)
   {
      amount = 1;
      cropPlanted = crop;
      timePlanted = Instant.now();
   }

   public int getAmount()
   {
      return amount;
   }

   public Crop getCrop()
   {
      return cropPlanted;
   }

   public void clear()
   {
      amount = 0;
      cropPlanted = null;
      timePlanted = null;
   }
}
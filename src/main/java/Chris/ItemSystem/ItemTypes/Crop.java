package Chris.ItemSystem.ItemTypes;

import Chris.ItemSystem.*;


public class Crop extends Item
{
  double minutesToGrow;
  int maxAmount;

  public Crop(String itemName, int itemTier, double growthTime, int maxCrops)
  {
    super(itemName, itemTier);
    minutesToGrow = growthTime;
    maxAmount = maxCrops;
  }

  public String getItemType()
  {
    return "Crop";
  }

  public double getGrowthTime()
  {
    return minutesToGrow;
  }

  public int getMaxAmount()
  {
    return maxAmount;
  }
}
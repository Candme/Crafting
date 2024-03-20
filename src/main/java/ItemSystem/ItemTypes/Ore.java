package ItemSystem.ItemTypes;

import ItemSystem.*;

public class Ore extends Item
{
    public Ore(String itemName, int itemTier)
    {
        super(itemName, itemTier);
    }

    public String getItemType()
    {
        return "Ore";
    }
}
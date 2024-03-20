package ItemSystem.GatheringAreas;

import java.util.ArrayList;

import ItemSystem.*;

import ItemSystem.ItemTypes.*;
import ItemSystem.GatheringAreas.Farms.*;

public class Farm implements java.io.Serializable 
{
    CropPlot[][] plots = new CropPlot[1][1];

    public Farm(int size)
    {
        plots = new CropPlot[size][size];
    }

    public void harvest(int x, int y)
    {
        Crop crop = plots[y][x].getCrop();
        int amount = plots[y][x].getAmount();
        Inventory.current.addItem(crop, amount);
        plots[y][x].clear();
    }

    public void plant(int x, int y, Crop crop)
    {
        if (Inventory.current != null && Inventory.current.removeItem(crop))
        {
            plots[y][x].plant(crop);
        }
    }
}
package Chris.ItemSystem.ItemTypes;

import Chris.*;
import Chris.ItemSystem.*;

public class Weapon extends Tool {
    public int amount;
    
    public Weapon(String itemName, int itemTier, int maxDurability, int damageAmount) {
        super(itemName, itemTier, maxDurability, ToolType.WEAPON);
        amount = damageAmount;
    }

    public void use()
    {
        if (Inventory.current.equipped != this)
        {
            Inventory.current.equipped = this;
        }
        else
        {
            super.use();
        }
    }
}
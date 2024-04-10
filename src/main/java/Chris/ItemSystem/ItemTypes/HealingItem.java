package Chris.ItemSystem.ItemTypes;

import Chris.*;
import Chris.ItemSystem.*;
import Chris.ItemSystem.Item;

public class HealingItem extends Tool {
    int amount;
    
    public HealingItem(String itemName, int itemTier, int healingAmount) {
        super(itemName, itemTier, 1, ToolType.CONSUMABLE);
        amount = healingAmount;
    }

    public void use()
    {
        Inventory.current.health += amount;
        if (Inventory.current.health > Inventory.current.maxHealth)
        {
            Inventory.current.health = Inventory.current.maxHealth;
        }
        super.use();
    }
}
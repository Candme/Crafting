package ItemSystem.ItemTypes;

import ItemSystem.*;

public class Tool extends Item {
    int maxDurability;
    int durability;
    ToolType toolType;

    public Tool(String itemName, int itemTier, int maximumDurabilty) {
        super(itemName, itemTier);
        maxDurability = maximumDurabilty;
        durability = maxDurability;
        toolType = ToolType.GENERIC;
    }

    public Tool(String itemName, int itemTier, int maximumDurabilty, ToolType typeOfTool) {
        super(itemName, itemTier);
        maxDurability = maximumDurabilty;
        durability = maxDurability;
        toolType = typeOfTool;
    }

    public void use() {
        if (Math.random() < 0.95)
        {
            durability--;
        }
        if (durability <= 0) {
            durability = maxDurability;
            System.out.println("A " + super.getName() + " broke!");
            Inventory.current.removeItem(this);
        }
    }

    public ToolType getToolType() {
        return toolType;
    }

    @Override
    public String getItemType()
    {
        return "Tool";
    }

    public enum ToolType {
        GENERIC,
        CRAFTING,
        PICKAXE,
        AXE
    }
}
package Chris.ItemSystem.ItemTypes;

import Chris.ItemSystem.*;

public class Wallet extends Tool {
  public Wallet() {
      super("Wallet", -1, -1);
  }

  public void use() {
    double amount = (((double)((int)((Math.random() + 1.5) * 245))) / 100) + 0.5;
    Inventory.current.addCredits(amount);
    System.out.println("Got " + Inventory.current.getCreditsText(amount) + " credits!");
    Inventory.current.removeItem(this);
  }

  
}
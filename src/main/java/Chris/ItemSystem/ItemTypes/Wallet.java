package Chris.ItemSystem.ItemTypes;

import Chris.*;
import Chris.ItemSystem.*;

public class Wallet extends Tool {
  public Wallet() {
      super("Wallet", -1, -1);
  }

  public void use() {
    double amount = (((double)((int)((Math.random() + 1.5) * 245))) / 100) + 0.5;
    Inventory.current.addCredits(amount);
    GUI.println("Got " + Inventory.current.getCreditsText(amount) + " credits!");
    Inventory.current.removeItem(this);
  }
    public void use(boolean announce) {
        double amount = (((double)((int)((Math.random() + 1.5) * 245))) / 100) + 0.5;
        Inventory.current.addCredits(amount);
        if (announce)
        {
            GUI.println("Got " + Inventory.current.getCreditsText(amount) + " credits!");
        }
        Inventory.current.removeItem(this);
      }

  
}
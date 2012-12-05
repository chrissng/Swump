package core.multiplayer.powerups;

import core.Player;


/** @author Kangwei */

public interface PowerUp {
    
    enum Type { TRY, TAKE_OVER, HINT, TIME };
    public boolean use();
    public Type getType();
    public void setUser(Player p);
}

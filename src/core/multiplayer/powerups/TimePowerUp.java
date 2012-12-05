package core.multiplayer.powerups;

import core.Player;
import core.Timer;

/** @author Kangwei */

public class TimePowerUp implements PowerUp{

    private Player player;
    
    public TimePowerUp(){
        
    }
    public Type getType() {
        return PowerUp.Type.TIME;
    }

    public void setUser(Player p) {
        this.player = p;
        
    }

    @Override
    public boolean use() {
        if(player!=null){
            Timer.getInstance().addToTime(-10000);
            return true;
        }
        return false;
    }

}

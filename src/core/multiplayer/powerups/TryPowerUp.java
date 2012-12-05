package core.multiplayer.powerups;

import core.Player;
import core.multiplayer.powerups.PowerUp.Type;


/** @author Kangwei */

public class TryPowerUp implements PowerUp {

	private Player player;
	
	public TryPowerUp(){}
	
	public Type getType(){
	    return PowerUp.Type.TRY;
	}
	
	public boolean use() {
		int newTurn = player.getTurnTries() + 1;
		player.setTurnTries(newTurn);
		return true;
	}

    @Override
    public void setUser(Player p) {
        this.player=p;
        
    }
	

}

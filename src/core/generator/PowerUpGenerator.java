package core.generator;
import core.multiplayer.powerups.HintPowerUp;
import core.multiplayer.powerups.PowerUp;
import core.multiplayer.powerups.TakeOverPowerUp;
import core.multiplayer.powerups.TimePowerUp;
import core.multiplayer.powerups.TryPowerUp;


/** @author Kangwei */

public class PowerUpGenerator {

    public static PowerUp generate(){
        int random = RandomGenerator.getRandomPowerUpVal();
        switch(random){
            case 0: return (PowerUp) new TakeOverPowerUp();
            case 1: return (PowerUp) new HintPowerUp(); 
            case 2: return (PowerUp) new TryPowerUp(); 
            case 3: return (PowerUp) new TimePowerUp();
        }
        return null;
    }
}

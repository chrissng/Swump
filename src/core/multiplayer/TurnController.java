package core.multiplayer;

import core.SudokuLogic;
import core.Timer;


/** @author Kangwei */


public class TurnController implements Runnable {
    static Timer timer = Timer.getInstance();
    public static final TurnController INSTANCE = new TurnController();
    
    
    protected TurnController(){}
    
    public static TurnController getInstance(){
        return TurnController.INSTANCE;
    }

    /** checks if timeLimit has been reached or exceeded */
    public boolean checkExceed(long elapsedTime, long timeLimit){
        if(elapsedTime>=timeLimit) {
            return true;
        }
        return false;
    }
    public void run(){
        while(!Thread.interrupted()){
            if(checkExceed(timer.getElapsedTime(), SudokuLogic.getInstance().getTurnTime())){                
                SudokuLogic.getInstance().goNextTurn();    
                timer.startTimer();
            }
        }        
    }    
}

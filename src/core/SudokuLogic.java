package core;

import java.awt.Color;
import java.util.LinkedList;

import core.generator.PowerUpGenerator;
import core.generator.RandomGenerator;
import core.multiplayer.TurnController;
import core.multiplayer.powerups.PowerUp;
import core.storage.Storage;

/** @author Kangwei */

public class SudokuLogic {

    private LinkedList<Player> players = new LinkedList<Player>();

    public Puzzle puzzle = null;
    private Player currentPlayer = null;
    private Player winner = null;

    private static SudokuLogic INSTANCE = null;

    public final int IN_PLAY = 0;
    public final int COMPLETED = 1;
    public final int PRE_GAME = -1;

    public final int SINGLEPLAY = 0;
    public final int MULTIPLAY = 1;

    private int gameState = PRE_GAME;
    private int mode = SINGLEPLAY;

    private int allowableHints;
    private int difficulty;
    private int allowableTries;
    private int penaltyTime = 10000;
    private long turnTime = 30000;
    private Thread turnHandler;
    private int posX;
    private int posY;

    
    protected SudokuLogic(){}
    
    public static SudokuLogic getInstance() {
        if(INSTANCE==null){
            INSTANCE = new SudokuLogic();
        }
        return INSTANCE;
    }
    
    
    /**
     * Starts a new single player game
     * 
     * @param difficulty
     * @return Puzzle
     * 
     */
    public Puzzle startNewGame(int difficulty) {
        if (getMode() == MULTIPLAY){
            turnHandler.interrupt();
        }
        setMode(SINGLEPLAY);
        setState(IN_PLAY);
        setDifficulty(difficulty);
        resetTimer();
        startTimer();
        currentPlayer = new Player();
        players.add(currentPlayer);
        puzzle = Storage.getInstance().retrievePuzzle(difficulty);

        return puzzle;
    }

    /**
     * Starts a new multiplayer game
     * 
     * @param difficulty Difficulty of puzzle
     * @param turnTime Time limit for a turn
     * @param tries Number of tries allowed
     * @return instance of retrieved Puzzle object
     */
    public Puzzle startNewGame(int difficulty, int turnTime, int tries) {
        if ((getMode()) == SINGLEPLAY) {
            turnHandler = new Thread(TurnController.getInstance());
            turnHandler.start();
        }
        setState(IN_PLAY);
        setMode(MULTIPLAY);
        setDifficulty(difficulty);
        resetTimer();
        startTimer(); 
        setTurnTime((long)turnTime * 1000);
        setTurnTries(tries);
        puzzle = Storage.getInstance().retrievePuzzle(difficulty);
        currentPlayer = players.peek();
        currentPlayer.addPowerup(PowerUpGenerator.generate()); 
        return puzzle;
    }

    /**
     * Initializes player specifications
     * 
     * @param name Name of player
     * @param color Color to represent player
     */
    public void initializePlayer(String name, Color color) {
        if ((getState() == COMPLETED) || 
                (getState() == IN_PLAY)) {
            players.clear();
            setState(PRE_GAME);
        }
        Player newPlayer = new Player(name, color);
        players.add(newPlayer);
    }

    /**
     * Current player uses a powerup
     * @param type Type of PowerUp
     */
    public boolean usePowerUp(PowerUp.Type type) {
        Player currentPlayer = getCurrentPlayer();
        int remaining=0;
        switch(type) {
            case HINT : remaining = currentPlayer.numHintPowerUp; break;
            case TAKE_OVER : remaining = currentPlayer.numTakeOverPowerUp; break;
            case TRY : remaining = currentPlayer.numTryPowerUp; break;
            case TIME : remaining = currentPlayer.numTimePowerUp; break;
        }
        if(remaining>0){
            if(type==PowerUp.Type.TAKE_OVER) {
                currentPlayer.takeOver(getTargetPosX(), getTargetPosY());
            }
            else{
                return currentPlayer.usePowerUp(type);
            }
        }
        return false;
    }

    /**
     * 
     * @return list of players who are currently playing
     */
    public LinkedList<Player> retrieveListOfPlayers() {
        LinkedList<Player> playerList = new LinkedList<Player>();
        try{
            for (int i = 0; i < players.size(); i++)
                playerList.add(players.get(i));       
        } 
        catch(NullPointerException e){
            e.printStackTrace();
        }
        return playerList;
    }

    /**
     * Retrieves the number of allowable hints a currentPlayer can use
     * 
     * @return number of hints
     */
    public int getNumOfHints() {
        return allowableHints;
    }

    
    public void setTargetPosX(int x){
        posX = x;
        
    }
    public void setTargetPosY(int y){
        posY = y;
    }
    
    public int getTargetPosX(){
        return posX;
    }
    
    public int getTargetPosY(){
        return posY;
    }
    /**
     * Sets the number of allowable hints a currentPlayer can use
     * 
     * @param hints
     */
    public void setNumOfHints(int hints) {
        allowableHints = hints;
        for (Player p : players) {
            p.setNumHints(hints);
        }
    }

    /**
     * Start next currentPlayer's turn
     * 
     * @return <b>true</b> if game still in play. <b>false</b> if no longer in
     *         play.
     */
    public boolean goNextTurn() {
        try{
            if (gameState == IN_PLAY) {
                currentPlayer = players.removeFirst();
                resetCurrentPlayerTries();
                players.add(currentPlayer);
                setCurrentPlayer(players.peek());
                currentPlayer.addPowerup(PowerUpGenerator.generate());
                resetTimer();
                startTimer();
                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Insert pencil mark
     * 
     * @param x X Coordinate of Puzzle
     * @param y Y Coordinate of Puzzle
     * @param value Value to be inserted
     */
    public void insertPencilMark(int x, int y, int value) {
        puzzle.addPencilMark(x, y, value);
    }

    /**
     * Refills the current currentPlayer's tries Method called after currentPlayer's turn has
     * ended
     */
    private void resetCurrentPlayerTries() {
        currentPlayer.setTurnTries(getTurnTries());
    }

    /**
     * Called each time currentPlayer fills a cell
     * 
     * @param x X coordinate of puzzle
     * @param y Y coordinate of puzzle
     * @param val value inserted
     * @return <b>true</b> if value was correct. <b>false</b> if value was wrong
     */

    public boolean validateCell(int x, int y, int val) {
        boolean isValid = false;
        int numTurns = currentPlayer.getTurnTries();

        int[][] solution = puzzle.getSystemAnswers();
        if (solution[x][y] == val) {
            isValid = true;
        } else
            isValid = false;

        if (isValid) {
            puzzle.setUserAnswers(x, y, val);
            switch (mode) {
                case SINGLEPLAY:
                    break;
                case MULTIPLAY:
                    currentPlayer.setTurnTries(--numTurns);
                    puzzle.setOwner(x, y, currentPlayer);
                    currentPlayer.getScore().increaseCellsOwned();
                    break;
            }
        } else if (!isValid) {
            switch (mode) {
                case SINGLEPLAY:
                    addToTime(penaltyTime);
                    break;
                case MULTIPLAY:
                    currentPlayer.setTurnTries(--numTurns);
                    break;
            }
        }
        
        if (mode == MULTIPLAY && currentPlayer.getTurnTries() == 0) {
            goNextTurn();
        }
        
        handleWin();
        
        return isValid;
    }
    
    public void handleWin(){
        if (allCellsFilled() == true) {
            setState(COMPLETED);
            if (getMode()==MULTIPLAY) {
                determineWinner();
                turnHandler.interrupt();
            } 
            else {
                setWinner(currentPlayer);
                resetTimer();
            }
            Storage.getInstance().deletePuzzle(puzzle.getPuzzleID());
        }
    }
    /** 
     * @param increasedTime Time added
     */
    private void addToTime(int increasedTime) {
        Timer.getInstance().addToTime(increasedTime);
    }

    /** Resets the Timer **/
    private void resetTimer() {
        Timer.getInstance().resetTimer();
    }

    /**
     * @param currentPlayer Player instance determined as the winner
     */
    public void setWinner(Player player) {
        winner = player;
    }

    /**
     * @return Player instance determined as the winner
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * This method chooses the winner with the most number of cells owned
     */
    private void determineWinner() {
        for (int i = 0; i < (players.size()-1); i++) {
            int currentPlayerOwnedCells = players.get(i).getScore().getCellsOwned();
            int nextPlayerOwnedCells = players.get(i+1).getScore().getCellsOwned();
            if (currentPlayerOwnedCells > nextPlayerOwnedCells) {
                setWinner(players.get(i));
            } else {
                setWinner(players.get(i+1));
            }
        }
    }

    /**
     * @return Remaining cells yet to be filled
     */
    public int getRemainingCells() {
        int cellsRemaining = 0;
        int[][] userAns = puzzle.getUserAnswers();

        for (int i = 0; i < puzzle.getUserAnswers().length; i++) {
            for (int j = 0; j < puzzle.getUserAnswers().length; j++) {
                if (userAns[i][j] == 0) {
                    cellsRemaining++;
                }
            }
        }
        return cellsRemaining;
    }

    /**
     * @return Number of cells already filled
     */
    public boolean allCellsFilled() {
        if (getRemainingCells() == 0)
            return true;
        else
            return false;
    }

    /**
     * Displays a hint
     */   
    public void showHint() {
        
        int[][] userAns = puzzle.getUserAnswers();
        int[][] solution = puzzle.getSystemAnswers();
        int row = RandomGenerator.getRandomRow();
        int col = RandomGenerator.getRandomCol();
        
        while (userAns[row][col] != 0) {
            row = RandomGenerator.getRandomRow();
            col = RandomGenerator.getRandomCol();
        }
        int numberOfHints = currentPlayer.getNumHints();
        if (userAns[row][col] == 0 && numberOfHints > 0) {     
            
            puzzle.setUserAnswers(row, col, solution[row][col]);
            currentPlayer.setNumHints(numberOfHints - 1);        

        }     
        handleWin();
    }
    
    public void updateScoreboard(String playerName, long time){
        if(getMode()==SINGLEPLAY){
            Storage.getInstance().updateScore(playerName, getDifficulty(), time);
        }
    }

    public String[][][] retrieveScoreboard() {
        String[][][] scores = Storage.getInstance().retrieveScoreboard();
        return scores;
    }

    public boolean saveGame(String gameName) {
        return Storage.getInstance().saveGame(gameName, puzzle,
                getElapsedTime());
    }

    public String[][] loadGameList() {
        return Storage.getInstance().loadGameList();
    }

    public Puzzle loadGame(String gameName, String timeStamp) {
        if(turnHandler!=null) {
            turnHandler.interrupt();
        }
        
        Game savedGame = Storage.getInstance().loadGame(gameName, timeStamp);
        puzzle = savedGame.getPuzzle();
        if(currentPlayer==null){
            currentPlayer = new Player();
            players.add(currentPlayer);
        }
        
        if (puzzle != null) {   
            long timeOffset = savedGame.getTimeElapsed();
            resetTimer();
            startTimer();
            Timer.getInstance().addToTime(timeOffset);
            setState(IN_PLAY);
            setMode(SINGLEPLAY);
            return puzzle;
        }

        return null;
    }

    public void deleteGame(String gameName, String timeStamp) {
        Storage.getInstance().deleteGame(gameName,timeStamp);
    }

    /**
     * Sets state of current game 
     * @param: state of game
     */
    public void setState(int state) {
        switch (state) {
        case (IN_PLAY):
            gameState = IN_PLAY; break;
        case (COMPLETED):
            gameState = COMPLETED; break;
        case (PRE_GAME):
            gameState = PRE_GAME; break;
        }
    }

    /**
     * @return: Current game state
     */
    public int getState() {
        return gameState;
    }

    /**
     * @param mode Sets game mode
     */
    public void setMode(int mode) {
        switch (mode) {
        case SINGLEPLAY:
            this.mode = SINGLEPLAY;
            break;
        case MULTIPLAY:
            this.mode = MULTIPLAY;
            break;
        }
    }

    public int getMode() {
        return mode;
    }

    /** @param: Player instance of current currentPlayer **/
    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    /** @return current currentPlayer */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void startTimer() {
        new Thread(Timer.getInstance()).start();
    }

    public long getElapsedTime() {
        return Timer.getInstance().getElapsedTime();
    }

    /**
     * Sets time limit of a turn
     * @param time
     */
    public void setTurnTime(long time) {
        this.turnTime = time;
    }

    /**
     * Gets time limit of a turn
     * @return specified time limit
     */
    
    public long getTurnTime() {
        return turnTime;
    }
    
    private void setTurnTries(int tries) {
        allowableTries = tries;
        for(Player p: players){
            p.setTurnTries(allowableTries);
        }
    }

    private int getTurnTries() {
        return allowableTries;
    }

    public void setTimePenalty(int penalty) {
        penaltyTime = penalty;
    }
    
    
    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }
    
    public int getDifficulty(){
        return difficulty;
    }


}

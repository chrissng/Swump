package core.multiplayer.powerups;

import core.Cell;
import core.Player;
import core.Puzzle;
import core.SudokuLogic;
import core.generator.RandomGenerator;
import core.multiplayer.powerups.PowerUp.Type;


/** @author Kangwei */

public class HintPowerUp implements PowerUp {

	private Player player;
	private Puzzle puzzle;
	
	public HintPowerUp(){}
	
	public HintPowerUp(Player player) {
		this.player = player;
	}
	
	public boolean use() {
	    puzzle = SudokuLogic.getInstance().puzzle;
	    Cell[][] grid = puzzle.getGrid();
	    int[][] userAns = puzzle.getUserAnswers();
        int[][] solution = puzzle.getSystemAnswers();
        int row = RandomGenerator.getRandomRow();
        int col = RandomGenerator.getRandomCol();
        
        while (userAns[row][col] != 0) {
            row = RandomGenerator.getRandomRow();
            col = RandomGenerator.getRandomCol();
        }
        
        if (userAns[row][col] == 0) {     
            puzzle.setUserAnswers(row, col, solution[row][col]);        
            grid[row][col].setOwner(player);
            player.getScore().increaseCellsOwned();
            puzzle.setGrid(grid);  
        }     
        SudokuLogic.getInstance().handleWin();
		return true;
	}
	
	public Type getType(){
	    return PowerUp.Type.HINT;
	}

    @Override
    public void setUser(Player p) {
        this.player=p;
        
    }

}

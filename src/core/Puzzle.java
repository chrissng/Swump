package core;



public class Puzzle {
    private Cell[][] grid;
    private final long puzzleID;
    private int difficulty;
    
    public Puzzle(long id, int diff, String solution, String givens) {
        grid = new Cell[9][9];
        puzzleID = id;
        difficulty = diff;
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int value = Integer.parseInt(solution.charAt((9 * i) + j) + "");
                boolean isFilled;
                if (Integer.parseInt(givens.charAt((9 * i) + j) + "") == 0) {
                    isFilled = false;
                } else {
                    isFilled = true;
                }

                grid[i][j] = new Cell(value, isFilled);
            }
        }
    }
    
    public int getDifficulty() {
        return difficulty;
    }
    
    public long getPuzzleID() {
        return puzzleID;
    }

    public int[][] getSystemAnswers() {
        int[][] cellValue = new int[9][9];
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[i].length;j++){
                cellValue[i][j] = grid[i][j].getValue();
            }
        }
        return cellValue;
    }

    public int getSystemAnswers(int x, int y) {
        return grid[x][y].getValue();
    }
    
    public int[][] getUserAnswers() {
        int[][] userAnswers = new int[9][9];
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[i].length;j++){
                userAnswers[i][j] = grid[i][j].isFilled() ? grid[i][j].getValue() : 0;
            }
        }
        return userAnswers;
    }
    
    public int getUserAnswer(int x, int y) {
        return grid[x][y].isFilled() ? grid[x][y].getValue() : 0;
    }
    
    public Player getOwner(int x, int y) {
        return grid[x][y].getOwner();
    }
    
    public void setOwner(int x, int y, Player player){
        grid[x][y].setOwner(player);
    }
    
    public int[][][] getPencilMarks() {
        int[][][] pencilMarks = new int[9][9][9];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                pencilMarks[i][j] = grid[i][j].getPencilMarks();
            }
        }
        return pencilMarks;
    }
    
    
    public int[] getPencilMarks(int x, int y) {
        return grid[x][y].getPencilMarks();
    }
    
    public void setUserAnswers(int[][] userAnswers) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (userAnswers[i][j] == 0) {
                    grid[i][j].setUnfilled();
                } else {
                    grid[i][j].setFilled();
                }
            }
        }
    }
    
    public void setUserAnswers(int x, int y, int userAnswer) {
        if (grid[x][y].getValue() == userAnswer) {
            grid[x][y].setFilled();
        } else {
            grid[x][y].setUnfilled();
        }
    }
    
    public void setPencilMarks(int[][][] pencilMarks) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j].setPencilMarks(pencilMarks[i][j]);
            }
        }
    }
    
    public void addPencilMark(int x, int y, int value) {
        grid[x][y].addPencilmark(value);
    }
    
    public Cell[][] getGrid(){
        return grid;
    }
    
    public void setGrid(Cell[][] grid){
        this.grid = grid;
    }
}

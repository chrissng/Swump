package core;



public class Cell {
	private final int value;
	private int[] pencilMarks; 
	private boolean isFilled;
	private Player owner;
	
	public Cell(int val, boolean filled) {
		isFilled = filled;
		value = val;
		pencilMarks = new int[9];
	}

	public boolean isFilled() {
		return isFilled;
	}

	public int getValue() {
		return value;
	}

	public int[] getPencilMarks() {
		return pencilMarks;
	}

	public void setPencilMarks(int[] pencilMarks) {
		this.pencilMarks = pencilMarks;
	}
	
	public void addPencilmark(int mark) {
        pencilMarks[mark - 1] = 1;
    }

    public void setFilled() {
        this.isFilled = true;
    }

    public void setUnfilled() {
        this.isFilled = false;
    }
    
    public void setOwner(Player owner){
        this.owner = owner;
    }
    
    public Player getOwner(){
        return owner;
    }
	
}

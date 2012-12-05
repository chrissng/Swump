package gui.board;

import gui.SudokuMainUI;
import gui.misc.FontGenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

import core.Player;
import core.SudokuLogic;
import core.multiplayer.powerups.PowerUp;

public class CellUI extends JLabel {

	private static final long serialVersionUID = 1L;
	private static final Dimension CELL_SIZE = new Dimension(50, 50);

    private final int x;
    private final int y;
    private Color bgColor;
    private boolean cellError;
    private Image cellErrorImage;

	public CellUI(int x, int y, int val, boolean filled) {
		this.x = x;
		this.y = y;
		this.cellError = false;
		try {
            this.cellErrorImage = ImageIO.read(new File("img/wrong.png"));
        } catch (IOException e) {
            this.cellErrorImage = null;
        }

		this.setMaximumSize(CELL_SIZE);
		this.setMinimumSize(CELL_SIZE);
		this.setPreferredSize(CELL_SIZE);
		this.setBackground(Color.WHITE);
		this.setOpaque(true);
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				try {
					if (SudokuLogic.getInstance().getMode() == 0) {
						activateInputUI(evt);
					} else {
						JButton button = SudokuMainUI.main.getMultiPlayerPanel().getTakeOver();
						if (button.isEnabled() && button.isSelected()) {
				        	Player tPlayer = SudokuLogic.getInstance().puzzle.getGrid()[getCoordX()][getCoordY()].getOwner();
				        	if (tPlayer == null) {
				        		return;
				        	}
				        	if (!tPlayer.getPlayerName().equals(SudokuLogic.getInstance().getCurrentPlayer().getPlayerName())) {
				        		doTakeOver();
				        		button.setSelected(false);
				        		SudokuMainUI.main.repaint();
				        	}
				        } else {
				        	activateInputUI(evt);
						}
					}
				} catch (Exception obj) {
					obj.printStackTrace();
				}
			}
		});
		
	}
	
	private void doTakeOver() {
		SudokuLogic.getInstance().setTargetPosX(getCoordX());
		SudokuLogic.getInstance().setTargetPosY(getCoordY());
		SudokuLogic.getInstance().usePowerUp(PowerUp.Type.TAKE_OVER);
	}

	private void activateInputUI(MouseEvent evt) {
		if (SudokuLogic.getInstance().puzzle.getUserAnswer(x, y) != 0) return;
		
		SudokuMainUI main = SudokuMainUI.main;
	    
		Point boardPos = SudokuMainUI.main.board.getLocation();		
		Point mousePos = main.getContentPane().getMousePosition(true);
        
		int posX;
        if (mousePos.getX() > boardPos.getX()+main.board.getWidth()-main.getInputUI().getWidth()) {
            posX = (int) (boardPos.getX()+main.board.getWidth()-main.getInputUI().getWidth())-5;
        } else {
            posX = (int) mousePos.getX()-5;
        }
        
        int posY;
        if (mousePos.getY() > boardPos.getY()+main.board.getHeight()-main.getInputUI().getHeight()) {
            posY = (int) (boardPos.getY()+main.board.getHeight()-main.getInputUI().getHeight())-5;
        } else {
            posY = (int) mousePos.getY()-5;
        }
        
        main.getInputUI().setLocation(posX, posY);
		main.getInputUI().setVisible(true);
		main.setActiveCell(this);
		main.repaint();
	}

	public void input(int val) {
	    int timeElapsed = (int) (SudokuLogic.getInstance().getElapsedTime()/1000);
		if(SudokuLogic.getInstance().validateCell(this.x, this.y, val)){
	        SudokuMainUI.main.board.resetTheme();
			SudokuMainUI.main.updateProgressBar();
			if (SudokuLogic.getInstance().getState() == 1) {
				SudokuMainUI.main.showWin(timeElapsed);
			}
        }
		else {
			final CellUI cell = this;
			final Timer timer = new Timer(200, null);
			timer.addActionListener(new ActionListener() {
				final int MAX = 5;
				int count = 0;
				public void actionPerformed(ActionEvent e) {
					if (count <= MAX) {
						if (count % 2 == 0) {
							cell.setError(true);
							SudokuMainUI.main.repaint();
						} else {
						    cell.setError(false);
						    SudokuMainUI.main.repaint();
						}
						count++;
					} else {
						timer.stop();
					}
				}
			});
			timer.start();
		}
	}
	
	public void inputPencilMark(int val) {

		SudokuLogic.getInstance().insertPencilMark(this.x, this.y, val);
		SudokuMainUI.main.board.resetTheme();
		this.repaint();
	}

	public int getCoordX() {
		return x;
	}

	public int getCoordY() {
		return y;
	}
	
    @Override
    public void setBackground(Color bg) {
        bgColor = bg;
    }
    
    private void setError(boolean error) {
        cellError = error;
    }
   
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
        if (SudokuLogic.getInstance().getMode() == 1) {
            try {
                g.setColor(SudokuLogic.getInstance().puzzle.getOwner(x, y).getColor());    
            } catch (NullPointerException n) {
                g.setColor(Color.WHITE);
            }
		} else {
            g.setColor(bgColor);
		}
        g.fillRoundRect(1, 1, this.getWidth()-2, this.getHeight()-2, 10, 10);
        g.setColor(Color.DARK_GRAY);
        g.drawRoundRect(1, 1, this.getWidth()-2, this.getHeight()-2, 10, 10);
        if (SudokuMainUI.main.getActiveCell() != null && SudokuMainUI.main.getActiveCell() == this) {
            g.drawRoundRect(2, 2, this.getWidth()-4, this.getHeight()-4, 8, 8);
            g.drawRoundRect(3, 3, this.getWidth()-6, this.getHeight()-6, 6, 6);
        }
        
        if (SudokuLogic.getInstance().puzzle == null) return;
        
		int cellValue = SudokuLogic.getInstance().puzzle.getUserAnswer(x, y);
		int[] pencilMarks = SudokuLogic.getInstance().puzzle.getPencilMarks(x, y);
		boolean isFilled = cellValue != 0 ? true : false;
		
		if (isFilled) { // draw cellVal
			g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 30));
			g.setColor(Color.DARK_GRAY);
			
			String cellValStr = String.valueOf(cellValue);
			int fontWidth = g.getFontMetrics().stringWidth(cellValStr);
			int fontHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();
			
			g.drawString(cellValStr, (this.getWidth()-fontWidth)/2, (this.getHeight()-fontHeight)/2+fontHeight);
		} else { // draw pencilMarks
			g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 11));
			g.setColor(Color.BLACK);
			
			int loc = 0;
			for (int i = 0; i < pencilMarks.length; i++) {
				String cellValStr = String.valueOf(i+1);
	            int fontWidth = g.getFontMetrics().stringWidth(cellValStr);
	            int fontHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();

                if (pencilMarks[i] == 1) {
                    loc++;

                    int horizPos = 1;
                    switch (loc % 3) {
                    case 1:
                        horizPos = 1;
                        break;
                    case 2:
                        horizPos = 2;
                        break;
                    case 0:
                        horizPos = 3;
                        break;
                    }

                    int vertPos = 1;
                    if (loc >= 1 && loc <= 3) {
                        vertPos = 1;
                    } else if (loc >= 4 && loc <= 6) {
                        vertPos = 2;
                    } else {
                        vertPos = 3;
                    }
				    
					g.drawString(cellValStr, (((this.getWidth()-(fontWidth*3))/4)*horizPos)+(horizPos-1)*fontWidth, 
					        (((this.getHeight()-(fontHeight*3))/4)*vertPos+fontHeight)+(vertPos-1)*fontHeight);
				}
			}
		}
	      
        if (cellError) {
            if (cellErrorImage!=null) {
                g.drawImage(cellErrorImage, (this.getWidth()-2-32)/2, (this.getHeight()-30)/2, getParent());
            } else {
                g.setColor(Color.RED);
                g.fillRoundRect(1, 1, this.getWidth()-2, this.getHeight()-2, 10, 10);
                g.setColor(Color.DARK_GRAY);
                g.drawRoundRect(1, 1, this.getWidth()-2, this.getHeight()-2, 10, 10);
            }
            return;
        }
        
        if (SudokuLogic.getInstance().getMode() == 1) {
            JButton button = SudokuMainUI.main.getMultiPlayerPanel().getTakeOver();
            if (button.isEnabled() && button.isSelected()) {
                Player tPlayer = SudokuLogic.getInstance().puzzle.getGrid()[getCoordX()][getCoordY()].getOwner();
                if (tPlayer == null) {
                    return;
                }
                if (!tPlayer.getPlayerName().equals(SudokuLogic.getInstance().getCurrentPlayer().getPlayerName())) {
                    g.setColor(Color.CYAN);
                    g.drawRoundRect(1, 1, this.getWidth()-2, this.getHeight()-2, 9, 9);
                    g.drawRoundRect(2, 2, this.getWidth()-4, this.getHeight()-4, 8, 8);
                    g.drawRoundRect(3, 3, this.getWidth()-6, this.getHeight()-6, 6, 6);
                }   
            }
        }
	}
}
package gui;

import gui.board.CellUI;
import gui.board.InputUI;
import gui.board.SudokuBoard;
import gui.misc.About;
import gui.misc.Scoreboard;
import gui.misc.SidePanel;
import gui.misc.Theme;
import gui.multi.MultiPlayerInit;
import gui.multi.MultiPlayerStatPanel;
import gui.single.SingleInit;
import gui.single.SinglePlayerPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import core.SudokuLogic;

public class SudokuMainUI extends JFrame {
	private JProgressBar progressBar;
	private static final long serialVersionUID = 6199126953768687537L;
	
	public static SudokuMainUI main = null;
	public SudokuBoard board;
	private SidePanel sidePanel;
	private SinglePlayerPanel singlePlayerPanel;
	private MultiPlayerStatPanel multiPlayerPanel;

	public Thread gameUIUpdateThread;
	
	private AttentionSeeker attn;
	private LogoPanel logoPanel;
	private CellUI activeCell;
	private InputUI inputUI;
	
	private boolean isPencilMarkInput;
    
    private boolean isNewMultiPlayerGameAndFirstPlayer;
    private String firstPlayerName;
    private String previousPlayerName;
	private boolean pauseTimer;

	public SudokuMainUI() {
	    createLogoPanel();
		createAttentionSeeker();
        createInputUI();
		
		this.setLayout(new BorderLayout());
		createControlPanel();
        
		board = new SudokuBoard();
		this.add(board, BorderLayout.CENTER);
		
		sidePanel = new SidePanel();
		this.add(sidePanel,BorderLayout.EAST);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setIconImage(new ImageIcon("img/icon.png").getImage());
		this.setTitle("Swump!");
		
		main = this;
	}

	private void createLogoPanel() {
	    logoPanel = new LogoPanel();
	    this.add(logoPanel);
    }

    private void createAttentionSeeker() {
	    attn = new AttentionSeeker("", 100, Font.PLAIN, 50);
        this.add(attn);
	}
	
	private void createInputUI() {
		inputUI = new InputUI();
		this.add(inputUI);
	}

	private void createControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(1, 7));
		JButton btnNewSingle = new JButton("Single player",
				new ImageIcon("img/single.png"));
		btnNewSingle.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnNewSingle.setHorizontalTextPosition(AbstractButton.CENTER);

		JButton btnNewMulti = new JButton("Multiplayer", new ImageIcon("img/multiplay.png"));
		btnNewMulti.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnNewMulti.setHorizontalTextPosition(AbstractButton.CENTER);

		JButton btnLoadGame = new JButton("Load game", new ImageIcon("img/drive_cd.png"));
		btnLoadGame.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnLoadGame.setHorizontalTextPosition(AbstractButton.CENTER);

		JButton btnSaveGame = new JButton("Save game", new ImageIcon(
				"img/drive_disk.png"));
		btnSaveGame.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnSaveGame.setHorizontalTextPosition(AbstractButton.CENTER);

		JButton btnScoreBoard = new JButton("Scoreboard", new ImageIcon("img/award.png"));
		btnScoreBoard.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnScoreBoard.setHorizontalTextPosition(AbstractButton.CENTER);		
		
		JButton btnChangeTheme = new JButton("Theme", new ImageIcon(
				"img/color_swatch.png"));
		btnChangeTheme.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnChangeTheme.setHorizontalTextPosition(AbstractButton.CENTER);

		JButton btnAbout = new JButton("About", new ImageIcon("img/about.png"));
		btnAbout.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnAbout.setHorizontalTextPosition(AbstractButton.CENTER);
		
		controlPanel.add(btnNewSingle);
		controlPanel.add(btnNewMulti);
		controlPanel.add(btnLoadGame);
		controlPanel.add(btnSaveGame);
		controlPanel.add(btnScoreBoard);
		controlPanel.add(btnChangeTheme);
		controlPanel.add(btnAbout);
		
		controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressBar.setMaximum(81);

		// actionPerform for change theme
		btnChangeTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Theme().setVisible(true);
			}
		});
		// actionPerform for score board
		btnScoreBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Scoreboard().setVisible(true);
			}
		});

		// actionPerform for multiplayer
        btnNewMulti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
	                new MultiPlayerInit().setVisible(true);
	                SudokuMainUI.main.repaint();
                }
                catch(Exception ex){}
            }
        });
		
		// actionPerform for exit
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new About();
			}
		});

		// actionPerformed for Save Button
		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    if (SudokuLogic.getInstance().getState() != 0) {
			        JOptionPane.showMessageDialog(null,
                            "There is no game in progress.", "Error", JOptionPane.ERROR_MESSAGE);
			        return;
	            }
			    
				if (SudokuLogic.getInstance().getMode() == 0) {
					Object result = JOptionPane.showInputDialog(
							SudokuMainUI.main, "Enter game name:");
					try {
						if (result != null && !result.toString().equals("")) {

							SudokuLogic.getInstance().saveGame(
									result.toString());
						}

						else if (result.toString().equals("")) {
							JOptionPane.showMessageDialog(null,
									"Please enter a game name!");
						}
					} catch (NullPointerException f) {
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Saving is not available in multiplayer mode.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// actionPerformed for Load Button
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//SudokuMainUI.main.board.changeTheme(new Color(222,235,247),new Color(158,202,225));
				String gameList[][] = SudokuLogic.getInstance().loadGameList();

				String bigList[] = new String[gameList.length];

				for (int i = 0; i < gameList.length; i++) {
					bigList[i] = gameList[i][0] + " saved on: "
							+ gameList[i][1];
				}
				try {
					String response = (String) JOptionPane.showInputDialog(
							null, "Select a saved game", "Load game",
							JOptionPane.PLAIN_MESSAGE, null, bigList,
							"");
					int timeIndex = response.indexOf(":");
					String time = response.substring(timeIndex + 2);
					String name = response.substring(0, timeIndex - 9);
					resumeSinglePlayerGame(name, time);
				} catch (NullPointerException f) {
				}
			}
		});

		
		// actionPerformed for New Button
		btnNewSingle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SingleInit().setVisible(true);
				SudokuLogic.getInstance().setNumOfHints(3);
				SudokuMainUI.main.repaint();
			}
		});

		this.add(controlPanel, BorderLayout.NORTH);
		this.add(progressBar,BorderLayout.SOUTH);
	}
	
	public void updateProgressBar(){
		//int current = progressBar.getValue();
		//progressBar.setValue(++current);
	    progressBar.setValue(81-SudokuLogic.getInstance().getRemainingCells());
	}

	public SinglePlayerPanel getSinglePlayerPanel() {
		return singlePlayerPanel;
	}
	
	public MultiPlayerStatPanel getMultiPlayerPanel() {
		return multiPlayerPanel;
	}
	
	public InputUI getInputUI() {
		return inputUI;
	}

	public CellUI getActiveCell() {
		return activeCell;
	}

	public void setActiveCell(CellUI activeCell) {
		this.activeCell = activeCell;
	}

	public boolean isPencilMarkInput() {
		return isPencilMarkInput;
	}

	public void setPencilMarkInput(boolean isPencilMark) {
		this.isPencilMarkInput = isPencilMark;
	}
	
	public LogoPanel getLogoPanel() {
	    return logoPanel;
	}
	
	public JProgressBar getProgressBar() {
        return progressBar;
    }
	
    public SidePanel getSidePanel() {
        return sidePanel;
    }
	
	public void showWin(int time) {
		stopTimer();
		if (SudokuLogic.getInstance().getMode() == 0) {
		    JOptionPane.showMessageDialog(SudokuMainUI.main, "Congratulations!\nYou have completed the puzzle!");
	        String name = JOptionPane.showInputDialog(null, "Please enter your name:");
	        if(name != null && name.trim().length() > 0){
	            SudokuLogic.getInstance().updateScoreboard(name,time);
	        }
		} else {
		    JOptionPane.showMessageDialog(SudokuMainUI.main, SudokuLogic.getInstance().getWinner().getPlayerName() + " wins!");
		}

		logoPanel.setVisible(true);
		board.setVisible(false);
		progressBar.setVisible(false);
        sidePanel.setVisible(false);
	}
	
	public void activateMultiPlayerPanel() {
	    multiPlayerPanel = new MultiPlayerStatPanel();
        sidePanel.setPanel(multiPlayerPanel);
        startTimer();
        updateProgressBar();
        SudokuMainUI.main.setPencilMarkInput(false);
        SudokuMainUI.main.repaint();
	}
	
	public void activateSinglePlayerPanel(int difficulty){
		singlePlayerPanel = new SinglePlayerPanel();
		sidePanel.setPanel(singlePlayerPanel);
		startTimer();

		String diff="";
		switch(difficulty){
			case 0:diff="Easy";break;
			case 1 :diff="Normal";break;
			case 2:diff="Hard";break;
		}
		SudokuMainUI.main.getSinglePlayerPanel().setDifficulty(diff);
        SudokuMainUI.main.setPencilMarkInput(false);
		SudokuMainUI.main.repaint();
	}
	
    private void startTimer() {
    	pauseTimer = false;
        isNewMultiPlayerGameAndFirstPlayer = true;
        firstPlayerName = SudokuLogic.getInstance().getCurrentPlayer().getPlayerName();
        previousPlayerName = SudokuLogic.getInstance().getCurrentPlayer().getPlayerName();
        
        logoPanel.setVisible(false);
        board.setVisible(true);
        progressBar.setVisible(true);
        sidePanel.setVisible(true);
        
        if (gameUIUpdateThread != null) {
        	return;
        }
        gameUIUpdateThread = new Thread(new Runnable() {
            public void run() {
                while (!pauseTimer) {
                    try {
                        long time = SudokuLogic.getInstance().getElapsedTime();
                        long elapsedTime;
                        if (SudokuLogic.getInstance().getMode() == 0) {
                            // SINGLEPLAYER
                            elapsedTime = time;
                        } else {
                            String currentPlayerName = SudokuLogic.getInstance().getCurrentPlayer().getPlayerName();
                            
                            if (multiPlayerPanel != null ) {
                                multiPlayerPanel.refresh();
                            }
                            elapsedTime = SudokuLogic.getInstance().getTurnTime()
                                    - time + 1000;
                            if (SudokuMainUI.main.getMultiPlayerPanel() != null) {
                                SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();
                            }
                            
                            if (!firstPlayerName.equals(currentPlayerName)) {
                                isNewMultiPlayerGameAndFirstPlayer = false;
                            }

                            attn.setLocation(main.board.getLocation());
                            attn.setSize(main.board.getSize());
                            if (elapsedTime/1000 <= 5) {
                                attn.setText(elapsedTime/1000+"", 100, Font.PLAIN, 500);
                                attn.setVisible(true);
                                main.repaint();
                            }  else if ((elapsedTime/1000+1 > SudokuLogic.getInstance().getTurnTime()/1000) && (!previousPlayerName.equals(currentPlayerName))) {
                                SudokuMainUI.main.getMultiPlayerPanel().getTakeOver().setSelected(false);
                                attn.setText("Next!", 150, Font.BOLD, 140);
                                attn.setVisible(true);
                                main.repaint();
                            } else if ((elapsedTime/1000+1 > SudokuLogic.getInstance().getTurnTime()/1000) && isNewMultiPlayerGameAndFirstPlayer) {
                                isNewMultiPlayerGameAndFirstPlayer = false;
                                attn.setText("Start!", 150, Font.BOLD, 140);
                                attn.setVisible(true);
                                main.repaint();
                            } else {
                                attn.setVisible(false);
                            }
                            previousPlayerName = currentPlayerName;
                        }
                        SudokuMainUI.main.sidePanel.setTime(elapsedTime);
                        
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {}
                    } catch (Exception e) {}
                }
            }
        });
        gameUIUpdateThread.start();
    }

    public void stopTimer() {
        if (gameUIUpdateThread != null) {
        	gameUIUpdateThread = null;
        	pauseTimer = true;
        }
    }
    
    public void startNewSinglePlayerGame(int difficulty){
		SudokuLogic.getInstance().startNewGame(difficulty);
		attn.setVisible(false);
		activateSinglePlayerPanel(difficulty);
		SudokuLogic.getInstance().setNumOfHints(3);
		updateProgressBar();
	}
    
    public void resumeSinglePlayerGame(String name, String time) {
    	SudokuLogic.getInstance().loadGame(name, time);
    	activateSinglePlayerPanel(SudokuLogic.getInstance().puzzle.getDifficulty());
		SudokuLogic.getInstance().setNumOfHints(3);
		updateProgressBar();
    }
    
}

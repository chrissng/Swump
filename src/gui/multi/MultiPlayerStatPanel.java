package gui.multi;

import gui.SudokuMainUI;
import gui.misc.FontGenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import core.Player;
import core.SudokuLogic;
import core.multiplayer.powerups.PowerUp;

public class MultiPlayerStatPanel extends JPanel {
	private static final long serialVersionUID = -3478952106040900005L;
	
	private JLabel lblHint;
    private JLabel lblTake;
    private JLabel lblTry;
    private JLabel lblTime;

    private JButton btnTake;
    private JButton btnHint;
    private JButton btnTry;
    private JButton btnTime;
    
    private StatUI[] playerStat;
	private JPanel playerInfo = new JPanel();
	
    private class StatUI extends JPanel {
    	
        private static final long serialVersionUID = -4750409607717678613L;

		private static final String SCORE_DESC = "Score: ";
        
        private static final String TRIES_DESC = "Tries Left: ";

        private static final int CURRENT_TURN_BOX_LENGTH = 50;

        private static final int NOT_TURN_BOX_LENGTH = 20;

        private static final int X_OFFSET = 15;

        private static final int SPACE_BETWEEN = 10;

        private boolean isPlayerTurn;

        private String playerName;

        private String score;
        
        private String triesLeft;

        private Color playerColor;

        public StatUI() {
            playerName = "";
            score = "";
            playerColor = Color.BLACK;
        }
        
        /*
        public StatUI(boolean isTurn, String name, Color color) {
            playerName = name;
            score = SCORE_DESC + 0;
            playerColor = color;
            triesLeft = TRIES_DESC + 0;
            setPlayerTurn(isTurn);

            //this.setBorder(BorderFactory.createLineBorder(Color.black));
        }*/

        public void setPlayerTurn(boolean turn) {
            isPlayerTurn = turn;

            this.setPreferredSize(new Dimension(200, 70));
            this.setMinimumSize(getPreferredSize());
            this.setMaximumSize(getPreferredSize());
        }

        public boolean isPlayerTurn() {
            return isPlayerTurn;
        }

        public String getPlayerScore() {
            return score;
        }
        
        public void setPlayerScore(int score) {
            this.score = SCORE_DESC + score;
        }

        public Color getPlayerColor() {
            return playerColor;
        }

        public void setPlayerColor(Color playerColor) {
            this.playerColor = playerColor;
        }
        
        public String getPlayerName() {
            return playerName;
        }
        
        public void setPlayerName(String name) {
            this.playerName = name;
        }
        
        public String getTriesLeft() {
        	return triesLeft;
        }
        
        public void setTriesLeft(int t) {
        	this.triesLeft = TRIES_DESC + t;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (getPlayerName().equals("")) return;
            
            g.setColor(Color.DARK_GRAY);

            int height = this.getHeight();
            int playerNamefontHeight, scorefontHeight;

            if (isPlayerTurn()) {
				g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 12));
				scorefontHeight = g.getFontMetrics().getAscent()
						- g.getFontMetrics().getDescent();
				g.setFont(FontGenerator.generateItalicFont(Font.BOLD, 30));
				playerNamefontHeight = g.getFontMetrics().getAscent()
						- g.getFontMetrics().getDescent();

				// Draw player's name
				g
						.drawString(
								getPlayerName(),
								CURRENT_TURN_BOX_LENGTH + X_OFFSET
										+ SPACE_BETWEEN,
								(height - SPACE_BETWEEN - SPACE_BETWEEN
										- scorefontHeight - scorefontHeight - playerNamefontHeight)
										/ 3 + playerNamefontHeight);

				// Draw player's score
				g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 12));
				g
						.drawString(
								getPlayerScore(),
								CURRENT_TURN_BOX_LENGTH + X_OFFSET
										+ SPACE_BETWEEN,
								(height - SPACE_BETWEEN - SPACE_BETWEEN
										- scorefontHeight - scorefontHeight - playerNamefontHeight)
										/ 3
										+ playerNamefontHeight
										+ SPACE_BETWEEN + scorefontHeight);

				// Draw player's tries left
				g
						.drawString(
								getTriesLeft(),
								CURRENT_TURN_BOX_LENGTH + X_OFFSET
										+ SPACE_BETWEEN,
								(height - SPACE_BETWEEN - SPACE_BETWEEN
										- scorefontHeight - scorefontHeight - playerNamefontHeight)
										/ 3
										+ playerNamefontHeight
										+ SPACE_BETWEEN
										+ scorefontHeight
										+ SPACE_BETWEEN + scorefontHeight);

                // Paint player's colour
                g.setColor(getPlayerColor());
                g.fillRoundRect(X_OFFSET,
                        (height - CURRENT_TURN_BOX_LENGTH) / 2,
                        CURRENT_TURN_BOX_LENGTH, CURRENT_TURN_BOX_LENGTH, 8, 8);
            } else {
                g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 12));
                scorefontHeight = g.getFontMetrics().getAscent()
                        - g.getFontMetrics().getDescent();
                g.setFont(FontGenerator.generateItalicFont(Font.BOLD, 16));
                playerNamefontHeight = g.getFontMetrics().getAscent()
                        - g.getFontMetrics().getDescent();

                // Draw player's name
                g.drawString(getPlayerName(), CURRENT_TURN_BOX_LENGTH + X_OFFSET
                        + SPACE_BETWEEN, (height - SPACE_BETWEEN
                        - scorefontHeight - playerNamefontHeight)
                        / 2 + playerNamefontHeight);

                // Draw player's score
                g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 12));
                g.drawString(getPlayerScore(), CURRENT_TURN_BOX_LENGTH + X_OFFSET
                        + SPACE_BETWEEN, (height - SPACE_BETWEEN
                        - scorefontHeight - playerNamefontHeight)
                        / 2
                        + playerNamefontHeight
                        + SPACE_BETWEEN
                        + scorefontHeight);

                // Paint player's colour
                g.setColor(getPlayerColor());
                g.fillRoundRect(
                        ((CURRENT_TURN_BOX_LENGTH - NOT_TURN_BOX_LENGTH) / 2)
                                + X_OFFSET, (height - NOT_TURN_BOX_LENGTH) / 2,
                        NOT_TURN_BOX_LENGTH, NOT_TURN_BOX_LENGTH, 8, 8);
            }
        }
    }

    public MultiPlayerStatPanel() {
        
        lblHint = new JLabel("");
        lblTake = new JLabel("");
        lblTry = new JLabel("");
        lblTime = new JLabel("");
        
        lblHint.setVerticalAlignment(SwingConstants.BOTTOM);
        lblHint.setHorizontalAlignment(SwingConstants.CENTER);
        lblTake.setVerticalAlignment(SwingConstants.BOTTOM);
        lblTake.setHorizontalAlignment(SwingConstants.CENTER);
        lblTry.setVerticalAlignment(SwingConstants.BOTTOM);
        lblTry.setHorizontalAlignment(SwingConstants.CENTER);
        lblTime.setVerticalAlignment(SwingConstants.BOTTOM);
        lblTime.setHorizontalAlignment(SwingConstants.CENTER);
        
    	playerInfo.setLayout(new BoxLayout(playerInfo, BoxLayout.PAGE_AXIS));
        playerStat = new StatUI[4];
        for (int i = 0; i < playerStat.length; i++) {
            playerStat[i] = new StatUI();
            playerInfo.add(playerStat[i]);
        }
        this.setLayout(new BorderLayout());
        
        JPanel controlBtn = new JPanel();
        controlBtn.setLayout(new GridLayout(2,4));
        controlBtn.setBorder(BorderFactory.createEmptyBorder(15,0,15,15));        
        
        btnHint = new JButton(new ImageIcon("img/hintM.png"));
        btnHint.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnHint.setHorizontalTextPosition(AbstractButton.CENTER);
        btnHint.setToolTipText("Get a free hint!");
        // actionPerform for Hint
        btnHint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
	                SudokuLogic.getInstance().usePowerUp(PowerUp.Type.HINT);
                    SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();
                    SudokuMainUI.main.updateProgressBar();
                    SudokuMainUI.main.board.repaint();
                    
                    if (SudokuLogic.getInstance().getState() == 1) {
                        SudokuMainUI.main.showWin(0);
                    }
                }
                catch(Exception ex){}
            }
        });
		
		btnTake = new JButton(new ImageIcon("img/takeover.png"));
		btnTake.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnTake.setHorizontalTextPosition(AbstractButton.CENTER);
		btnTake.setToolTipText("Take over a cell!");
		// actionPerform for Takeover
        btnTake.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                	if(btnTake.isSelected() == true){
                		btnTake.setSelected(false);
                		SudokuMainUI.main.board.repaint();
                	}
                	else {
                		btnTake.setSelected(true);
                		SudokuMainUI.main.board.repaint();
                	}
                    SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();
                }
                catch(Exception ex){}
            }
        });
        
		btnTry = new JButton(new ImageIcon("img/tries.png"));
		btnTry.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnTry.setHorizontalTextPosition(AbstractButton.CENTER);
		btnTry.setToolTipText("Add more tries to yourself this turn!");
		// actionPerform for Try
		btnTry.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
	                SudokuLogic.getInstance().usePowerUp(PowerUp.Type.TRY);
                    SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();
                }
                catch(Exception ex){}
            }
        });
		
		btnTime = new JButton(new ImageIcon("img/time.png"));
		btnTime.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnTime.setHorizontalTextPosition(AbstractButton.CENTER);
		btnTime.setToolTipText("Extend your turn time!");
        // actionPerform for Time
		btnTime.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    SudokuLogic.getInstance().usePowerUp(PowerUp.Type.TIME);
                    SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();
                }
                catch(Exception ex){}
            }
        });
		
		controlBtn.add(lblHint);
		controlBtn.add(lblTake);
		controlBtn.add(lblTry);
        controlBtn.add(lblTime);
		controlBtn.add(btnHint);
		controlBtn.add(btnTake);
		controlBtn.add(btnTry);
        controlBtn.add(btnTime);
		
		this.add(playerInfo,BorderLayout.NORTH);
		this.add(controlBtn,BorderLayout.SOUTH);
		this.refresh();
    }

    public void refresh() {
        LinkedList<Player> play = SudokuLogic.getInstance().retrieveListOfPlayers();
        
        int total = play.size();
        for (int i = 0; i < total; i++) {
            Player tPlayer = play.remove();
            if (i == 0) {
                playerStat[i].setPlayerTurn(true);
            } else {
                playerStat[i].setPlayerTurn(false);
            }
            playerStat[i].setPlayerScore((tPlayer.getScore() == null) ? 0 : tPlayer.getScore().getCellsOwned());
            playerStat[i].setPlayerName(tPlayer.getPlayerName());
            playerStat[i].setPlayerColor(tPlayer.getColor());
            playerStat[i].setTriesLeft(tPlayer.getTurnTries());
        }
                
        this.repaint();
        //this.revalidate();
    }
    
    public JButton getTakeOver(){
    	return btnTake;
    }

    public void refreshButtons() {
        int hint = SudokuLogic.getInstance().getCurrentPlayer().numHintPowerUp;
        int takeOver = SudokuLogic.getInstance().getCurrentPlayer().numTakeOverPowerUp;
        int tryPwr = SudokuLogic.getInstance().getCurrentPlayer().numTryPowerUp;
        int time = SudokuLogic.getInstance().getCurrentPlayer().numTimePowerUp;
        
        if (hint <= 0) {
            lblHint.setEnabled(false);
            btnHint.setEnabled(false);
        } else {
            lblHint.setEnabled(true);
            btnHint.setEnabled(true);
        }
        if (takeOver <= 0) {
            lblTake.setEnabled(false);
            btnTake.setEnabled(false);
        } else {
            lblTake.setEnabled(true);
            btnTake.setEnabled(true);
        }
        if (tryPwr <= 0) {
            lblTry.setEnabled(false);
            btnTry.setEnabled(false);
        } else {
            lblTry.setEnabled(true);
            btnTry.setEnabled(true);
        }
        if (time <= 0) {
            lblTime.setEnabled(false);
            btnTime.setEnabled(false);
        } else {
            lblTime.setEnabled(true);
            btnTime.setEnabled(true);
        }
        
        lblHint.setText(hint+"");
        lblTake.setText(takeOver+"");
        lblTry.setText(tryPwr+"");
        lblTime.setText(time+"");
    }
}

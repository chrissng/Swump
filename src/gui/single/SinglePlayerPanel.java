package gui.single;

import gui.SudokuMainUI;
import gui.misc.FontGenerator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.SudokuLogic;

public class SinglePlayerPanel extends JPanel{
	private static final long serialVersionUID = 1735162861773916659L;
	private JLabel difficulty;
	JLabel left;
	public SinglePlayerPanel(){
		this.setLayout(new BorderLayout());
		
		JPanel stats = new JPanel();
		stats.setLayout(new GridLayout(8,1,0,0));
		
		difficulty = new JLabel("Difficulty: ");
		difficulty.setFont(FontGenerator.generateItalicFont(Font.PLAIN,  20));
		
		stats.add(difficulty);
		JPanel iconPanel = new JPanel();
		iconPanel.setLayout(new GridLayout(1,2));
		
		final JButton btnHint = new JButton("Show hint", new ImageIcon("img/hint.png"));
		btnHint.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnHint.setHorizontalTextPosition(AbstractButton.CENTER);
		// actionPerformed for generate hint Button
		
		btnHint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
				    int elapsedTime = (int) (SudokuLogic.getInstance().getElapsedTime()/1000);
					int hintLeft = SudokuLogic.getInstance().getCurrentPlayer().getNumHints();
					if(hintLeft > 0){
						btnHint.setEnabled(true);
						SudokuLogic.getInstance().showHint();
			            SudokuMainUI.main.updateProgressBar();
						SudokuMainUI.main.repaint();
						if(hintLeft - 1 == 0){
							btnHint.setEnabled(false);
						}
						if(SudokuLogic.getInstance().getState() == 1){
							SudokuMainUI.main.showWin(elapsedTime);
						}
					}
					else
						btnHint.setEnabled(false);
				}
				catch(Exception ex) {
				    ex.printStackTrace();
				} 
				
			}
		});
		final JButton btnToggle = new JButton("Pen",new ImageIcon("img/pen.png"));
		btnToggle.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnToggle.setHorizontalTextPosition(AbstractButton.CENTER);
		btnToggle.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	if(btnToggle.getText().equalsIgnoreCase("Pen")){
            		btnToggle.setText("Pencil");
            		SudokuMainUI.main.setPencilMarkInput(true);
            		btnToggle.setIcon(new ImageIcon("img/pencil.png"));
            	} else{
            		btnToggle.setText("Pen");
            		SudokuMainUI.main.setPencilMarkInput(false);
            		btnToggle.setIcon(new ImageIcon("img/pen.png"));
            	}
            }
        });
		
		iconPanel.add(btnHint);
		iconPanel.add(btnToggle);
		this.add(stats,BorderLayout.WEST);
		this.add(iconPanel,BorderLayout.SOUTH);
		this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
	
	public void setDifficulty(String diff) {
		difficulty.setText("Difficulty: " + diff);
	}
}

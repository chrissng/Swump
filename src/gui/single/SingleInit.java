package gui.single;

import gui.SudokuMainUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SingleInit extends JDialog{
	
	private static final long serialVersionUID = -7490402782065032152L;
	public Thread gameUIUpdateThread;

	public SingleInit(){
		super(SudokuMainUI.main, "Single player game", true);
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(200,80));
		this.getContentPane().add(mainPanel);
		this.pack();
		this.setResizable(false);
        this.setLocationRelativeTo(SudokuMainUI.main);
        
        mainPanel.setLayout(new FlowLayout());
        
        JPanel btnPanel = new JPanel();
        
        JButton btnEasy = new JButton("Easy",new ImageIcon("img/emotion_tongue.png"));
		btnEasy.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnEasy.setHorizontalTextPosition(AbstractButton.CENTER);
        JButton btnNormal = new JButton("Normal",new ImageIcon("img/emotion_smile.png"));
		btnNormal.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnNormal.setHorizontalTextPosition(AbstractButton.CENTER);
        JButton btnHard = new JButton("Hard",new ImageIcon("img/emotion_evilgrin.png"));
		btnHard.setVerticalTextPosition(AbstractButton.BOTTOM);
		btnHard.setHorizontalTextPosition(AbstractButton.CENTER);
		mainPanel.add(new JLabel("Please select difficulty level"),BorderLayout.NORTH);
        btnPanel.add(btnEasy,BorderLayout.WEST);
        btnPanel.add(btnNormal,BorderLayout.CENTER);
        btnPanel.add(btnHard,BorderLayout.EAST);
        mainPanel.add(btnPanel);
        
        // Action handler
        btnEasy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				SudokuMainUI.main.startNewSinglePlayerGame(0);
			}
		});
     // Action handler
        btnNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				SudokuMainUI.main.startNewSinglePlayerGame(1);
			}
		});
     // Action handler
        btnHard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				SudokuMainUI.main.startNewSinglePlayerGame(2);
			}
		});
	}
	
}

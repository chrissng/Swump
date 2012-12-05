package gui.misc;

import gui.SudokuMainUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class About extends JDialog {
    private static final long serialVersionUID = 1L;

    private String strAbout = "<html><h1>Swump!</h1><p>Version 0.2<br>All rights reserved."
            + "<p><h3>Developed by:</h3>Lau Wei Lun<br/>Sng Aik Wei<br/>Tran Binh Nguyen<br/>Wong Kangwei<p>&nbsp;</p></html>";

    public About() {
        super(SudokuMainUI.main, "About", true);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(300, 250));
        
        JLabel about = new JLabel();
        about.setText(strAbout);
        about.setFont(FontGenerator.generateStdFont(Font.PLAIN, 14));
        about.setVerticalTextPosition(JLabel.NORTH);
        
        mainPanel.add(new JLabel(new ImageIcon("img/pen.png")),
                BorderLayout.WEST);
        mainPanel.add(about, BorderLayout.CENTER);

        this.getContentPane().add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(SudokuMainUI.main);
        this.setResizable(false);
        this.setVisible(true);
    }
}

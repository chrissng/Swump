package gui.board;

import gui.SudokuMainUI;
import gui.misc.FontGenerator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputUI extends JPanel {

    private static final long serialVersionUID = -7301750359459732387L;
	public static InputUI inputUI = null;
    
    private class InputButton extends JLabel {
        
        private static final long serialVersionUID = -4816053135542630420L;

		InputButton(String text) {
            super(text);

            //setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
            setFont(FontGenerator.generateItalicFont(Font.PLAIN, 14));
            setOpaque(false);
            
            // If mouse is outside input panel -> set visible to false
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent evt) {
                    numberSelected(null);
                }
            	public void mouseExited(MouseEvent evt) {
    				Point mousePos = SudokuMainUI.main.getContentPane().getMousePosition(true);
    				try {
    				    if ((mousePos.getX() < inputUI.getX()) || (mousePos.getY() < inputUI.getY()) || (mousePos.getX() > inputUI.getX() + inputUI.getWidth()) || (mousePos.getY() > inputUI.getY() + inputUI.getHeight())) {
                            inputUI.setVisible(false);
                            SudokuMainUI.main.setActiveCell(null);
                            SudokuMainUI.main.repaint();
                        }
    				} catch (NullPointerException npe) {
    				    inputUI.setVisible(false);
                        SudokuMainUI.main.setActiveCell(null);
                        SudokuMainUI.main.repaint();
    				}
    				//if(!(mousePos.getX() > inputUI.getX() && mousePos.getX() < inputUI.getX() +120 ) || !(mousePos.getY() > inputUI.getY() && mousePos.getY() < inputUI.getY() +120 )){
    				//	inputUI.setVisible(false);
    				//}
    			}
    			
    		});
        }
        
        private void numberSelected(ActionEvent evt) {

            inputUI.setVisible(false);
        	if (SudokuMainUI.main.isPencilMarkInput()) {
        		SudokuMainUI.main.getActiveCell().inputPencilMark(Integer.parseInt(this.getText()));
        	} else {
                SudokuMainUI.main.getActiveCell().input(Integer.parseInt(this.getText()));
        	}
        	SudokuMainUI.main.setActiveCell(null);
        	SudokuMainUI.main.repaint();
        }
    }
    
    public InputUI() {
        setLayout(new GridLayout(3,3));
        setVisible(false);
        setSize(120,120);
        setOpaque(false);
        
        for (int i = 1; i <= 9; i++) {
            InputButton button = new InputButton(i+"");
            add(button);
        }
        
        inputUI = this;
    }
    
    protected void paintComponent(Graphics g) {
        int w = getWidth()-2;
        int h = getHeight()-2;
        int arc = 20;

        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Graphics2D g2 = (Graphics2D) g.create();
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(189, 189, 189, 200));
        g.fillRoundRect(0, 0, w, h, arc, arc);

        ((Graphics2D) g).setStroke(new BasicStroke(2f));
        g.setColor(Color.WHITE);
        g.drawRoundRect(0, 0, w, h, arc, arc);
        
        g.drawLine(w/3, 0, w/3, h); // left vertical
        g.drawLine((w/3)*2, 0, (w/3)*2, h); // right vertical
        g.drawLine(0, h/3, w, h/3); // top horizontal
        g.drawLine(0, (h/3)*2, w, (h/3)*2); // bottom horizontal
    }
}

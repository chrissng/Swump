import gui.SudokuMainUI;

import javax.swing.UIManager;

import core.generator.PuzzleStorageThread;

public class Swump {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception ex) {
            System.exit(0);
        }
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PuzzleStorageThread generateEasy = new PuzzleStorageThread(0);
                PuzzleStorageThread generateNormal = new PuzzleStorageThread(1);
                PuzzleStorageThread generateHard = new PuzzleStorageThread(2);
                new Thread(generateEasy).start();
                new Thread(generateNormal).start();
                new Thread(generateHard).start();
                
                SudokuMainUI main = new SudokuMainUI();
                main.getLogoPanel().setVisible(true);
                main.setVisible(true);
            }
        });
    }
}

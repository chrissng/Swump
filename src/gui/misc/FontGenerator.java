package gui.misc;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class FontGenerator {

    private static Font italicFont;
    private static Font font;
    
    private FontGenerator() {}
    
    public static Font generateItalicFont(int style, int size) {
        if (italicFont != null) return italicFont.deriveFont(style, size);
        
        try {
            italicFont = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream(new FileInputStream("font/segoepr.ttf")));
        } catch (Exception ex) {
            italicFont = new Font("SanSerif", style, size);
        }
        italicFont = italicFont.deriveFont(style, size);
        return italicFont;
    }
    
    public static Font generateStdFont(int style, int size) {
        if (font != null) return font.deriveFont(style, size);
        
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream(new FileInputStream("font/segoeui.ttf")));
        } catch (Exception ex) {
            font = new Font("SanSerif", style, size);
        }
        font = font.deriveFont(style, size);
        return font;
    }
}
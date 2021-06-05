package gui.label;

import javax.swing.*;
import java.awt.*;

public class SeparationLabel extends JLabel {
    public SeparationLabel(String text, Color bg, Color fontColor, Font myFont, int height) {
        setText(text);
        setBackground(bg);
        setOpaque(false);
        setForeground(fontColor);
        setFont(new Font(myFont.getName(), myFont.getStyle(), 60));
        setSize(1100, height);
        setLocation(560, 0);
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}

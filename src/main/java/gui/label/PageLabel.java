package gui.label;

import javax.swing.*;
import java.awt.*;

public class PageLabel extends JLabel {
    public PageLabel(int numPages, Color bgColor, Color secondaryColor, Font checkFont) {
        setText("Seitenzahl: " + numPages);
        setBackground(bgColor);
        setForeground(secondaryColor);
        setFont(checkFont);
        setSize(700, 60);
    }
}

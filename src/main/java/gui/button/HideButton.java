package gui.button;

import javax.swing.*;
import java.awt.*;

public class HideButton extends JButton {
    public HideButton(ImageIcon icon, Color bg, int height) {
        setIcon(icon);
        setBackground(bg);
        setSize(64, 64);
        setLocation(25, (height - getHeight()) / 2);
        setBorder(null);
    }
}

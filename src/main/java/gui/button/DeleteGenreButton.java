package gui.button;

import javax.swing.*;
import java.awt.*;

public class DeleteGenreButton extends JButton {
    public DeleteGenreButton(ImageIcon icon, Color bg) {
        setIcon(icon);
        setSize(64, 64);
        setLocation(1800, -2);
        setBorder(null);
        setBackground(bg);
    }
}

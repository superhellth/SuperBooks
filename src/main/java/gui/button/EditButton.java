package gui.button;

import javax.swing.*;
import java.awt.*;

public class EditButton extends JButton {

    public EditButton(ImageIcon icon, Color fontColor, Color bgColor, Font buttonFont) {
        setIcon(icon);
        setForeground(fontColor);
        setBackground(bgColor);
        setFont(buttonFont);
        setSize(64, 64);
    }
}

package gui.button;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DeleteButton extends JButton {
    public DeleteButton(ImageIcon icon, Color bgColor, Font buttonFont, Color primaryColor) {
        setIcon(icon);
        setBackground(bgColor);
        setBorder(new LineBorder(primaryColor, 2, true));
        setFont(buttonFont);
        setSize(64, 64);
    }
}

package gui.label;

import javax.swing.*;
import java.awt.*;

public class TitleLabel extends JLabel {

    public TitleLabel(String title, Color fontColor, Font myFont) {
        setText(title);
        setBackground(Color.BLACK);
        setForeground(fontColor);
        Font nameFont = new Font(myFont.getName(), myFont.getStyle(), 73);
        setFont(nameFont);
        setSize(1600, 85);
    }
}

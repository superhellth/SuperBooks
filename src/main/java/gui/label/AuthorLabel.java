package gui.label;

import bookManagement.Book;

import javax.swing.*;
import java.awt.*;

public class AuthorLabel extends JLabel {
    public AuthorLabel(String author, Color bgColor, Color fontColor, Font myFont) {
        setText(author);
        setBackground(bgColor);
        setForeground(fontColor);
        Font authorFont = new Font(myFont.getName(), myFont.getStyle(), 50);
        setFont(authorFont);
        setSize(700, 60);
    }
}

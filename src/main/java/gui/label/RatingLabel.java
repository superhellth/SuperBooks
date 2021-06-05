package gui.label;

import bookManagement.Book;

import javax.swing.*;
import java.awt.*;

public class RatingLabel extends JLabel {
    public RatingLabel(int totalRat, Color bgColor, Color secondaryColor, Font checkFont) {
        setText("Gesamtbewertung: " + totalRat + "%");
        setBackground(bgColor);
        setForeground(secondaryColor);
        setFont(checkFont);
        setSize(700, 60);
    }
}

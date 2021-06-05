package otherFrames;

import bookManagement.Subgenre;
import gui.swing.CusJFrame;
import main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddGenreFrame extends CusJFrame {

    private MainFrame mainFrame;

    private Font smFont;

    private Dimension smLabelDimension = new Dimension(175, 30);
    private Dimension smFieldDimension = new Dimension(175, 60);

    private JComboBox<String> genreField;
    private JTextField subgenreField;

    public AddGenreFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        getContentPane().setBackground(mainFrame.bgColor);
        setTitle("Subgenre hinzufügen");
        setSize(650, 180);
        centralize();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        smFont = new Font(mainFrame.myFont.getName(), mainFrame.myFont.getStyle(), 30);

        createLabel("Genre:", 25, 25);
        createLabel("Subgenre:", 225, 25);

        genreField = createComboBox(mainFrame.getGenreStrings(), smFieldDimension, 25, 55);
        subgenreField = createTextField(225, 55);

        createButton("Hinzufügen", new Dimension(200, 60), 425, 55);

        setVisible(true);
    }

    private JButton createButton(String text, Dimension size, int xCord, int yCord) {
        JButton button = new JButton(text);
        button.setBackground(mainFrame.primaryColor);
        button.setForeground(mainFrame.fontColor);
        button.setSize(size);
        button.setLocation(xCord, yCord);
        button.setFont(smFont);
        button.addActionListener(e -> {
            mainFrame.dbWriter.addGenreToDb(new Subgenre(genreField.getSelectedItem().toString(),
                    subgenreField.getText()));
            mainFrame.init();
            dispose();
        });

        add(button);
        return button;
    }

    private JComboBox<String> createComboBox(ArrayList<String> items, Dimension size, int xCord, int yCord) {
        JComboBox<String> comboBox = new JComboBox<>();
        for(String s : items) {
            comboBox.addItem(s);
        }

        comboBox.setBackground(mainFrame.primaryColor);
        comboBox.setForeground(mainFrame.fontColor);
        comboBox.setFont(smFont);

        comboBox.setSize(size);
        comboBox.setLocation(xCord, yCord);

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals("5")) {
                comboBox.setSelectedItem("5");
            }
        }

        add(comboBox);
        return comboBox;
    }

    private JTextField createTextField(int xCord, int yCord) {
        JTextField textField = new JTextField();
        textField.setForeground(mainFrame.fontColor);
        textField.setBackground(mainFrame.secondaryColor);
        textField.setSize(smFieldDimension);
        textField.setLocation(xCord, yCord);
        textField.setFont(smFont);

        add(textField);
        return textField;
    }

    private void createLabel(String text, int xCord, int yCord) {
        JLabel label = new JLabel(text);
        label.setForeground(mainFrame.fontColor);
        label.setBackground(mainFrame.bgColor);
        label.setSize(smLabelDimension);
        label.setLocation(xCord, yCord);
        label.setFont(smFont);

        add(label);
    }

}

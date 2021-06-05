package statisticFrames;

import gui.swing.CusJFrame;
import main.MainFrame;

import javax.swing.*;

public class FavFrame extends CusJFrame {

    private MainFrame mainFrame;

    public FavFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        //set basic gui
        setBasicGui();

        //create title label


        //create cover label

        //create author label

        //create genre label

        //create subgenre label

        //create language label

        //create rating labels


        setVisible(true);
    }

    private void setBasicGui() {
        setTitle("Deine Lieblingsbücher und Genres");
        setSize(1920, 1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

}

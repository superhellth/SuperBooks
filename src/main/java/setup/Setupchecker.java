package setup;

import main.MainFrame;

import java.io.*;

public class Setupchecker {

    public Setupchecker() {
        String dbPath = "";

        Reader reader = null;
        File dbSettings = new File("dbPath.txt");
        // Aus Verzeichnis
        if (dbSettings.exists()) {
            try {
                reader = new FileReader(dbSettings);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Aus Main Jar
        } else {
            InputStream inputStream = MainFrame.class.getClassLoader().getResourceAsStream("dbPath.txt");
            reader = new InputStreamReader(inputStream);
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(reader);
            dbPath = bufferedReader.readLine();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dbPath == null || dbPath.equalsIgnoreCase("null")) {
            new SetupFrame();
        } else {
            new MainFrame(dbPath);
            //new MainStatFrame(mainFrame);
        }
    }
}

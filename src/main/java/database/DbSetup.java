package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbSetup extends BookDbConnection {

    private String coverDirectory;

    public DbSetup(Connection connection, String coverDirectory) {
        super(connection);
        this.coverDirectory = coverDirectory;
        createBooks();
        createGenres();
        fillGenres();
        createInfo();
        fillInfo();
        createSubgenres();
        createLanguages();
        fillLanguages();
    }

    private void createLanguages() {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE languages (" +
                    "Sprache Varchar(75))");

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillLanguages() {
        addLanguages("Deutsch", "Englisch", "Spanisch", "Französisch");
    }

    private void addLanguages(String... languages) {
        for (String language : languages) {
            addLanguage(language);
        }
    }
    private void addLanguage(String language) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO languages " +
                    "values (?)");
            statement.setString(1, language);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createSubgenres() {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE subgenres (" +
                    "Genre Varchar(75), Subgenre Varchar(75))");

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createInfo() {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE info (" +
                    "Name Varchar(100), Wert Varchar(75))");

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillInfo() {
        Map<String, String> infos = new HashMap<>();
        infos.put("erstesKriterium", "Genre");
        infos.put("zweitesKriterium", "Lesedatum");
        infos.put("BewertungAnzeigen", "true");
        infos.put("SeitenAnzeigen", "false");
        infos.put("ScrollZurücksetzen", "false");
        infos.put("font", "AR ESSENCE");
        infos.put("ersteFarbe", "102, 102, 102");
        infos.put("zweiteFarbe", "0, 0, 204");
        infos.put("fontFarbe", "0, 0, 0");
        infos.put("coverPfad", coverDirectory);
        for (String key : infos.keySet()) {
            addInfo(key, infos.get(key));
        }
    }

    private void addInfo(String info, String value) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO info " +
                    "values(?, ?)");
            statement.setString(1, info);
            statement.setString(2, value);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createGenres() {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE genres (" +
                    "genre Varchar(75))");

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillGenres() {
        addGenres("Epik", "Lyrik", "Drama", "Sachliteratur");
    }

    private void addGenres(String... genres) {
        for (String genre : genres) {
            addGenre(genre);
        }
    }
    private void addGenre(String genre) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO genres " +
                    "values (?)");
            statement.setString(1, genre);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createBooks() {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE books (" +
                    "Buch Varchar(75), Autor Varchar(75), Seitenzahl Int, Spannung Int, Mehrwert Int, Spass Int, Sprache Varchar(75), " +
                    "Verlag Varchar(75), Geschichte Int, Subgenre Varchar(75), Genre Varchar(75), Lesedatum Varchar(75), " +
                    "Sprachbewertung Int, ZweiterAutor Varchar(75), empfehlung Boolean)");

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

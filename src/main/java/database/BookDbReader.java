package database;

import bookManagement.Book;
import bookManagement.Genre;
import bookManagement.Subgenre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookDbReader extends BookDbConnection {

    public BookDbReader(Connection connection) {
        super(connection);
    }
    
    public ArrayList<Book> getBooks() {
        ArrayList<Book> books = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from Books");

            ResultSet bookSet = statement.executeQuery();
            while (bookSet.next()) {
                Book book = new Book();
                book.setName(bookSet.getString("Buch"));
                book.setAuthor(bookSet.getString("Autor"));
                book.setNumPages(bookSet.getInt("Seitenzahl"));
                book.setSubgenre(new Subgenre(bookSet.getString("genre"), bookSet.getString("Subgenre")));
                book.setTensionRat(bookSet.getInt("Spannung"));
                book.setInterestRat(bookSet.getInt("Mehrwert"));
                book.setStoryRat(bookSet.getInt("Geschichte"));
                book.setFunRat(bookSet.getInt("Spass"));
                book.setLanguage(bookSet.getString("Sprache"));
                book.setPubComp(bookSet.getString("Verlag"));
                book.setSecondAuthor(bookSet.getString("ZweiterAutor"));
                String readingDate = bookSet.getString("Lesedatum");
                if (readingDate == null) {
                    book.setReadingDate("00.0000");
                } else {
                    book.setReadingDate(readingDate);
                }
                book.setLanguageRat(bookSet.getInt("Sprachbewertung"));
                book.setRecommended(bookSet.getBoolean("Empfehlung"));

                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public ArrayList<String> getLanguages() {
        ArrayList<String> languages = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select distinct * from languages");

            ResultSet languageSet = statement.executeQuery();

            while (languageSet.next()) {
                languages.add(languageSet.getString("Sprache"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return languages;
    }

    public ArrayList<String> getAuthors() {
        ArrayList<String> authors = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select distinct autor from books");

            ResultSet authorSet = statement.executeQuery();

            while (authorSet.next()) {
                authors.add(authorSet.getString("Autor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    public ArrayList<String> getPubComps() {
        ArrayList<String> pubComps = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT verlag FROM books");

            ResultSet pubCompSet = statement.executeQuery();

            while (pubCompSet.next()) {
                if (!pubComps.contains(pubCompSet.getString("verlag").toUpperCase())) {
                    pubComps.add(pubCompSet.getString("verlag").toUpperCase());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pubComps;
    }

    public ArrayList<Genre> getGenres() {
        ArrayList<Genre> genres = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select distinct genre from genres");

            ResultSet genreSet = statement.executeQuery();

            while (genreSet.next()) {
                genres.add(new Genre(genreSet.getString("genre")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genres;
    }

    public ArrayList<Subgenre> getSubgenres() {
        ArrayList<Subgenre> genres = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select distinct genre, subgenre from subgenres");

            ResultSet genreSet = statement.executeQuery();

            while (genreSet.next()) {
                genres.add(new Subgenre(genreSet.getString("genre"), genreSet.getString("subgenre")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genres;
    }

    public String getInfo(String info) {
        ResultSet resultSet;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT wert FROM info WHERE name = ?");

            statement.setString(1, info);

            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString("wert");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package database;

import bookManagement.Book;
import bookManagement.Subgenre;

import java.sql.*;

public class BookDbWriter extends BookDbConnection{

    public BookDbWriter(Connection connection) {
        super(connection);
    }
    
    public void addBookToDb(Book book) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into books " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            int i = 1;
            statement.setString(i++, book.getName());
            statement.setString(i++, book.getAuthor());
            statement.setInt(i++, book.getNumPages());
            statement.setInt(i++, book.getTensionRat());
            statement.setInt(i++, book.getInterestRat());
            statement.setInt(i++, book.getFunRat());
            statement.setString(i++, book.getLanguage());
            statement.setString(i++, book.getPubComp());
            statement.setInt(i++, book.getStoryRat());
            statement.setString(i++, book.getSubgenre().getSubgenre());
            statement.setString(i++, book.getSubgenre().getGenre());
            statement.setString(i++, book.getReadingDate());
            statement.setInt(i++, book.getLanguageRat());
            statement.setString(i++, book.getSecondAuthor());
            statement.setBoolean(i, book.isRecommended());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGenreToDb(Subgenre subgenre) {
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate("insert into subgenres " +
                    "values ('" + subgenre.getGenre() + "', '" + subgenre.getSubgenre() + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(Book book) {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from books" +
                    " where Buch = ? and Autor = ? and Seitenzahl = ?");

            int i = 1;
            statement.setString(i++, book.getName());
            statement.setString(i++, book.getAuthor());
            statement.setInt(i, book.getNumPages());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGenre(Subgenre subgenre) {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from subgenres" +
                    " where genre = ? and subgenre = ?");

            int i = 1;
            statement.setString(i++, subgenre.getGenre());
            statement.setString(i, subgenre.getSubgenre());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book oldValues, Book newValues) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE books" +
                    " set Buch = ?, Autor = ?, Seitenzahl = ?, Subgenre = ?, Spannung = ?, Mehrwert = ?, Sprachbewertung = ?, Geschichte = ?," +
                    " Spass = ?, Sprache = ?, Verlag = ?, Lesedatum = ?, ZweiterAutor = ?, Empfehlung = ?" +
                    " WHERE Buch = ? AND Autor = ? AND Seitenzahl = ?");

            int i = 1;
            statement.setString(i++, newValues.getName());
            statement.setString(i++, newValues.getAuthor());
            statement.setInt(i++, newValues.getNumPages());
            statement.setString(i++, newValues.getSubgenre().getSubgenre());
            statement.setInt(i++, newValues.getTensionRat());
            statement.setInt(i++, newValues.getInterestRat());
            statement.setInt(i++, newValues.getLanguageRat());
            statement.setInt(i++, newValues.getStoryRat());
            statement.setInt(i++, newValues.getFunRat());
            statement.setString(i++, newValues.getLanguage());
            statement.setString(i++, newValues.getPubComp());
            statement.setString(i++, newValues.getReadingDate());
            statement.setString(i++, newValues.getSecondAuthor());
            statement.setBoolean(i++, newValues.isRecommended());

            statement.setString(i++, oldValues.getName());
            statement.setString(i++, oldValues.getAuthor());
            statement.setInt(i, oldValues.getNumPages());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateInfo(String info, String value) {
        try {
            PreparedStatement statement = connection.prepareStatement("update info" +
                    " set wert = ? where name = ?");

            int i = 1;
            statement.setString(i++, value);
            statement.setString(i, info);

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renameGenre(Subgenre subgenre, String newName) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE subgenres" +
                    " SET subgenre = ? WHERE genre = ? AND subgenre = ?");

            int i1 = 1;
            statement.setString(i1++, newName);
            statement.setString(i1++, subgenre.getGenre());
            statement.setString(i1, subgenre.getSubgenre());

            statement.executeUpdate();


            PreparedStatement statement2 = connection.prepareStatement("UPDATE books" +
                    " SET subgenre = ? WHERE genre = ? AND subgenre = ?");

            int i = 1;
            statement2.setString(i++, newName);

            statement2.setString(i++, subgenre.getGenre());
            statement2.setString(i, subgenre.getSubgenre());

            statement2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

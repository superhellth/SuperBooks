package database;

import java.sql.Connection;
import java.sql.SQLException;

public class BookDbConnection {

    public Connection connection;

    public BookDbConnection(Connection connection) {
        this.connection = connection;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

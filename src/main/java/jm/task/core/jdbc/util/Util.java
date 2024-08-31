package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД

    final static String DATABASE_URL = "jdbc:mysql://localhost/users";
    final static String USER = "root";
    final static String PASSWORD = "12345";

    private Util() {

    }

    public static Connection getConnection() {

        try {
            return DriverManager.getConnection (DATABASE_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException (e);
        }

    }
}

package jm.task.core.jdbc.dao;

import java.sql.*;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final Connection connection = Util.getConnection ();

    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement ()) {
            connection.setAutoCommit (false);
            statement.executeUpdate ("CREATE TABLE users (\n" + "  `id` INT NOT NULL AUTO_INCREMENT,\n" + "  `name` VARCHAR(50) NOT NULL,\n" + "  `lastname` VARCHAR(50) NOT NULL,\n" + "  `age` INT NOT NULL,\n" + "  PRIMARY KEY (`id`))");
            connection.commit ();
        } catch (SQLException e) {
            try {
                connection.rollback ();
            } catch (SQLException ex) {
                throw new RuntimeException (ex);
            }
            e.printStackTrace ();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement ()) {
            connection.setAutoCommit (false);
            statement.executeUpdate ("DROP TABLE users");
            connection.commit ();
        } catch (SQLException e) {
            e.printStackTrace ();
            try {
                connection.rollback ();
            } catch (SQLException ex) {
                throw new RuntimeException (ex);
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement ("INSERT INTO users(name, lastname, age) VALUES(?, ?, ?)")) {
            connection.setAutoCommit (false);
            preparedStatement.setString (1, name);
            preparedStatement.setString (2, lastName);
            preparedStatement.setInt (3, age);
            preparedStatement.executeUpdate ();
            System.out.println ("User с именем — " + name + " " + lastName + " добавлен в базу данных");
            connection.commit ();
        } catch (SQLException e) {
            e.printStackTrace ();
            try {
                connection.rollback ();
            } catch (SQLException ex) {
                throw new RuntimeException (ex);
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement ("DELETE FROM users WHERE id=?")) {
            connection.setAutoCommit (false);
            preparedStatement.setLong (1, id);
            preparedStatement.executeUpdate ();
            connection.commit ();
        } catch (SQLException e) {
            e.printStackTrace ();
            try {
                connection.rollback ();
            } catch (SQLException ex) {
                throw new RuntimeException (ex);
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<> ();
        try (Statement statement = connection.createStatement ()) {
            ResultSet resultSet = statement.executeQuery ("SELECT * FROM users");
            while (resultSet.next ()) {
                userList.add (new User (resultSet.getString ("name"), resultSet.getString ("lastname"), resultSet.getByte ("age")));
            }
        } catch (SQLException e) {
            e.printStackTrace ();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement ()) {
            statement.executeUpdate ("DELETE FROM users");
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }
}

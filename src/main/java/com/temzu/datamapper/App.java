package com.temzu.datamapper;

import com.temzu.datamapper.data.UserDataMapper;
import com.temzu.datamapper.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class App {

    private static Connection connection;

    public static void main(String[] args) {
        try {
            connect();
            prepareData();
            User user = new User(null, "Temzu");
            UserDataMapper userDataMapper = new UserDataMapper(connection);
            userDataMapper.insert(user);

            user = userDataMapper.findById(1L);
            System.out.println("First get: " + user);

            user.setLogin("Uzmet");
            userDataMapper.update(user);
            user = userDataMapper.findById(1L);
            System.out.println("Second get: " + user);

            userDataMapper.delete(user);
            user = userDataMapper.findById(1L);
            System.out.println("Third get: " + user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:javadb.db");
    }

    private static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void prepareData() throws SQLException {
        PreparedStatement statement1 = connection.prepareStatement(
                "DROP TABLE IF EXISTS users"
        );
        statement1.execute();

        PreparedStatement statement2 = connection.prepareStatement(
                        "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "login TEXT UNIQUE);"
        );
        statement2.executeUpdate();
    }
}

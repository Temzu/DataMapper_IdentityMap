package com.temzu.datamapper.data;

import com.temzu.datamapper.exceptions.InvalidRequestException;
import com.temzu.datamapper.exceptions.ResourceNotFoundException;
import com.temzu.datamapper.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataMapper {
    private final Connection connection;
    private final IdentityUserMap userMap;

    public UserDataMapper(Connection connection) {
        this.connection = connection;
        userMap = new IdentityUserMap();
    }

    public User findById(Long id) throws SQLException, ResourceNotFoundException {
        User user = userMap.getUser(id);
        if (user != null) {
            return user;
        }
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, login FROM users WHERE id = ?");
        statement.setLong(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong(1));
                user.setLogin(resultSet.getString(2));
                userMap.addUser(user);
                return user;
            }
        }
        return null;
    }

    public void insert(User user) throws Exception {
        String login = user.getLogin();
        if (login == null) {
            throw new InvalidRequestException("user login = " + login);
        }
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users (login) VALUES(?)");
        statement.setString(1, login);
        statement.execute();
    }

    public void update(User user) throws Exception {
        Long id = user.getId();
        String login = user.getLogin();
        if (id == null || login == null) {
            throw new InvalidRequestException("user id = " + id + " user login = " + login);
        }
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE users SET login = ? WHERE ID = ?");
        statement.setString(1, login);
        statement.setLong(2, id);
        if (statement.execute()) {
            throw new ResourceNotFoundException(String.valueOf(id));
        }
        User hashUser = userMap.getUser(id);
        if (hashUser != null) {
            hashUser.setLogin(login);
        }
    }

    public void delete(User user) throws Exception {
        Long id = user.getId();
        if (id == null) {
            throw new InvalidRequestException("user id = " + id);
        }
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM users WHERE id = ?");
        statement.setLong(1, id);
        if (statement.execute()) {
            throw new ResourceNotFoundException(String.valueOf(id));
        }
        if (userMap.getUser(id) != null) {
            userMap.removeUser(id);
        }
    }

}

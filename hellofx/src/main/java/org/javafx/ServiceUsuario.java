package org.javafx;

import org.javafx.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceUsuario {
    private ObservableList<Usuario> users;

    public ServiceUsuario() {
        users = FXCollections.observableArrayList();
        loadUsersFromDatabase();
    }
    private int nextId = 1;

    public ObservableList<Usuario> getUsers() {
        return users;
    }

    private void loadUsersFromDatabase() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT u.*, r.rol FROM \"user\" u JOIN rol r ON u.id_rol = r.id_rol")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Usuario user = new Usuario();
                user.setId(resultSet.getInt("id_user"));
                user.setName(resultSet.getString("nombre"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("telefono"));
                user.setCedula(resultSet.getString("cedula"));
                user.setRol(resultSet.getString("rol"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

    public void addUser(Usuario user) {
        String sql = "INSERT INTO \"user\" (nombre, email, telefono, cedula, id_rol) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getCedula());
            // Map role name to id_rol
            int idRol = getIdRolFromRolName(user.getRol());
            statement.setInt(5, idRol);
            statement.executeUpdate();
            users.add(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public void updateUser(Usuario updatedUser) {
        String sql = "UPDATE \"user\" SET nombre = ?, email = ?, telefono = ?, cedula = ?, id_rol = ? WHERE id_user = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedUser.getName());
            statement.setString(2, updatedUser.getEmail());
            statement.setString(3, updatedUser.getPhone());
            statement.setString(4, updatedUser.getCedula());
            int idRol = getIdRolFromRolName(updatedUser.getRol());
            statement.setInt(5, idRol);
            statement.setInt(6, updatedUser.getId());
            statement.executeUpdate();
            loadUsersFromDatabase(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM \"user\" WHERE id_user = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            users.removeIf(user -> user.getId() == userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public Usuario getUserById(int userId) {
        return users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElse(null);
    }

    private int getIdRolFromRolName(String rolName) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_rol FROM rol WHERE rol = ?")) {
            statement.setString(1, rolName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_rol");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Default to first role if not found
    }

    public ObservableList<String> getRoles() {
        ObservableList<String> roles = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT rol FROM rol")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                roles.add(resultSet.getString("rol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}

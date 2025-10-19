package org.javafx;

import org.javafx.Recomendacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceRecomendacion {
    private ObservableList<Recomendacion> recomendaciones;

    public ServiceRecomendacion() {
        recomendaciones = FXCollections.observableArrayList();
        loadRecomendacionesFromDatabase();
    }

    public ObservableList<Recomendacion> getRecomendaciones() {
        return recomendaciones;
    }

    private void loadRecomendacionesFromDatabase() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_recomendacion, nombre, descripcion, url FROM recomendacion")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Recomendacion recomendacion = new Recomendacion();
                recomendacion.setId(resultSet.getInt("id_recomendacion"));
                recomendacion.setNombre(resultSet.getString("nombre"));
                recomendacion.setDescripcion(resultSet.getString("descripcion"));
                recomendacion.setUrl(resultSet.getString("url"));
                recomendaciones.add(recomendacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRecomendacion(Recomendacion recomendacion) {
        String sql = "INSERT INTO recomendacion (nombre, descripcion, url) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, recomendacion.getNombre());
            statement.setString(2, recomendacion.getDescripcion());
            statement.setString(3, recomendacion.getUrl());
            statement.executeUpdate();

            // Get the generated id
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                recomendacion.setId(generatedKeys.getInt(1));
            }
            recomendaciones.add(recomendacion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRecomendacion(Recomendacion updatedRecomendacion) {
        String sql = "UPDATE recomendacion SET nombre = ?, descripcion = ?, url = ? WHERE id_recomendacion = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedRecomendacion.getNombre());
            statement.setString(2, updatedRecomendacion.getDescripcion());
            statement.setString(3, updatedRecomendacion.getUrl());
            statement.setInt(4, updatedRecomendacion.getId());
            statement.executeUpdate();
            loadRecomendacionesFromDatabase(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecomendacion(int recomendacionId) {
        String sql = "DELETE FROM recomendacion WHERE id_recomendacion = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, recomendacionId);
            statement.executeUpdate();
            recomendaciones.removeIf(recomendacion -> recomendacion.getId() == recomendacionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Recomendacion getRecomendacionById(int recomendacionId) {
        return recomendaciones.stream()
                .filter(recomendacion -> recomendacion.getId() == recomendacionId)
                .findFirst()
                .orElse(null);
    }
}

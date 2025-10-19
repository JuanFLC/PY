package org.javafx;

import org.javafx.Estanque;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class ServiceEstanque {
    private ObservableList<Estanque> estanques;

    public ServiceEstanque() {
        estanques = FXCollections.observableArrayList();
        loadEstanquesFromDatabase();
    }

    public ObservableList<Estanque> getEstanques() {
        return estanques;
    }

    private void loadEstanquesFromDatabase() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT id_estanque, capacidad, fecha_operacion, id_granja FROM estanque")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Estanque estanque = new Estanque();
                estanque.setId(resultSet.getInt("id_estanque"));
                estanque.setCapacidad(resultSet.getInt("capacidad"));
                estanque.setFechaOperacion(resultSet.getDate("fecha_operacion") != null ? resultSet.getDate("fecha_operacion").toLocalDate() : null);
                estanque.setIdGranja(resultSet.getInt("id_granja"));
                estanques.add(estanque);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEstanque(Estanque estanque) {
        String sql = "INSERT INTO estanque (id_estanque, capacidad, fecha_operacion, id_granja) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, estanque.getId());
            statement.setInt(2, estanque.getCapacidad());
            statement.setDate(3, estanque.getFechaOperacion() != null ? java.sql.Date.valueOf(estanque.getFechaOperacion()) : null);
            statement.setInt(4, estanque.getIdGranja());
            statement.executeUpdate();
            estanques.add(estanque);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEstanque(Estanque updatedEstanque) {
        String sql = "UPDATE estanque SET capacidad = ?, fecha_operacion = ?, id_granja = ? WHERE id_estanque = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, updatedEstanque.getCapacidad());
            statement.setDate(2, updatedEstanque.getFechaOperacion() != null ? java.sql.Date.valueOf(updatedEstanque.getFechaOperacion()) : null);
            statement.setInt(3, updatedEstanque.getIdGranja());
            statement.setInt(4, updatedEstanque.getId());
            statement.executeUpdate();
            loadEstanquesFromDatabase(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEstanque(int estanqueId) {
        String sql = "DELETE FROM estanque WHERE id_estanque = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, estanqueId);
            statement.executeUpdate();
            estanques.removeIf(estanque -> estanque.getId() == estanqueId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Estanque getEstanqueById(int estanqueId) {
        return estanques.stream()
                .filter(estanque -> estanque.getId() == estanqueId)
                .findFirst()
                .orElse(null);
    }

    public ObservableList<String> getGranjas() {
        ObservableList<String> granjas = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT nombre FROM granja")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                granjas.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return granjas;
    }

    public int getNextId() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COALESCE(MAX(id_estanque), 0) + 1 AS next_id FROM estanque")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
}

package org.javafx;

import org.javafx.Lote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ServiceLote {
    private ObservableList<Lote> lotes;

    public ServiceLote() {
        lotes = FXCollections.observableArrayList();
        loadLotesFromDatabase();
    }

    public ObservableList<Lote> getLotes() {
        return lotes;
    }

    private void loadLotesFromDatabase() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT l.id_lote, e.nombre as especie, l.cantidad, c.fecha_cosecha, c.id_estanque " +
                 "FROM lotes l LEFT JOIN especies e ON l.id_especie = e.id_especie " +
                 "LEFT JOIN cosecha c ON l.id_lote = c.id_lote")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Lote lote = new Lote();
                lote.setId(resultSet.getInt("id_lote"));
                lote.setEspecie(resultSet.getString("especie"));
                lote.setCantidad(resultSet.getInt("cantidad"));
                lote.setFechaRecoleccion(resultSet.getDate("fecha_cosecha") != null ? resultSet.getDate("fecha_cosecha").toLocalDate() : null);
                lote.setIdEstanque(resultSet.getInt("id_estanque"));
                lotes.add(lote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLote(Lote lote) {
        String sqlLote = "INSERT INTO lotes (id_lote, id_especie, cantidad) VALUES (?, (SELECT id_especie FROM especies WHERE nombre = ?), ?)";
        String sqlCosecha = "INSERT INTO cosecha (id_lote, id_estanque, fecha_cosecha) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection()) {
            // Insert into lotes
            try (PreparedStatement statement = connection.prepareStatement(sqlLote)) {
                statement.setInt(1, lote.getId());
                statement.setString(2, lote.getEspecie());
                statement.setInt(3, lote.getCantidad());
                statement.executeUpdate();
            }
            // Insert into cosecha if fecha is provided
            if (lote.getFechaRecoleccion() != null) {
                try (PreparedStatement statement = connection.prepareStatement(sqlCosecha)) {
                    statement.setInt(1, lote.getId());
                    statement.setInt(2, lote.getIdEstanque());
                    statement.setDate(3, java.sql.Date.valueOf(lote.getFechaRecoleccion()));
                    statement.executeUpdate();
                }
            }
            lotes.add(lote);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLote(Lote updatedLote) {
        String sqlLote = "UPDATE lotes SET id_especie = (SELECT id_especie FROM especies WHERE nombre = ?), cantidad = ? WHERE id_lote = ?";
        String sqlCosecha = "UPDATE cosecha SET fecha_cosecha = ? WHERE id_lote = ? AND id_estanque = ?";
        try (Connection connection = DatabaseUtil.getConnection()) {
            // Update lotes
            try (PreparedStatement statement = connection.prepareStatement(sqlLote)) {
                statement.setString(1, updatedLote.getEspecie());
                statement.setInt(2, updatedLote.getCantidad());
                statement.setInt(3, updatedLote.getId());
                statement.executeUpdate();
            }
            // Update cosecha
            try (PreparedStatement statement = connection.prepareStatement(sqlCosecha)) {
                statement.setDate(1, updatedLote.getFechaRecoleccion() != null ? java.sql.Date.valueOf(updatedLote.getFechaRecoleccion()) : null);
                statement.setInt(2, updatedLote.getId());
                statement.setInt(3, updatedLote.getIdEstanque());
                statement.executeUpdate();
            }
            loadLotesFromDatabase(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLote(int loteId) {
        String sqlCosecha = "DELETE FROM cosecha WHERE id_lote = ?";
        String sqlLote = "DELETE FROM lotes WHERE id_lote = ?";
        try (Connection connection = DatabaseUtil.getConnection()) {
            // Delete from cosecha first due to foreign key
            try (PreparedStatement statement = connection.prepareStatement(sqlCosecha)) {
                statement.setInt(1, loteId);
                statement.executeUpdate();
            }
            // Delete from lotes
            try (PreparedStatement statement = connection.prepareStatement(sqlLote)) {
                statement.setInt(1, loteId);
                statement.executeUpdate();
            }
            lotes.removeIf(lote -> lote.getId() == loteId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Lote getLoteById(int loteId) {
        return lotes.stream()
                .filter(lote -> lote.getId() == loteId)
                .findFirst()
                .orElse(null);
    }

    public ObservableList<String> getEspecies() {
        ObservableList<String> especies = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT nombre FROM especies")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                especies.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return especies;
    }


    public int getNextId() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COALESCE(MAX(id_lote), 0) + 1 AS next_id FROM lotes")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public ObservableList<String> getEstanques() {
        ObservableList<String> estanques = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_estanque FROM estanque")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                estanques.add(String.valueOf(resultSet.getInt("id_estanque")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estanques;
    }

    public ObservableList<String> getEstanquesWithNames() {
        ObservableList<String> estanques = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_estanque FROM estanque")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                estanques.add("Estanque " + resultSet.getInt("id_estanque"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estanques;
    }

    public void addImageToLote(int loteId, String fileName, String filePath, String especie, long size, int width, int height, String format, String user) {
        String sql = "INSERT INTO imagenes_pescados (nombre_archivo, ruta_archivo, especie, tamaño_archivo, ancho, alto, formato, usuario_subio, id_lote) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fileName);
            statement.setString(2, filePath);
            statement.setString(3, especie);
            statement.setLong(4, size);
            statement.setInt(5, width);
            statement.setInt(6, height);
            statement.setString(7, format);
            statement.setString(8, user);
            statement.setInt(9, loteId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> getImagesForLote(int loteId) {
        ObservableList<String> images = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT nombre_archivo FROM imagenes_pescados WHERE id_lote = ?")) {
            statement.setInt(1, loteId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                images.add(resultSet.getString("nombre_archivo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return images;
    }
}

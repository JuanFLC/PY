package org.javafx;

import org.javafx.Granja;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ServiceGranja {
    private ObservableList<Granja> granjas;

    public ServiceGranja() {
        granjas = FXCollections.observableArrayList();
        loadGranjasFromDatabase();
    }

    public ObservableList<Granja> getGranjas() {
        return granjas;
    }

    private void loadGranjasFromDatabase() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT g.id_granja, g.nombre, g.especies_cultivadas, c.nombre as ubicacion, g.id_ciudad " +
                 "FROM granja g LEFT JOIN ciudad c ON g.id_ciudad = c.id_ciudad")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Granja granja = new Granja();
                granja.setId(resultSet.getInt("id_granja"));
                granja.setNombre(resultSet.getString("nombre"));
                String[] especies = (String[]) resultSet.getArray("especies_cultivadas").getArray();
                granja.setEspeciesCultivadas(String.join(", ", especies));
                granja.setUbicacion(resultSet.getString("ubicacion"));
                granja.setIdCiudad(resultSet.getInt("id_ciudad"));
                granjas.add(granja);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGranja(Granja granja, List<String> especies) {
        String sql = "INSERT INTO granja (id_granja, nombre, especies_cultivadas, id_ciudad) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, granja.getId());
            statement.setString(2, granja.getNombre());
            statement.setArray(3, connection.createArrayOf("text", especies.toArray()));
            statement.setInt(4, granja.getIdCiudad());
            statement.executeUpdate();
            granjas.add(granja);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGranja(Granja updatedGranja, List<String> especies) {
        String sql = "UPDATE granja SET nombre = ?, especies_cultivadas = ?, id_ciudad = ? WHERE id_granja = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedGranja.getNombre());
            statement.setArray(2, connection.createArrayOf("text", especies.toArray()));
            statement.setInt(3, updatedGranja.getIdCiudad());
            statement.setInt(4, updatedGranja.getId());
            statement.executeUpdate();
            loadGranjasFromDatabase(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGranja(int granjaId) {
        String sql = "DELETE FROM granja WHERE id_granja = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, granjaId);
            statement.executeUpdate();
            granjas.removeIf(granja -> granja.getId() == granjaId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Granja getGranjaById(int granjaId) {
        return granjas.stream()
                .filter(granja -> granja.getId() == granjaId)
                .findFirst()
                .orElse(null);
    }

    public int getNextId() {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COALESCE(MAX(id_granja), 0) + 1 AS next_id FROM granja")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public ObservableList<String> getDepartamentos() {
        ObservableList<String> departamentos = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT nombre FROM departamento")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                departamentos.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departamentos;
    }

    public ObservableList<String> getMunicipiosByDepartamento(String departamento) {
        ObservableList<String> municipios = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT m.nombre FROM municipio m JOIN departamento d ON m.id_departamento = d.id_departamento WHERE d.nombre = ?")) {
            statement.setString(1, departamento);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                municipios.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return municipios;
    }

    public ObservableList<String> getCiudadesByMunicipio(String municipio) {
        ObservableList<String> ciudades = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT c.nombre FROM ciudad c JOIN municipio m ON c.id_municipio = m.id_municipio WHERE m.nombre = ?")) {
            statement.setString(1, municipio);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ciudades.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ciudades;
    }

    public int getIdCiudadByName(String ciudad) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_ciudad FROM ciudad WHERE nombre = ?")) {
            statement.setString(1, ciudad);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_ciudad");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getDepartamentoByCiudad(int idCiudad) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT d.nombre FROM departamento d JOIN municipio m ON d.id_departamento = m.id_departamento JOIN ciudad c ON m.id_municipio = c.id_municipio WHERE c.id_ciudad = ?")) {
            statement.setInt(1, idCiudad);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMunicipioByCiudad(int idCiudad) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT m.nombre FROM municipio m JOIN ciudad c ON m.id_municipio = c.id_municipio WHERE c.id_ciudad = ?")) {
            statement.setInt(1, idCiudad);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

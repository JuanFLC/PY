package org.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LotesInfoController implements Initializable {

    @FXML
    private TableView<LoteInfo> lotesTable;
    @FXML
    private TableColumn<LoteInfo, Integer> colId;
    @FXML
    private TableColumn<LoteInfo, String> colEspecie;
    @FXML
    private TableColumn<LoteInfo, Integer> colCantidad;
    @FXML
    private TableColumn<LoteInfo, String> colEstanque;
    @FXML
    private TableColumn<LoteInfo, String> colGranja;
    @FXML
    private TableColumn<LoteInfo, String> colUsuario;
    @FXML
    private TableColumn<LoteInfo, String> colFechaRecoleccion;
    @FXML
    private TableColumn<LoteInfo, String> colFechaOperacion;

    private ObservableList<LoteInfo> loteInfoList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadLoteInfoData();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colEstanque.setCellValueFactory(new PropertyValueFactory<>("estanque"));
        colGranja.setCellValueFactory(new PropertyValueFactory<>("granja"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colFechaRecoleccion.setCellValueFactory(new PropertyValueFactory<>("fechaRecoleccion"));
        colFechaOperacion.setCellValueFactory(new PropertyValueFactory<>("fechaOperacion"));
    }

    private void loadLoteInfoData() {
        loteInfoList.clear();
        String query = "SELECT DISTINCT l.id_lote, esp.nombre as especie, l.cantidad, " +
                       "est.id_estanque, g.nombre as granja, u.nombre as usuario, " +
                       "c.fecha_cosecha as fecha_recoleccion, est.fecha_operacion " +
                       "FROM lotes l " +
                       "LEFT JOIN especies esp ON l.id_especie = esp.id_especie " +
                       "LEFT JOIN cosecha c ON l.id_lote = c.id_lote " +
                       "LEFT JOIN estanque est ON c.id_estanque = est.id_estanque " +
                       "LEFT JOIN granja g ON est.id_granja = g.id_granja " +
                       "LEFT JOIN imagenes_pescados img ON l.id_lote = img.id_lote " +
                       "LEFT JOIN \"user\" u ON img.usuario_subio = u.nombre";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LoteInfo loteInfo = new LoteInfo();
                loteInfo.setId(resultSet.getInt("id_lote"));
                loteInfo.setEspecie(resultSet.getString("especie"));
                loteInfo.setCantidad(resultSet.getInt("cantidad"));
                loteInfo.setEstanque("Estanque " + resultSet.getInt("id_estanque"));
                loteInfo.setGranja(resultSet.getString("granja"));
                loteInfo.setUsuario(resultSet.getString("usuario"));
                loteInfo.setFechaRecoleccion(resultSet.getDate("fecha_recoleccion") != null ? resultSet.getDate("fecha_recoleccion").toString() : "");
                loteInfo.setFechaOperacion(resultSet.getDate("fecha_operacion") != null ? resultSet.getDate("fecha_operacion").toString() : "");
                loteInfoList.add(loteInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lotesTable.setItems(loteInfoList);
    }

    // Inner class for table data
    public static class LoteInfo {
        private int id;
        private String especie;
        private int cantidad;
        private String estanque;
        private String granja;
        private String usuario;
        private String fechaRecoleccion;
        private String fechaOperacion;

        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getEspecie() { return especie; }
        public void setEspecie(String especie) { this.especie = especie; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public String getEstanque() { return estanque; }
        public void setEstanque(String estanque) { this.estanque = estanque; }

        public String getGranja() { return granja; }
        public void setGranja(String granja) { this.granja = granja; }

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }

        public String getFechaRecoleccion() { return fechaRecoleccion; }
        public void setFechaRecoleccion(String fechaRecoleccion) { this.fechaRecoleccion = fechaRecoleccion; }

        public String getFechaOperacion() { return fechaOperacion; }
        public void setFechaOperacion(String fechaOperacion) { this.fechaOperacion = fechaOperacion; }
    }
}

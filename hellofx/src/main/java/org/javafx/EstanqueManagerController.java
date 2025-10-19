package org.javafx;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import org.javafx.Estanque;
import org.javafx.ServiceEstanque;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EstanqueManagerController implements Initializable {

    @FXML private TableView<Estanque> estanquesTable;
    @FXML private TableColumn<Estanque, Integer> colId;
    @FXML private TableColumn<Estanque, Integer> colCapacidad;
    @FXML private TableColumn<Estanque, LocalDate> colFecha;
    @FXML private TableColumn<Estanque, Integer> colIdGranja;

    @FXML private TextField txtCapacidad;
    @FXML private DatePicker dpFecha;
    @FXML private ChoiceBox<String> chGranja = new ChoiceBox<>();

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    private ServiceEstanque estanqueService;
    private Estanque selectedEstanque;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        estanqueService = new ServiceEstanque();
        setupTableColumns();
        loadEstanques();
        setupTableSelection();
        loadGranjas();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaOperacion"));
        colIdGranja.setCellValueFactory(new PropertyValueFactory<>("idGranja"));
    }

    private void loadEstanques() {
        estanquesTable.setItems(estanqueService.getEstanques());
    }

    private void setupTableSelection() {
        estanquesTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectEstanque(newValue);
                }
            });
    }

    private void selectEstanque(Estanque estanque) {
        selectedEstanque = estanque;
        txtCapacidad.setText(String.valueOf(estanque.getCapacidad()));
        dpFecha.setValue(estanque.getFechaOperacion());
        chGranja.setValue(getGranjaNameById(estanque.getIdGranja()));

        btnAdd.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
    }

    @FXML
    private void handleAddEstanque() {
        if (validateFields()) {
            Estanque estanque = new Estanque();
            estanque.setId(estanqueService.getNextId());
            estanque.setCapacidad(Integer.parseInt(txtCapacidad.getText()));
            estanque.setFechaOperacion(dpFecha.getValue());
            estanque.setIdGranja(getGranjaIdByName(chGranja.getValue()));

            estanqueService.addEstanque(estanque);
            clearFields();
        }
    }

    @FXML
    private void handleUpdateEstanque() {
        if (selectedEstanque != null && validateFields()) {
            selectedEstanque.setCapacidad(Integer.parseInt(txtCapacidad.getText()));
            selectedEstanque.setFechaOperacion(dpFecha.getValue());
            selectedEstanque.setIdGranja(getGranjaIdByName(chGranja.getValue()));

            estanqueService.updateEstanque(selectedEstanque);
            estanquesTable.refresh();
            clearFields();
        }
    }

    @FXML
    private void handleDeleteEstanque() {
        if (selectedEstanque != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText("¿Estás seguro de eliminar este estanque?");
            alert.setContentText("Esta acción no se puede deshacer.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                estanqueService.deleteEstanque(selectedEstanque.getId());
                clearFields();
            }
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
    }

    private void clearFields() {
        txtCapacidad.clear();
        dpFecha.setValue(null);
        chGranja.setValue(null);
        estanquesTable.getSelectionModel().clearSelection();

        selectedEstanque = null;
        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    private boolean validateFields() {
        if (txtCapacidad.getText().isEmpty() ||
            dpFecha.getValue() == null ||
            chGranja.getValue() == null) {

            showAlert("Error", "Todos los campos son obligatorios");
            return false;
        }
        try {
            Integer.parseInt(txtCapacidad.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Capacidad debe ser un número entero");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadGranjas() {
        chGranja.setItems(estanqueService.getGranjas());
    }

    private String getGranjaNameById(int idGranja) {
        // This is a simple implementation; in a real app, you might cache this or query the database
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT nombre FROM granja WHERE id_granja = ?")) {
            statement.setInt(1, idGranja);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int getGranjaIdByName(String nombre) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_granja FROM granja WHERE nombre = ?")) {
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_granja");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

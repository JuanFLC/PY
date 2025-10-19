package org.javafx;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import org.javafx.Granja;
import org.javafx.ServiceGranja;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class FarmManagerController implements Initializable {

    @FXML private TableView<Granja> farmsTable;
    @FXML private TableColumn<Granja, Integer> colId;
    @FXML private TableColumn<Granja, String> colName;
    @FXML private TableColumn<Granja, String> colEspecies;
    @FXML private TableColumn<Granja, String> colUbicacion;

    @FXML private TextField txtName;
    @FXML private CheckBox cbTilapia;
    @FXML private CheckBox cbCarpa;
    @FXML private CheckBox cbSalmon;
    @FXML private CheckBox cbBagre;
    @FXML private ChoiceBox<String> chDepartamento;
    @FXML private ChoiceBox<String> chMunicipio;
    @FXML private ChoiceBox<String> chCiudad;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    private ServiceGranja granjaService;
    private Granja selectedGranja;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        granjaService = new ServiceGranja();
        setupTableColumns();
        loadGranjas();
        setupTableSelection();
        loadDepartamentos();
        setupChoiceBoxListeners();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEspecies.setCellValueFactory(new PropertyValueFactory<>("especiesCultivadas"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
    }

    private void loadGranjas() {
        farmsTable.setItems(granjaService.getGranjas());
    }

    private void setupTableSelection() {
        farmsTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectGranja(newValue);
                }
            });
    }

    private void selectGranja(Granja granja) {
        selectedGranja = granja;
        txtName.setText(granja.getNombre());
        String[] especies = granja.getEspeciesCultivadas().split(", ");
        cbTilapia.setSelected(Arrays.asList(especies).contains("Tilapia roja (Mojarra)"));
        cbCarpa.setSelected(Arrays.asList(especies).contains("Carpa"));
        cbSalmon.setSelected(Arrays.asList(especies).contains("Salmón"));
        cbBagre.setSelected(Arrays.asList(especies).contains("Bagre"));

        String departamento = granjaService.getDepartamentoByCiudad(granja.getIdCiudad());
        String municipio = granjaService.getMunicipioByCiudad(granja.getIdCiudad());
        String ciudad = granja.getUbicacion();

        chDepartamento.setValue(departamento);
        chMunicipio.setItems(granjaService.getMunicipiosByDepartamento(departamento));
        chMunicipio.setValue(municipio);
        chCiudad.setItems(granjaService.getCiudadesByMunicipio(municipio));
        chCiudad.setValue(ciudad);

        btnAdd.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
    }

    @FXML
    private void handleAddGranja() {
        if (validateFields()) {
            Granja granja = new Granja();
            granja.setId(granjaService.getNextId());
            granja.setNombre(txtName.getText());
            List<String> especies = getSelectedEspecies();
            granja.setEspeciesCultivadas(String.join(", ", especies));
            granja.setIdCiudad(granjaService.getIdCiudadByName(chCiudad.getValue()));
            granja.setUbicacion(chCiudad.getValue());

            granjaService.addGranja(granja, especies);
            clearFields();
        }
    }

    @FXML
    private void handleUpdateGranja() {
        if (selectedGranja != null && validateFields()) {
            selectedGranja.setNombre(txtName.getText());
            List<String> especies = getSelectedEspecies();
            selectedGranja.setEspeciesCultivadas(String.join(", ", especies));
            selectedGranja.setIdCiudad(granjaService.getIdCiudadByName(chCiudad.getValue()));
            selectedGranja.setUbicacion(chCiudad.getValue());

            granjaService.updateGranja(selectedGranja, especies);
            farmsTable.refresh();
            clearFields();
        }
    }

    @FXML
    private void handleDeleteGranja() {
        if (selectedGranja != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText("¿Estás seguro de eliminar esta granja?");
            alert.setContentText("Esta acción no se puede deshacer.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                granjaService.deleteGranja(selectedGranja.getId());
                clearFields();
            }
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
    }

    private void clearFields() {
        txtName.clear();
        cbTilapia.setSelected(false);
        cbCarpa.setSelected(false);
        cbSalmon.setSelected(false);
        cbBagre.setSelected(false);
        chDepartamento.setValue(null);
        chMunicipio.setValue(null);
        chCiudad.setValue(null);
        farmsTable.getSelectionModel().clearSelection();

        selectedGranja = null;
        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    private boolean validateFields() {
        if (txtName.getText().isEmpty() ||
            chCiudad.getValue() == null ||
            getSelectedEspecies().isEmpty()) {

            showAlert("Error", "Todos los campos son obligatorios");
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

    private void loadDepartamentos() {
        chDepartamento.setItems(granjaService.getDepartamentos());
    }

    private void setupChoiceBoxListeners() {
        chDepartamento.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                chMunicipio.setItems(granjaService.getMunicipiosByDepartamento(newValue));
                chMunicipio.setValue(null);
                chCiudad.setValue(null);
            }
        });

        chMunicipio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                chCiudad.setItems(granjaService.getCiudadesByMunicipio(newValue));
                chCiudad.setValue(null);
            }
        });
    }

    private List<String> getSelectedEspecies() {
        List<String> especies = new ArrayList<>();
        if (cbTilapia.isSelected()) especies.add("Tilapia roja (Mojarra)");
        if (cbCarpa.isSelected()) especies.add("Carpa");
        if (cbSalmon.isSelected()) especies.add("Salmón");
        if (cbBagre.isSelected()) especies.add("Bagre");
        return especies;
    }
}

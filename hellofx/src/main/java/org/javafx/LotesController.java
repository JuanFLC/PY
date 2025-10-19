package org.javafx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.imageio.ImageIO;

import javafx.fxml.FXML;
import org.javafx.Lote;
import org.javafx.ServiceLote;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LotesController implements Initializable {

    @FXML private TableView<Lote> lotesTable;
    @FXML private TableColumn<Lote, Integer> colId;
    @FXML private TableColumn<Lote, String> colEspecie;
    @FXML private TableColumn<Lote, Integer> colCantidad;
    @FXML private TableColumn<Lote, LocalDate> colFecha;

    @FXML private ChoiceBox<String> chEspecie;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker dpFecha;
    @FXML private ChoiceBox<String> chEstanque;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private ListView<String> lvImages;
    @FXML private Button btnUploadImage;
    @FXML private Button btnViewImage;
    @FXML private ImageView imagePreview;

    private ServiceLote loteService;
    private Lote selectedLote;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loteService = new ServiceLote();
        setupTableColumns();
        loadLotes();
        setupTableSelection();
        loadEspecies();
        loadEstanquesWithNames();

        // Initially disable image buttons
        btnUploadImage.setDisable(true);
        btnViewImage.setDisable(true);

        // Set up image preview listener
        lvImages.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    loadImagePreview(newValue);
                } else {
                    clearImagePreview();
                }
            });
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaRecoleccion"));
    }

    private void loadLotes() {
        lotesTable.setItems(loteService.getLotes());
    }

    private void setupTableSelection() {
        lotesTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectLote(newValue);
                }
            });
    }

    private void selectLote(Lote lote) {
        selectedLote = lote;
        chEspecie.setValue(lote.getEspecie());
        txtCantidad.setText(String.valueOf(lote.getCantidad()));
        dpFecha.setValue(lote.getFechaRecoleccion());
        chEstanque.setValue("Estanque " + lote.getIdEstanque());

        btnAdd.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);

        // Enable image buttons since a lote is selected
        btnUploadImage.setDisable(false);
        btnViewImage.setDisable(false);

        // Load images for the selected lote
        lvImages.setItems(loteService.getImagesForLote(lote.getId()));
    }

    @FXML
    private void handleAddLote() {
        if (validateFields()) {
            Lote lote = new Lote();
            lote.setId(loteService.getNextId());
            lote.setEspecie(chEspecie.getValue());
            lote.setCantidad(Integer.parseInt(txtCantidad.getText()));
            lote.setFechaRecoleccion(dpFecha.getValue());
            lote.setIdEstanque(Integer.parseInt(chEstanque.getValue().replace("Estanque ", "")));

            loteService.addLote(lote);
            clearFields();
        }
    }

    @FXML
    private void handleUpdateLote() {
        if (selectedLote != null && validateFields()) {
            selectedLote.setEspecie(chEspecie.getValue());
            selectedLote.setCantidad(Integer.parseInt(txtCantidad.getText()));
            selectedLote.setFechaRecoleccion(dpFecha.getValue());
            selectedLote.setIdEstanque(Integer.parseInt(chEstanque.getValue().replace("Estanque ", "")));

            loteService.updateLote(selectedLote);
            lotesTable.refresh();
            clearFields();
        }
    }

    @FXML
    private void handleDeleteLote() {
        if (selectedLote != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText("¿Estás seguro de eliminar este lote?");
            alert.setContentText("Esta acción no se puede deshacer.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                loteService.deleteLote(selectedLote.getId());
                clearFields();
            }
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
    }

    private void clearFields() {
        chEspecie.setValue(null);
        txtCantidad.clear();
        dpFecha.setValue(null);
        chEstanque.setValue(null);
        lvImages.getItems().clear();
        lotesTable.getSelectionModel().clearSelection();

        selectedLote = null;
        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        // Disable image buttons when no lote is selected
        btnUploadImage.setDisable(true);
        btnViewImage.setDisable(true);

        // Clear image preview
        clearImagePreview();
    }

    private boolean validateFields() {
        if (chEspecie.getValue() == null ||
            txtCantidad.getText().isEmpty() ||
            dpFecha.getValue() == null ||
            chEstanque.getValue() == null) {

            showAlert("Error", "Todos los campos son obligatorios");
            return false;
        }
        try {
            Integer.parseInt(txtCantidad.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Cantidad debe ser un número entero");
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

    private void loadEspecies() {
        chEspecie.setItems(loteService.getEspecies());
    }

    private void loadEstanquesWithNames() {
        chEstanque.setItems(loteService.getEstanquesWithNames());
    }

    @FXML
    private void handleUploadImage() {
        if (selectedLote == null) {
            showAlert("Error", "Selecciona un lote primero");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        Stage stage = (Stage) btnUploadImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Generate unique filename
                String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String uniqueName = UUID.randomUUID().toString() + extension;

                // Copy to images folder
                Path destPath = Paths.get("hellofx", "images", uniqueName);
                Files.createDirectories(destPath.getParent());
                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                // Get image dimensions
                BufferedImage img = ImageIO.read(selectedFile);
                int width = img.getWidth();
                int height = img.getHeight();

                // Save to DB
                loteService.addImageToLote(selectedLote.getId(), uniqueName, destPath.toString(), selectedLote.getEspecie(),
                    selectedFile.length(), width, height, extension.substring(1), "user"); // Assuming user is "user"

                // Refresh list
                lvImages.setItems(loteService.getImagesForLote(selectedLote.getId()));

            } catch (IOException e) {
                showAlert("Error", "Error al subir la imagen: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleViewImage() {
        String selectedImage = lvImages.getSelectionModel().getSelectedItem();
        if (selectedImage == null) {
            showAlert("Error", "Selecciona una imagen primero");
            return;
        }

        try {
            Path imagePath = Paths.get("hellofx", "images", selectedImage);
            if (Files.exists(imagePath)) {
                // Open image with default viewer
                java.awt.Desktop.getDesktop().open(imagePath.toFile());
            } else {
                showAlert("Error", "Imagen no encontrada");
            }
        } catch (IOException e) {
            showAlert("Error", "Error al abrir la imagen: " + e.getMessage());
        }
    }

    private void loadImagePreview(String imageName) {
        try {
            Path imagePath = Paths.get("hellofx", "images", imageName);
            if (Files.exists(imagePath)) {
                Image image = new Image(imagePath.toUri().toString());
                imagePreview.setImage(image);
            } else {
                clearImagePreview();
            }
        } catch (Exception e) {
            clearImagePreview();
        }
    }

    private void clearImagePreview() {
        imagePreview.setImage(null);
    }
}

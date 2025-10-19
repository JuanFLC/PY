package org.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import org.javafx.Usuario;
import org.javafx.ServiceUsuario;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserManagerController implements Initializable {

    @FXML private TableView<Usuario> usersTable;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colName;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colPhone;
    @FXML private TableColumn<Usuario, String> colCedula;
    @FXML private TableColumn<Usuario, String> colRol;

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private ChoiceBox<String> ChRol = new ChoiceBox<>();
    @FXML private TextField txtCedula;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    private ServiceUsuario userService;
    private Usuario selectedUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = new ServiceUsuario();
        setupTableColumns();
        loadUsers();
        setupTableSelection();
        loadRoles();
    }


      private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
    }

    private void loadUsers() {
        usersTable.setItems(userService.getUsers());
    }

    private void setupTableSelection() {
        usersTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectUser(newValue);
                }
            });
    }

    private void selectUser(Usuario user) {
        selectedUser = user;
        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhone());
        txtCedula.setText(user.getCedula());
        ChRol.setValue(user.getRol());

        btnAdd.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
    }

    @FXML
    private void handleAddUser() {
        if (validateFields()) {
            Usuario user = new Usuario();
            user.setName(txtName.getText());
            user.setEmail(txtEmail.getText());
            user.setPhone(txtPhone.getText());
            user.setCedula(txtCedula.getText());
            user.setRol(ChRol.getValue());

            userService.addUser(user);
            clearFields();
        }
    }

    @FXML
    private void handleUpdateUser() {
        if (selectedUser != null && validateFields()) {
            selectedUser.setName(txtName.getText());
            selectedUser.setEmail(txtEmail.getText());
            selectedUser.setPhone(txtPhone.getText());
            selectedUser.setCedula(txtCedula.getText());
            selectedUser.setRol(ChRol.getValue());

            userService.updateUser(selectedUser);
            usersTable.refresh();
            clearFields();
        }
    }

    @FXML
    private void handleDeleteUser() {
        if (selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText("¿Estás seguro de eliminar este usuario?");
            alert.setContentText("Esta acción no se puede deshacer.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                userService.deleteUser(selectedUser.getId());
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
        txtEmail.clear();
        txtPhone.clear();
        txtCedula.clear();
        ChRol.setValue(null);
        usersTable.getSelectionModel().clearSelection();

        selectedUser = null;
        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    private boolean validateFields() {
        if (txtName.getText().isEmpty() ||
            txtEmail.getText().isEmpty() ||
            txtPhone.getText().isEmpty() ||
            txtCedula.getText().isEmpty() ||
            ChRol.getValue() == null) {

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

    private void loadRoles() {
        ChRol.setItems(userService.getRoles());
    }

}

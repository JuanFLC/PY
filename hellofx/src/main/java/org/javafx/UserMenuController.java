package org.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.Stage;

import java.io.IOException;

public class UserMenuController {

    @FXML
    private SplitMenuButton userRoleMenu;
    @FXML
    private MenuItem adminMenuItem;
    @FXML
    private MenuItem piscicultorMenuItem;
    @FXML
    private MenuItem tecnicoMenuItem;

    @FXML
    private void selectRole(javafx.event.ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String role = selectedItem.getText();
        App.setCurrentUserRole(role);
        userRoleMenu.setText(role);

        // Navigate to main menu
        try {
            App.setVistaActual("mainmenu");
            App.scene.setRoot(App.loadFXML("mainmenu"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

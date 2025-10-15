package org.javafx;

import java.io.IOException;

import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    void switchToLotes() throws  IOException {
        App.setRoot("lotes");
    }

    @FXML
    private void switchToUserManager() throws IOException {
        App.setRoot("usermanager");
    }
}

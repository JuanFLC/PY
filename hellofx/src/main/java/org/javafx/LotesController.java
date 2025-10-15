package org.javafx;

import java.io.IOException;

import javafx.fxml.FXML;

public class LotesController {
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainmenu");
    }
}

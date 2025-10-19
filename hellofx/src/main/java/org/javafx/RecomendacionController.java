package org.javafx;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class RecomendacionController implements Initializable {

    @FXML private GridPane mosaicPane;

    private ServiceRecomendacion recomendacionService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recomendacionService = new ServiceRecomendacion();
        loadRecomendaciones();
    }

    private void loadRecomendaciones() {
        var recomendaciones = recomendacionService.getRecomendaciones();
        mosaicPane.getChildren().clear();
        int column = 0;
        int row = 0;
        for (Recomendacion rec : recomendaciones) {
            VBox card = createCard(rec);
            mosaicPane.add(card, column, row);
            column++;
            if (column == 3) { // 3 columns per row
                column = 0;
                row++;
            }
        }
    }

    private VBox createCard(Recomendacion rec) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9; -fx-background-radius: 5; -fx-cursor: hand;");
        card.setPrefWidth(500);
        card.setPrefHeight(300);

        Label nameLabel = new Label(rec.getNombre());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        String desc = rec.getDescripcion();
        if (desc == null || desc.trim().isEmpty()) {
            desc = "Descripción no disponible";
        }
        Label descLabel = new Label(desc);
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(500);
        descLabel.setStyle("-fx-font-size: 14px;");

        Hyperlink urlLink = new Hyperlink("Ver más...");
        urlLink.setStyle("-fx-font-size: 14px;");
        urlLink.setOnAction(e -> showDetailsDialog(rec));

        card.setOnMouseClicked(e -> showDetailsDialog(rec));

        card.getChildren().addAll(nameLabel, descLabel, urlLink);
        return card;
    }

    private void showDetailsDialog(Recomendacion rec) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de Recomendación");
        alert.setHeaderText(rec.getNombre());

        String desc = rec.getDescripcion();
        if (desc == null || desc.trim().isEmpty()) {
            desc = "Descripción no disponible";
        }
        Label descLabel = new Label(desc);
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(400);

        Hyperlink urlLink = new Hyperlink(rec.getUrl());
        urlLink.setOnAction(e -> openUrl(rec.getUrl()));

        VBox content = new VBox(10, descLabel, urlLink);
        alert.getDialogPane().setContent(content);

        alert.showAndWait();
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            showAlert("Error", "No se pudo abrir la URL: " + url);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

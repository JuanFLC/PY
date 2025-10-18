package org.javafx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.net.URL;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainMenuController implements  Initializable {

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TreeView<String> treeMenuList;
    @FXML
    private VBox contentArea;

    private Map<String, String> vistaMap = new HashMap<>();


    @Override
    public void initialize(URL var1, ResourceBundle var2)
    {
        loadTreeList();
        configurarListeners();
    }

    private void loadTreeList(){
        TreeItem<String> root = new TreeItem<>("Gestionar");
        root.setExpanded(true);
        TreeItem<String> branch1 = new TreeItem<>("Credenciales y creacion de entidades");
        TreeItem<String> branch2 = new TreeItem<>("Trazabilidad");
        TreeItem<String> branch3 = new TreeItem<>("Recomendaciones");
        
        TreeItem<String> leaf1 = new TreeItem<>("Usuarios");
        TreeItem<String> leaf2 = new TreeItem<>("Lotes");
        TreeItem<String> leaf3 = new TreeItem<>("Granjas");

        TreeItem<String> leaf4 = new TreeItem<>("Reportes");
        TreeItem<String> leaf5 = new TreeItem<>("Registro alimentario");
        TreeItem<String> leaf6 = new TreeItem<>("Inventario");


        branch1.getChildren().addAll(leaf1,leaf2,leaf3);
        branch2.getChildren().addAll(leaf4,leaf5,leaf6);
        root.getChildren().addAll(branch1, branch2, branch3);

        treeMenuList.setRoot(root);
        treeMenuList.setShowRoot(false);


        vistaMap.put("Usuarios", "usermanager");
        vistaMap.put("Lotes", "lotes");
        vistaMap.put("Granjas", "farmmanager");
    }

    private void configurarListeners() {
        treeMenuList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null && !newValue.getValue().equals("Gestionar") 
                    && !newValue.getChildren().isEmpty()) {
                    // Es una categoría, no hacer nada o expandir/colapsar
                    newValue.setExpanded(!newValue.isExpanded());
                } else if (newValue != null && vistaMap.containsKey(newValue.getValue())) {
                    loadMiddleView(vistaMap.get(newValue.getValue()));
                }
            }
        );
    }

    @FXML
    public void loadMiddleView(String fxml)
    {
        try {
            if(fxml.equals(App.getVistaActual())) return;

            Parent vista = App.loadFXML(fxml);

            Object controlador = App.getFXMLLoader().getController();
            App.setControladores(fxml, controlador);
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(vista);   

        } catch (IOException e) {
            e.printStackTrace();
            App.mostrarError("Error al cargar vista: " + fxml);
        }
        

    }

     public <T> T getControlador(String fxmlPath, Class<T> tipo) {
        Object controlador = App.getControllers().get(fxmlPath);
        return tipo.isInstance(controlador) ? tipo.cast(controlador) : null;
    }

    @FXML
    private void loadVistaLotes(){
        loadMiddleView("lotes.fxml");
    }

    @FXML
    private void loadVistaUsuarios(){
        loadMiddleView("usermanager.fxml");
    }

}

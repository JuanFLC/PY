package org.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    private static Map<String, Object> controladores = new HashMap<>();
    private static String vistaActual;
    private static FXMLLoader fxmlLoader;
    private static String currentUserRole;



    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("usermenu"), 640, 480);
        stage.setScene(scene);
        stage.show();

    }
    static FXMLLoader getFXMLLoader()
    {
        return fxmlLoader;
    }
    
    static Parent loadFXML(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();    
    }

    public static String getVistaActual(){
        return vistaActual;    
    }

    static public Map<String,Object> getControllers(){
        
      return controladores;  
    }

    static void setVistaActual(String fxml){
        vistaActual = fxml;
    }

    static void setControladores(String fxml, Object controlador)
    {
        controladores.put(fxml, controlador);
    }

    public static void mostrarError(String mensaje) {
        // Implementar manejo de errores
        System.err.println(mensaje);
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    public static void setCurrentUserRole(String role) {
        currentUserRole = role;
    }

    public static void main(String[] args) {
        launch();
    }

}
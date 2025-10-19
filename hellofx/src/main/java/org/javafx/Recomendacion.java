package org.javafx;

import javafx.beans.property.*;

public class Recomendacion {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty descripcion;
    private final StringProperty url;

    public Recomendacion() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
        this.url = new SimpleStringProperty();
    }

    public Recomendacion(int id, String nombre, String descripcion, String url) {
        this();
        setId(id);
        setNombre(nombre);
        setDescripcion(descripcion);
        setUrl(url);
    }

    // Getters y Setters para propiedades
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String value) { nombre.set(value); }
    public StringProperty nombreProperty() { return nombre; }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String value) { descripcion.set(value); }
    public StringProperty descripcionProperty() { return descripcion; }

    public String getUrl() { return url.get(); }
    public void setUrl(String value) { url.set(value); }
    public StringProperty urlProperty() { return url; }
}

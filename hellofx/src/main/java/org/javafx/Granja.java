package org.javafx;

import javafx.beans.property.*;

public class Granja {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty especiesCultivadas;
    private final StringProperty ubicacion;
    private final IntegerProperty idCiudad;

    public Granja() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.especiesCultivadas = new SimpleStringProperty();
        this.ubicacion = new SimpleStringProperty();
        this.idCiudad = new SimpleIntegerProperty();
    }

    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String value) { nombre.set(value); }
    public StringProperty nombreProperty() { return nombre; }

    public String getEspeciesCultivadas() { return especiesCultivadas.get(); }
    public void setEspeciesCultivadas(String value) { especiesCultivadas.set(value); }
    public StringProperty especiesCultivadasProperty() { return especiesCultivadas; }

    public String getUbicacion() { return ubicacion.get(); }
    public void setUbicacion(String value) { ubicacion.set(value); }
    public StringProperty ubicacionProperty() { return ubicacion; }

    public int getIdCiudad() { return idCiudad.get(); }
    public void setIdCiudad(int value) { idCiudad.set(value); }
    public IntegerProperty idCiudadProperty() { return idCiudad; }
}

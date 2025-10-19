package org.javafx;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Lote {
    private final IntegerProperty id;
    private final StringProperty especie;
    private final IntegerProperty cantidad;
    private final ObjectProperty<LocalDate> fechaRecoleccion;
    private final IntegerProperty idEstanque;

    public Lote() {
        this.id = new SimpleIntegerProperty();
        this.especie = new SimpleStringProperty();
        this.cantidad = new SimpleIntegerProperty();
        this.fechaRecoleccion = new SimpleObjectProperty<>();
        this.idEstanque = new SimpleIntegerProperty();
    }

    public Lote(int id, String especie, int cantidad, LocalDate fechaRecoleccion) {
        this();
        setId(id);
        setEspecie(especie);
        setCantidad(cantidad);
        setFechaRecoleccion(fechaRecoleccion);
    }

    // Getters y Setters para propiedades
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getEspecie() { return especie.get(); }
    public void setEspecie(String value) { especie.set(value); }
    public StringProperty especieProperty() { return especie; }

    public int getCantidad() { return cantidad.get(); }
    public void setCantidad(int value) { cantidad.set(value); }
    public IntegerProperty cantidadProperty() { return cantidad; }

    public LocalDate getFechaRecoleccion() { return fechaRecoleccion.get(); }
    public void setFechaRecoleccion(LocalDate value) { fechaRecoleccion.set(value); }
    public ObjectProperty<LocalDate> fechaRecoleccionProperty() { return fechaRecoleccion; }

    public int getIdEstanque() { return idEstanque.get(); }
    public void setIdEstanque(int value) { idEstanque.set(value); }
    public IntegerProperty idEstanqueProperty() { return idEstanque; }

}

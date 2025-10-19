package org.javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

public class Estanque {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty capacidad = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> fechaOperacion = new SimpleObjectProperty<>();
    private final IntegerProperty idGranja = new SimpleIntegerProperty();

    public Estanque() {}

    public Estanque(int id, int capacidad, LocalDate fechaOperacion, int idGranja) {
        setId(id);
        setCapacidad(capacidad);
        setFechaOperacion(fechaOperacion);
        setIdGranja(idGranja);
    }

    // id property
    public IntegerProperty idProperty() { return id; }
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    // capacidad property
    public IntegerProperty capacidadProperty() { return capacidad; }
    public int getCapacidad() { return capacidad.get(); }
    public void setCapacidad(int capacidad) { this.capacidad.set(capacidad); }

    // fechaOperacion property
    public ObjectProperty<LocalDate> fechaOperacionProperty() { return fechaOperacion; }
    public LocalDate getFechaOperacion() { return fechaOperacion.get(); }
    public void setFechaOperacion(LocalDate fechaOperacion) { this.fechaOperacion.set(fechaOperacion); }

    // idGranja property
    public IntegerProperty idGranjaProperty() { return idGranja; }
    public int getIdGranja() { return idGranja.get(); }
    public void setIdGranja(int idGranja) { this.idGranja.set(idGranja); }

    @Override
    public String toString() {
        return "Estanque " + getId();
    }
}

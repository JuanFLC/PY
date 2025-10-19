package org.javafx;

import javafx.beans.property.*;



public class Usuario {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty cedula;
    private final StringProperty rol;

    
    public Usuario() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.cedula = new SimpleStringProperty();
        this.rol = new SimpleStringProperty();
    }

    public Usuario(int id, String name, String email, String phone, String cedula, String rol) {
        this();
        setId(id);
        setName(name);
        setEmail(email);
        setPhone(phone);
        setCedula(cedula);
        setRol(rol);
    }

    public enum TypeUser
    {
        Administrador("Administrador"),
        Piscicultor("Piscicultor"),
        Tecnico("Tecnico");

        private final String user;

        private TypeUser(String typeUser)
        {
            this.user = typeUser;
        }

        public String getTypeUser() {
            return user;
        }
    }

    // Getters y Setters para propiedades
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }

    public String getPhone() { return phone.get(); }
    public void setPhone(String value) { phone.set(value); }
    public StringProperty phoneProperty() { return phone; }

    public String getCedula() { return cedula.get(); }
    public void setCedula(String value) { cedula.set(value); }
    public StringProperty CedulaProperty() { return cedula; }

    public String getRol() { return rol.get(); }
    public void setRol(String value) { rol.set(value); }
    public StringProperty RolProperty(){return rol;}
    
}
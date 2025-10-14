package org.javafx;

import javafx.beans.property.*;


public class Usuario {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty department;

    public Usuario() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.department = new SimpleStringProperty();
    }

    public Usuario(int id, String name, String email, String phone, String department) {
        this();
        setId(id);
        setName(name);
        setEmail(email);
        setPhone(phone);
        setDepartment(department);
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

    public String getDepartment() { return department.get(); }
    public void setDepartment(String value) { department.set(value); }
    public StringProperty departmentProperty() { return department; }
}
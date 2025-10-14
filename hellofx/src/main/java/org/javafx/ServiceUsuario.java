package org.javafx;

import org.javafx.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServiceUsuario {
     private ObservableList<Usuario> users;
    private int nextId = 1;

    public ServiceUsuario() {
        users = FXCollections.observableArrayList();
        // Datos de ejemplo
        addUser(new Usuario(nextId++, "Juan Pérez", "juan@email.com", "123456789", "Ventas"));
        addUser(new Usuario(nextId++, "María García", "maria@email.com", "987654321", "TI"));
    }

    public ObservableList<Usuario> getUsers() {
        return users;
    }

    public void addUser(Usuario user) {
        user.setId(nextId++);
        users.add(user);
    }

    public void updateUser(Usuario updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                break;
            }
        }
    }

    public void deleteUser(int userId) {
        users.removeIf(user -> user.getId() == userId);
    }

    public Usuario getUserById(int userId) {
        return users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElse(null);
    }
}

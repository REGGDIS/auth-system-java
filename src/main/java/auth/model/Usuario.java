package auth.model;

import javafx.beans.property.*;

public class Usuario {
    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty correo;
    private StringProperty contrasena;
    private StringProperty rol;

    public Usuario() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.correo = new SimpleStringProperty();
        this.contrasena = new SimpleStringProperty();
        this.rol = new SimpleStringProperty();
    }

    public Usuario(int id, String nombre, String correo, String rol) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.correo = new SimpleStringProperty(correo);
        this.rol = new SimpleStringProperty(rol);
        this.contrasena = new SimpleStringProperty();
    }

    // Getters and setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getCorreo() {
        return correo.get();
    }

    public void setCorreo(String correo) {
        this.correo.set(correo);
    }

    public String getContrasena() {
        return contrasena.get();
    }

    public void setContrasena(String contrasena) {
        this.contrasena.set(contrasena);
    }

    public String getRol() {
        return rol.get();
    }

    public void setRol(String rol) {
        this.rol.set(rol);
    }

    // Métodos Property necesarios para TableView
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public StringProperty correoProperty() {
        return correo;
    }

    public StringProperty contrasenaProperty() {
        return contrasena;
    }

    public StringProperty rolProperty() {
        return rol;
    }
}

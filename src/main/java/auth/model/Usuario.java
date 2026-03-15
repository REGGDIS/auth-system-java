package auth.model;

import javafx.beans.property.*;

public class Usuario {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private StringProperty correo = new SimpleStringProperty();
    private StringProperty contrasena = new SimpleStringProperty();
    private StringProperty rol = new SimpleStringProperty();

    public Usuario() {}

    public Usuario(int id, String nombre, String correo, String rol) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.correo.set(correo);
        this.rol.set(rol);
    }

    public Usuario(String nombre, String correo, String contrasena, String rol) {
        this.nombre.set(nombre);
        this.correo.set(correo);
        this.contrasena.set(contrasena);
        this.rol.set(rol);
    }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty correoProperty() { return correo; }
    public StringProperty contrasenaProperty() { return contrasena; }
    public StringProperty rolProperty() { return rol; }

    // Standard getters and setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    public String getCorreo() { return correo.get(); }
    public void setCorreo(String correo) { this.correo.set(correo); }

    public String getContrasena() { return contrasena.get(); }
    public void setContrasena(String contrasena) { this.contrasena.set(contrasena); }

    public String getRol() { return rol.get(); }
    public void setRol(String rol) { this.rol.set(rol); }
}

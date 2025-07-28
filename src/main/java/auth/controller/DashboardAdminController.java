package auth.controller;

import auth.model.Usuario;
import auth.util.ConexionBD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardAdminController {

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, Integer> colId;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colCorreo;

    @FXML
    private TableColumn<Usuario, String> colRol;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtRol;

    @FXML
    private Label lblTituloFormulario;

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    private Usuario usuarioSeleccionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCorreo.setCellValueFactory(cellData -> cellData.getValue().correoProperty());
        colRol.setCellValueFactory(cellData -> cellData.getValue().rolProperty());

        tablaUsuarios.setItems(listaUsuarios);
        cargarUsuarios();

        // Al hacer clic en una fila, cargar datos en los campos
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                usuarioSeleccionado = newSel;
                txtNombre.setText(newSel.getNombre());
                txtCorreo.setText(newSel.getCorreo());
                txtRol.setText(newSel.getRol());
                lblTituloFormulario.setText("Editar Usuario");
            }
        });
    }

    private void cargarUsuarios() {
        listaUsuarios.clear();
        try (Connection conn = ConexionBD.conectar()) {
            String query = "SELECT * FROM usuarios";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("rol")
                );
                listaUsuarios.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void guardarCambios() {
        String nuevoNombre = txtNombre.getText().trim();
        String nuevoCorreo = txtCorreo.getText().trim();
        String nuevoRol = txtRol.getText().trim();

        if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty() || nuevoRol.isEmpty()) {
            mostrarAlerta("Todos los campos deben estar completos.");
            return;
        }

        try (Connection conn = ConexionBD.conectar()) {
            if (usuarioSeleccionado == null) {
                // Insertar nuevo Usuario
                String contrasenaTemporal = "temporal123";
                String hash = BCrypt.hashpw(contrasenaTemporal, BCrypt.gensalt());

                String sql = "INSERT INTO usuarios (nombre, correo, rol, contrasena) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nuevoNombre);
                stmt.setString(2, nuevoCorreo);
                stmt.setString(3, nuevoRol);
                stmt.setString(4, hash);

                int filas = stmt.executeUpdate();
                if (filas > 0) {
                    mostrarInfo("Nuevo usuario creado correctamente. Contraseña temporal: temporal123");
                    cargarUsuarios();
                }

            } else {
                // Actualizar Usuario existente
                String sql = "UPDATE usuarios SET nombre = ?, correo = ?, rol = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nuevoNombre);
                stmt.setString(2, nuevoCorreo);
                stmt.setString(3, nuevoRol);
                stmt.setInt(4, usuarioSeleccionado.getId());

                int filas = stmt.executeUpdate();
                if (filas > 0) {
                    // Actualizar el objeto local
                    usuarioSeleccionado.setNombre(nuevoNombre);
                    usuarioSeleccionado.setCorreo(nuevoCorreo);
                    usuarioSeleccionado.setRol(nuevoRol);
                    tablaUsuarios.refresh();
                    mostrarInfo("Usuario actualizado correctamente.");
                }
            }

            // Limpiar formulario y resetear selección
            usuarioSeleccionado = null;
            txtNombre.clear();
            txtCorreo.clear();
            txtRol.clear();
            tablaUsuarios.getSelectionModel().clearSelection();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al actualizar el usuario.");
        }
    }

    @FXML
    public void nuevoUsuario() {
        usuarioSeleccionado = null;
        txtNombre.clear();
        txtCorreo.clear();
        txtRol.clear();
        tablaUsuarios.getSelectionModel().clearSelection();
        lblTituloFormulario.setText("Agregar Nuevo Usuario");
    }

    @FXML
    public void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) tablaUsuarios.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void eliminarUsuario() {
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Por favor selecciona un usuario para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de que deseas eliminar al usuario seleccionado?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                try (Connection conn = ConexionBD.conectar()) {
                    String sql = "DELETE FROM usuarios WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, usuarioSeleccionado.getId());

                    int filas = stmt.executeUpdate();
                    if (filas > 0) {
                        listaUsuarios.remove(usuarioSeleccionado);
                        mostrarInfo("Usuario eliminado correctamente.");
                        usuarioSeleccionado = null;
                        txtNombre.clear();
                        txtCorreo.clear();
                        txtRol.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta("Error al eliminar el usuario.");
                }
            }
        });
    }
}

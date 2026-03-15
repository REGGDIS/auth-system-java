package auth.controller;

import auth.service.AuthService;
import auth.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistroController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblMensaje;

    private final AuthService authService = new AuthService();

    @FXML
    public void handleRegistro(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor, complete todos los campos.");
            return;
        }

        boolean success = authService.registrar(nombre, correo, contrasena);

        if (success) {
            lblMensaje.setText("Usuario registrado exitosamente.");
        } else {
            lblMensaje.setText("Error al registrar (el correo ya existe o problema de BD).");
        }
    }

    @FXML
    public void volverAlLogin(ActionEvent event) {
        SceneManager.switchScene("/login.fxml", "Login");
    }
}

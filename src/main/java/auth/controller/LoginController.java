package auth.controller;

import auth.model.Usuario;
import auth.service.AuthService;
import auth.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblMensaje;

    private final AuthService authService = new AuthService();

    @FXML
    public void handleLogin(ActionEvent event) {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor, complete todos los campos.");
            return;
        }

        Optional<Usuario> loginResult = authService.login(correo, contrasena);

        if (loginResult.isPresent()) {
            Usuario usuario = loginResult.get();
            lblMensaje.setText("Inicio de sesión exitoso.");

            // Redirigir según el rol
            String vista = usuario.getRol().equals("admin") ? "/dashboard_admin.fxml" : "/dashboard_user.fxml";
            String titulo = "Panel - " + usuario.getRol().toUpperCase();
            SceneManager.switchScene(vista, titulo);
        } else {
            lblMensaje.setText("Correo o contraseña incorrectos.");
        }
    }

    @FXML
    public void irARegistro(ActionEvent event) {
        SceneManager.switchScene("/registro.fxml", "Registro de Usuario");
    }
}

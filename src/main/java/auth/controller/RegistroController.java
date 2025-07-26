package auth.controller;

import auth.util.ConexionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistroController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblMensaje;

    @FXML
    public void handleRegistro(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor, complete todos los campos.");
            return;
        }

        try (Connection conn = ConexionBD.conectar()) {
            // Verificar si el correo ya existe
            String checkQuery = "SELECT * FROM usuarios WHERE correo = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, correo);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                lblMensaje.setText("Este correo ya está registrado.");
                return;
            }

            // Insertar nuevo usuario (rol por defecto: user)
            String hashed = BCrypt.hashpw(contrasena, BCrypt.gensalt(10));
            String insertQuery = "INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, 'user')";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, nombre);
            insertStmt.setString(2, correo);
            insertStmt.setString(3, hashed);
            insertStmt.executeUpdate();

            lblMensaje.setText("Usuario registrado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error al registrar usuario.");
        }
    }

    @FXML
    public void volverAlLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error al volver al login.");
        }
    }
}

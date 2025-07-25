package auth.controller;

import auth.model.Usuario;
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

public class LoginController {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblMensaje;

    @FXML
    public void handleLogin(ActionEvent event) {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor, complete todos los campos.");
            return;
        }

        try (Connection conn = ConexionBD.conectar()) {
            String query = "SELECT * FROM usuarios WHERE correo = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, correo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashed = rs.getString("contrasena");
                String rol = rs.getString("rol");

                System.out.println("Contraseña ingresada: " + contrasena);
                System.out.println("Hash desde la BD: " + hashed);
                System.out.println("Comparación BCrypt: " + BCrypt.checkpw(contrasena, hashed));

                if (BCrypt.checkpw(contrasena, hashed)) {
                    lblMensaje.setText("Inicio de sesión exitoso.");

                    // Redirigir según el rol
                    String vista = rol.equals("admin") ? "/dashboard_admin.fxml" : "/dashboard_user.fxml";
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(vista));
                    Stage stage = (Stage) txtCorreo.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.setTitle("Panel - " + rol.toUpperCase());
                    stage.show();
                } else {
                    lblMensaje.setText("Contraseña incorrecta.");
                }
            } else {
                lblMensaje.setText("Usuario no encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error al conectar a la base de datos.");
        }
    }
}

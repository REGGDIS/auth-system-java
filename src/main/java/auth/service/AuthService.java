package auth.service;

import auth.model.Usuario;
import auth.util.ConexionBD;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AuthService {

    public Optional<Usuario> login(String correo, String contrasena) {
        String query = "SELECT * FROM usuarios WHERE correo = ?";
        
        try (Connection conn = ConexionBD.conectar()) {
            if (conn == null) return Optional.empty();
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashed = rs.getString("contrasena");
                if (BCrypt.checkpw(contrasena, hashed)) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setContrasena(hashed);
                    usuario.setRol(rs.getString("rol"));
                    return Optional.of(usuario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean registrar(String nombre, String correo, String contrasena) {
        if (existeCorreo(correo)) return false;

        String hashed = BCrypt.hashpw(contrasena, BCrypt.gensalt(10));
        String query = "INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, 'user')";

        try (Connection conn = ConexionBD.conectar()) {
            if (conn == null) return false;

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, hashed);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean existeCorreo(String correo) {
        String query = "SELECT id FROM usuarios WHERE correo = ?";
        try (Connection conn = ConexionBD.conectar()) {
            if (conn == null) return false;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

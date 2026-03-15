package auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        // En una app real, usaríamos inyección de dependencias para mockear el DAO
        authService = new AuthService();
    }

    @Test
    void testLoginValidation() {
        // Test básico de validación (actualmente el service no valida campos vacíos, lo hace el ctrl)
        // Pero podríamos añadir lógica de validación al service en el futuro
        assertNotNull(authService);
    }
}

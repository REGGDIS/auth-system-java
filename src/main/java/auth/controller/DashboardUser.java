package auth.controller;

import auth.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardUser {

    @FXML
    public void cerrarSesion(ActionEvent event) {
        SceneManager.switchScene("/login.fxml", "Login");
    }
}

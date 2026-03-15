package auth;

import atlantafx.base.theme.PrimerDark;
import auth.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        
        SceneManager.setStage(primaryStage);
        SceneManager.switchScene("/login.fxml", "Sistema de Autenticación");
        primaryStage.setResizable(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

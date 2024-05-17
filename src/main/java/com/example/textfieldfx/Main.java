package com.example.textfieldfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource("/com/example/textfieldfx/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login Form");

        // Set the application icon using a relative path
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/images/Football_AppIcon.png")).toExternalForm());
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
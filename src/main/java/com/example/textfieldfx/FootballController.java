package com.example.textfieldfx;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class FootballController {

    @FXML
    public ImageView backToCurrency_ImageView;

    @FXML
    public ImageView laliga_ImageView;
    public ImageView forwardToPL_ImageView; // controls back button and pl IV
    public AnchorPane footballHome_AnchorPane;


    private void applySceneTransition(Parent root) {
        // Create a scale transition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), root);
        scaleTransition.setFromX(0.5); // starting scale factor
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1.0);   // ending scale factor
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }


    public void forwardToPrem(MouseEvent e){

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/textfieldfx/PremierLeague_Football.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            applySceneTransition(root); // Apply transition || @note may change to after Parent root = fxml.load();
            stage.show();

        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void forwardToLaLiga(MouseEvent e){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/textfieldfx/LaLiga_Football.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            applySceneTransition(root); // Apply transition || @note may change to after Parent root = fxml.load();
            stage.show();

        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


}

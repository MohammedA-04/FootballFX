package com.example.textfieldfx;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Objects;

// Resp: Controls Login Form Page 1
public class Controller {

    @FXML
    private Label myLabel;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button myButton;


    int age;
    int yearsLeft=18;
    String usrUsername = "Mo";
    String usrpassword = "abc123";
    String user, pass;

    public void submit(ActionEvent event){

        try{
            age = Integer.parseInt(ageTextField.getText());
            user = usernameTextField.getText();
            pass = passwordTextField.getText();

            if (age>= 18){
                System.out.println("You are old enough: " +age);

                if (user.equals(usrUsername) && pass.equals(usrpassword)){

                    myLabel.setText("Success");

                    //   /resources/HomePage.css
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/textfieldfx/Football.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    System.out.println("Username or Password is incorrect");
                }

            } else {
                yearsLeft = yearsLeft - age;
                myLabel.setText("Cannot sign: need " + yearsLeft + "years");
            }

        } catch (NumberFormatException NFE){
            myLabel.setText("Enter only numbers: e.g., 47");

        } catch (Exception e){
            e.printStackTrace();
           myLabel.setText("Unknown Error: Text Field requires numbers" + e.getMessage());
        }


    }
}
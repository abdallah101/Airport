package sample.objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Ports_Controller;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class Utilities
{
        public void goToUserHomeScreen(User currentUser, ActionEvent event) throws IOException {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("sample/ports.fxml"));

            Parent root = null;

            try {
                root = loader.load();
            }catch (IOException e)
            {e.printStackTrace();}
            //root = loader.load(getClass().getClassLoader().getResource("sample/ports.fxml"));

            Scene newScene = new Scene(root);
            Ports_Controller homeScreenController = loader.getController();
            homeScreenController.loadUserData(currentUser);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(newScene);
            window.setResizable(false);
            window.show();

//            Parent ports = FXMLLoader.load(getClass().getResource("ports.fxml"));
//            Stage window = (Stage)((Node)action1.getSource()).getScene().getWindow();
//            window.setScene(new Scene(ports, 600, 500));
//            window.show();
        }
}


package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
//import java.awt.event.ActionEvent;
import java.io.IOException;

public class Ports_Controller {
    public void GoBack (ActionEvent backwards) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("javafx.fxml"));
        Stage window = (Stage)((Node)backwards.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 600, 500));
        window.show();
    }
}

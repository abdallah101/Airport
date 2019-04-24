package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class REG_SUCCESS_Controller {

    public void GoToLogin (ActionEvent backwards) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("javafx.fxml"));
        Stage window = (Stage)((Node)backwards.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 600, 500));
        window.show();
    }

}

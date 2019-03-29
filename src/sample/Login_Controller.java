package sample;

import Connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class Login_Controller {
    public PasswordField PassField;
    public TextField EmailBox;
    public Text Failure;

    public void registerhere(ActionEvent actionEvent) throws Exception {

        Parent root1 = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(new Scene (root1, 600, 500));
        window.show();
    }

    public void Login (ActionEvent action1) throws Exception {

        //Setting connection
        ConnectionClass connectionclass = new ConnectionClass();
        Connection connection = connectionclass.getConnection();

        //Getting Username and password from login screen
        String SQL = "SELECT Username, Password FROM USER WHERE Username = '"+EmailBox.getText()+"' && Password = '"+PassField.getText()+"' ";
        Statement stmt = connection.createStatement();

        //setting a boolean to true if login and password combination is found
        ResultSet rs = stmt.executeQuery(SQL);

        //if combination not found or at least one of the boxes is empty then fail
        if(!rs.next() || EmailBox.getText().isEmpty() || PassField.getText().isEmpty())
        {
            Failure.setText("Username or Password Incorrect, Try Again.");
        }
        //If succeeded then go to AirPort main Page
        else
        {
            Parent ports = FXMLLoader.load(getClass().getResource("ports.fxml"));
            Stage window = (Stage)((Node)action1.getSource()).getScene().getWindow();
            window.setScene(new Scene(ports, 600, 500));
        }
    }
}

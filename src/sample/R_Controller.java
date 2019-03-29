package sample;

import Connectivity.ConnectionClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class R_Controller {

    public TextField R_Email;
    public TextField R_Name;
    public PasswordField R_Password;
    public CheckBox NotRobot;
    public Text batata;

    public void Back2Login (ActionEvent goback) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("javafx.fxml"));
        Stage window = (Stage)((Node)goback.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 600, 500));
        window.show();
    }

    public void submit (ActionEvent sub) throws Exception {

        //Creating Connection
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        //Creating statement with username, password, and full name of client
        String sql="INSERT INTO USER (Username,Password,Name) VALUES('"+R_Email.getText()+"','"+R_Password.getText()+"','"+R_Name.getText()+"')";
        Statement statement = connection.createStatement();

        //Getting a boolean to whether the username is already being used
        String SQL = "SELECT Username, Password FROM USER WHERE Username = '"+R_Email.getText()+"'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);


        //Check if any box is empty
        if (R_Email.getText().isEmpty() || R_Name.getText().isEmpty() || R_Password.getText().isEmpty() )
        {
            batata.setText("One or more fields is empty");
        }
        //check is username already being used in database
        else if (rs.next())
        {
            batata.setText("Username already used, try again.");
        }
        //check if human or not
        else if(NotRobot.isSelected()) {

            statement.executeUpdate(sql);

            Parent root = FXMLLoader.load(getClass().getResource("javafx.fxml"));
            Stage window = (Stage) ((Node) sub.getSource()).getScene().getWindow();
            window.setScene(new Scene(root, 600, 500));
            window.show();
        }
        //if not human then display this message
        else
        {
            batata.setText("Check Box First Please!");
        }

    }



}

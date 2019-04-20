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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class R_Controller {

    public TextField R_Email;
    public TextField R_Name;
    public PasswordField R_Password;
    public CheckBox NotRobot;
    public Text batata;
    private Socket client;
    private boolean reg = false;
    public void Back2Login (ActionEvent goback) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("javafx.fxml"));
        Stage window = (Stage)((Node)goback.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 600, 500));
        window.show();
    }

    public void submit (ActionEvent sub) throws Exception {



        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        client = new Socket(serverName, port);

        OutputStream outToServer = client.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToServer);
        out.writeUTF("2" + "\n" + R_Email.getText() + "\n" + R_Password.getText() + "\n" + R_Name.getText());


        //Getting input
        DataInputStream in = new DataInputStream(client.getInputStream());
        reg = in.readBoolean();
        out.close();
        in.close();
        client.close();
        System.out.println("Connection closed");


        //Check if any box is empty
        if (R_Email.getText().isEmpty() || R_Name.getText().isEmpty() || R_Password.getText().isEmpty() )
        {
            batata.setText("One or more fields is empty");
        }
        //check is username already being used in database
        else if (reg == false)
        {
            batata.setText("Username already used, try again.");
        }
        //check if human or not
        else if(NotRobot.isSelected()) {

            System.out.println("User successfully registered");
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

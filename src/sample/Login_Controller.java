package sample;

import com.sun.deploy.ref.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Ports_Controller;
import sample.objects.User;
import sample.objects.Appmodel;
import java.io.*;
import java.net.Socket;

public class Login_Controller {
    public PasswordField PassField;
    public TextField EmailBox;
    public Text Failure;
    public boolean found;
    private Socket client;
    private User model ;




    public void registerhere(ActionEvent actionEvent) throws Exception {

        Parent root1 = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(new Scene (root1, 600, 500));
        window.show();
    }

    public void Login (ActionEvent action1) throws Exception, IOException {

        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);

            client = new Socket(serverName, port);
            System.out.println("Connection Successful");

            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("1" + "\n" + EmailBox.getText() + "\n" + PassField.getText());
            System.out.println("output sent");

            //Getting input


            DataInputStream in = new DataInputStream(client.getInputStream());

            found = in.readBoolean();
            
            out.close();
            in.close();
            client.close();
            System.out.println("Connection closed");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //if combination not found or at least one of the boxes is empty then fail
        if( found == false || EmailBox.getText().isEmpty() || PassField.getText().isEmpty())
        {
            Failure.setText("Username or Password Incorrect, Try Again.");
        }
        //If succeeded then go to AirPort main Page
        else
        {
            Parent ports = FXMLLoader.load(getClass().getResource("ports.fxml"));
            Stage window = (Stage)((Node)action1.getSource()).getScene().getWindow();
            window.setScene(new Scene(ports, 600, 500));
            window.show();

        }


    }


}

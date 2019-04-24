package sample;

import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import sun.security.util.Resources_pt_BR;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class R_Controller {

    public TextField R_Email;
    public TextField R_Name;
    public PasswordField R_Password;
    public CheckBox NotRobot;
    public Text indicator;
    private Socket client;
    private boolean reg = false;
    public void Back2Login (ActionEvent goback) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("javafx.fxml"));
        Stage window = (Stage)((Node)goback.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 600, 500));
        window.setResizable(false);
        window.show();
    }

    public void submit (ActionEvent sub) throws Exception {



        if (R_Email.getText().isEmpty() || R_Name.getText().isEmpty() || R_Password.getText().isEmpty())
        {
            disError("One or more fields is empty");
        }
        else if (!R_Name.getText().matches("^[ A-Za-z]+$"))
        {
            disError("Your name has an invalid character in it");
        }
        else if (R_Password.getLength() < 6)
        {
            disError("Password needs to be greater than 6 characters long");
        }
        else if (R_Password.getText().matches("[1-9]+"))
        {
            disError("Password can't be numbers only");
        }
        else if (R_Password.getText().matches("[a-zA-Z]*$"))
        {
            disError("Password should include numbers");
        }
        else if (!NotRobot.isSelected())
        {
            disError("Check Box First Please!");
        }
        else {
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

            if (reg == false)
            {
                disError("Username already being used, try again.");
            }
            else
            {
                Parent root = FXMLLoader.load(getClass().getResource("Reg_success.fxml"));
                Stage window = (Stage) ((Node) sub.getSource()).getScene().getWindow();
                window.setScene(new Scene(root, 600, 500));
                window.setResizable(false);
                window.show();
            }

        }


    }
    public void disError (String error)
    {
        indicator.setText(error);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(4));
        visiblePause.setOnFinished(e -> { indicator.setVisible(false); });
        visiblePause.play();
        indicator.setVisible(true);
    }



}

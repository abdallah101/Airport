package sample;

import com.sun.deploy.ref.AppModel;
import javafx.scene.control.TextArea;
import sample.Login_Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import sample.objects.User;
import sun.rmi.runtime.Log;
//import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;




public class Ports_Controller implements Initializable {

    public String GATE;
    public String TIMESLOT;
    private Socket client;
    private User model ;
    




    @FXML
    public ComboBox <String> combobox;
    public ComboBox <String> timebox;



    ObservableList<String> list1 = FXCollections.observableArrayList();
    ObservableList<String> list2 = FXCollections.observableArrayList();
    ObservableList<String> list3 = FXCollections.observableArrayList();
    ObservableList<String> list4 = FXCollections.observableArrayList();

    ObservableList<String> list = FXCollections.observableArrayList("gate_one", "gate_two", "gate_three", "gate_four");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combobox.setItems(list);
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println(model.getUsername());

        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {

            client = new Socket(serverName, port);
            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("5");//tell me who i am
            //Getting input
            DataInputStream in = new DataInputStream(client.getInputStream());

            out.close();
            in.close();
            client.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void refresh () throws Exception
    {
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {


            client = new Socket(serverName, port);


            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("3");
            //Getting input
            DataInputStream in = new DataInputStream(client.getInputStream());

            String re = in.readUTF();
            BufferedReader reader = new BufferedReader(new StringReader(re));

            String next = reader.readLine();
            list1.clear();
            while (true)
            {
                next = reader.readLine();
                if(Integer.parseInt(next) == 222)
                {break;}
                list1.addAll(convertit(Integer.parseInt(next)));
            }
            list2.clear();
            while (true)
            {
                next = reader.readLine();
                if(Integer.parseInt(next) == 333)
                {break;}
                list2.addAll(convertit(Integer.parseInt(next)));
            }
            list3.clear();
            while (true)
            {
                next = reader.readLine();
                if(Integer.parseInt(next) == 444)
                {break;}
                list3.addAll(convertit(Integer.parseInt(next)));
            }
            list4.clear();
            while (true)
            {
                next = reader.readLine();
                if(next == null)
                {break;}
                list4.addAll(convertit(Integer.parseInt(next)));
            }

            out.close();
            in.close();
            client.close();
            System.out.println("Refreshed");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void comboChanged (ActionEvent change)
    {
        if(combobox.getValue() == "gate_one") {
            //System.out.println("G1");
            //timebox.getItems().clear();
            GATE = "1";
            timebox.setItems(list1);

        }
        else if (combobox.getValue() == "gate_two")
        {
            //System.out.println("G2");
            //timebox.getItems().clear();
            GATE = "2";
            timebox.setItems(list2);
        }
        else if (combobox.getValue() == "gate_three")
        {
            //System.out.println("G3");
            //timebox.getItems().clear();
            GATE = "3";
            timebox.setItems(list3);
        }
        else if (combobox.getValue() == "gate_four")
        {
            //System.out.println("G4");
            //timebox.getItems().clear();
            GATE = "4";
            timebox.setItems(list4);
        }
        else
        {}
    }

    public void ChangeTimeSlot (ActionEvent cts)
    {
        if (timebox.getValue() == "12am to 4am")
        {
            TIMESLOT = "1";
        }
        else if (timebox.getValue() == "4am to 8am")
        {
            TIMESLOT = "2";
        }
        else if (timebox.getValue() == "8am to 12pm")
        {
            TIMESLOT = "3";
        }
        else if (timebox.getValue() == "12pm to 4pm")
        {
            System.out.println("success");
            TIMESLOT = "4";
        }
        else if (timebox.getValue() == "4pm to 8pm")
        {
            TIMESLOT = "5";
        }
        else if (timebox.getValue() == "8pm to 12am")
        {
            TIMESLOT = "6";
        }
        else
        {}
    }

    public void CtimeSlots (ActionEvent SeeTimeSlots) throws Exception
    {
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {


            client = new Socket(serverName, port);


            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("4" + "\n" + GATE + "\n" + TIMESLOT);
            //Getting input
            DataInputStream in = new DataInputStream(client.getInputStream());

            out.close();
            in.close();
            client.close();
            System.out.println("Reservation successful");
            refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void GoBack (ActionEvent backwards) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("javafx.fxml"));
        Stage window = (Stage)((Node)backwards.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 600, 500));
        window.show();
    }

    public String convertit (int num)
    {
        String actual;
        if(num == 4)
        {
            actual = "12am to 4am";
        }
        else if (num == 8)
        {
            actual = "4am to 8am";
        }
        else if (num == 12)
        {
            actual = "8am to 12pm";
        }
        else if (num == 16)
        {
            actual = "12pm to 4pm";
        }
        else if (num == 20)
        {
            actual = "4pm to 8pm";
        }
        else if (num == 24)
        {
            actual = "8pm to 12am";
        }
        else
        {
            actual = "error";
        }

        return actual;


    }




}

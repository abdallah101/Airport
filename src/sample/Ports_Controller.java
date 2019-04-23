package sample;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import sample.objects.User;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Ports_Controller implements Initializable
{

    public String GATE;
    public String TIMESLOT;
    public Text reserve_success;
    public TextField STime_slot;
    public ListView listview;
    private Socket client;
    private User currentUser ;
    @FXML
    public ComboBox <String> combobox;
    public ComboBox <String> timebox;
    ObservableList<String> list1 = FXCollections.observableArrayList();
    ObservableList<String> list2 = FXCollections.observableArrayList();
    ObservableList<String> list3 = FXCollections.observableArrayList();
    ObservableList<String> list4 = FXCollections.observableArrayList();
    ObservableList<String> list = FXCollections.observableArrayList("gate_one", "gate_two", "gate_three", "gate_four");
    ObservableList<String> gatess = FXCollections.observableArrayList();

    //Passing username from Login_Controller to port_controller
    public void loadUserData(User CU)
    {
        this.currentUser = CU;
        refresh();
    }

    //Setting combobox with all Gates
    //Giving instructions for users to find particular timeslot in any gate
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        combobox.setItems(list);
        Label l = new Label("Input a number between\n0 and 24, Gates\ncorresponding " +
                "to a time\ninterval with that number\nin it will be displayed");
        l.setOpacity(10);
        listview.setPlaceholder(l);
    }

    //Function to remove all unavailable time slots from combo box related to time slots
    //keeping the available timeslots for each gate respectively
    public void refresh ()
    {
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {
            client = new Socket(serverName, port);

            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("3" + "\n" + currentUser.getUsername());

            //Getting input
            DataInputStream in = new DataInputStream(client.getInputStream());
            String re = in.readUTF();
            BufferedReader reader = new BufferedReader(new StringReader(re));
            String next = reader.readLine();

            //Clearing then adding new time slots that are available with respect to the right gate
            listview.getItems().clear();
            list1.clear();
            Label l = new Label("Input a number between\n0 and 24, Gates\ncorresponding " +
                    "to a time\ninterval with that number\nin it will be displayed");
            l.setOpacity(10);
            listview.setPlaceholder(l);
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

    //Changing time box content according to the gate combobox content
    //Setting as well GATE so as to send it to the server, so that the server knows what gate is being asked for
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
        {
            GATE = null;
        }
    }

    //Same as with GATE but with TIMESLOT so that server knows what timeslot is being asked for
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
        {
            TIMESLOT = null;
        }
    }

    //Function for reserving a timeslot for a particular gate
    public void CtimeSlots (ActionEvent SeeTimeSlots) throws Exception
    {
        //if the gate or time combobox is empty then give this error message which disappears after a delay
        if(GATE == null || TIMESLOT == null)
        {
            disError("Gate or TimeSlot hasn't been chosen");

        }
        else
        {
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {

            client = new Socket(serverName, port);

            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("4" + "\n" + currentUser.getUsername() + "\n" + GATE + "\n" + TIMESLOT);

            //Getting input
            DataInputStream in = new DataInputStream(client.getInputStream());
            String reserved = in.readUTF();

            if(Integer.parseInt(reserved) == 2)
            {
                //Server sends back 2 if user has exceeded his reservation limit
                disError("You have exceeded your reservation limit");
            }
            else if(Integer.parseInt((reserved))== 3)
            {
                //if an error occurred on server side, it returns 3 and an error is displayed (Contact admin)
                disError("Error, Try again!");
            }
            else if (Integer.parseInt((reserved)) == 4)
            {
                //if it happens and user didnt refresh and someone else already reserved the time slot
                //then this error message will appear
                disError("Error: Someone Beat you to it\n time slot already reserved");
            }
            else
            {
                disError("Successfully Reserved!");
            }

            out.close();
            in.close();
            client.close();
            //System.out.println("Reservation successful");
            refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    //Function to display the history of a user with the server
    public void History (ActionEvent event)
    {
        Stage stage = new Stage();
        WebView browser = new WebView();
        WebEngine we = browser.getEngine();
        ScrollPane sc = new ScrollPane();
        sc.setFitToHeight(true);
        sc.setFitToWidth(true);
        sc.setContent(browser);
        we.loadContent(getLogFiles());
        VBox vb = new VBox(10);
        vb.getChildren().add(sc);
        Scene scene = new Scene(vb,500,500);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    //Logout button
    public void GoBack (ActionEvent backwards) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("javafx.fxml"));
        Stage window = (Stage)((Node)backwards.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 600, 500));
        window.setResizable(false);
        window.show();
    }

    //Error display function with timeout
    public void disError (String error)
    {
        reserve_success.setText(error);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
        visiblePause.setOnFinished(e -> { reserve_success.setVisible(false); });
        visiblePause.play();
        reserve_success.setVisible(true);
    }

    //Function to convert server's database indication of a time interval to an appropriate representation
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

    //Function to retrieve a specific user's history with the server
    //user can't get another user's history, username sent is the one the person logged in with
    public String getLogFiles ()
    {
        String history = null;
        String temp= null;
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {
            client = new Socket(serverName, port);

            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("5" + "\n" + currentUser.getUsername());
            System.out.println("here");
            //Getting input
            DataInputStream in = new DataInputStream(client.getInputStream());
            String re = in.readUTF();
            BufferedReader reader = new BufferedReader(new StringReader(re));

            history = reader.readLine();
            while (true)
            {
                temp = reader.readLine();
                if(temp == null)
                {
                    break;
                }
                history = history + "<br>\n" + temp;

            }
            out.close();
            in.close();
            client.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return history;
    }

    //Searching function interaction with server
    public void GO (ActionEvent event)
    {
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {
            client = new Socket(serverName, port);

            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("6" + "\n" + currentUser.getUsername() +"\n" + STime_slot.getText());

            //Getting input
            DataInputStream in = new DataInputStream(client.getInputStream());
            String re = in.readUTF();
            BufferedReader reader = new BufferedReader(new StringReader(re));

            reader.readLine();
            String temp = reader.readLine();

            listview.getItems().clear();

            //Server returns 999 if user sent an input which isnt an integer
            //i could have stopped the user from sending a non-integer from the client side
            //but i didn't realize this until after i already implemented it on the server side

            if(Integer.parseInt(temp) == 999)
            {
                Label l = new Label("You can't input anything\nother than numbers");
                l.setOpacity(10);
                listview.setPlaceholder(l);
            }
            else {

                while (temp != null) {

                    if (Integer.parseInt(temp) == 1) {
                        gatess.add("Gate one");
                    } else if (Integer.parseInt(temp) == 2) {
                        gatess.add("Gate two");
                    } else if (Integer.parseInt(temp) == 3) {
                        gatess.add("Gate three");
                    } else if (Integer.parseInt(temp) == 4) {
                        gatess.add("Gate four");
                    } else {
                        gatess.add("Error, dont mind this");
                    }
                    temp = reader.readLine();
                }
                listview.setItems(gatess);
            }
            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

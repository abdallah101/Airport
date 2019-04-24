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



public class Ports_Controller implements Initializable {

    public String GATE;
    public String TIMESLOT;
    public Text reserve_success;
    public TextField STime_slot;
    public ListView listview;
    private User currentUser ;

    public void loadUserData(User CU)
    {
        this.currentUser = CU;
        refresh();
    }

    @FXML
    public ComboBox <String> combobox;
    public ComboBox <String> timebox;
    public ComboBox <String> removeRes;
    ObservableList<String> list1 = FXCollections.observableArrayList();
    ObservableList<String> list2 = FXCollections.observableArrayList();
    ObservableList<String> list3 = FXCollections.observableArrayList();
    ObservableList<String> list4 = FXCollections.observableArrayList();
    ObservableList<String> list5 = FXCollections.observableArrayList();
    ObservableList<String> list6 = FXCollections.observableArrayList();
    ObservableList<String> list7 = FXCollections.observableArrayList();
    ObservableList<String> list8 = FXCollections.observableArrayList();
    ObservableList<String> list9 = FXCollections.observableArrayList();
    ObservableList<String> list10 = FXCollections.observableArrayList();
    ObservableList<String> list = FXCollections.observableArrayList("Gate one", "Gate two", "Gate three", "Gate four", "Gate five", "Gate six", "Gate seven", "Gate eight"
    ,"Gate nine", "Gate ten");
    ObservableList<String> gatess = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        combobox.setItems(list);
        Label l = new Label("Input a number between\n0 and 24, Gates\ncorresponding " +
                "to a time\ninterval with that number\nin it will be displayed");
        l.setOpacity(10);
        listview.setPlaceholder(l);
    }

    public void refresh ()
    {

        //System.out.println("");
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {

            Socket client = new Socket(serverName, port);

            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("3" + "\n" + currentUser.getUsername());


            //Getting input
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println(in.available());
            String re = in.readUTF();

            BufferedReader reader = new BufferedReader(new StringReader(re));
            String next = reader.readLine();

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
                if(Integer.parseInt(next) == 555)
                {break;}
                list4.addAll(convertit(Integer.parseInt(next)));
            }list5.clear();
            while (true)
            {
                next = reader.readLine();
                if(Integer.parseInt(next) == 666)
                {break;}
                list5.addAll((convertit((Integer.parseInt(next)))));
            }list6.clear();
            while (true)
            {
                next = reader.readLine();
                if(Integer.parseInt(next) == 777)
                {break;}
                list6.addAll((convertit((Integer.parseInt(next)))));
            }list7.clear();
            while (true)
            {
                next = reader.readLine();
                if(Integer.parseInt(next) == 888)
                {break;}
                list7.addAll((convertit((Integer.parseInt(next)))));
            }list8.clear();
            while (true)
            {
                next = reader.readLine();
                if(Integer.parseInt(next) == 999)
                {break;}
                list8.addAll((convertit((Integer.parseInt(next)))));
            }list9.clear();
            while (true)
            {
                next = reader.readLine();
                if(Integer.parseInt(next) == 1000)
                {break;}
                list9.addAll((convertit((Integer.parseInt(next)))));
            }list10.clear();
            while (true)
            {
                next = reader.readLine();
                if(next == null)
                {break;}
                list10.addAll((convertit((Integer.parseInt(next)))));
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
        if(combobox.getValue() == "Gate one") {
            GATE = "1";
            timebox.setItems(list1);

        }
        else if (combobox.getValue() == "Gate two")
        {
            GATE = "2";
            timebox.setItems(list2);
        }
        else if (combobox.getValue() == "Gate three")
        {
            GATE = "3";
            timebox.setItems(list3);
        }
        else if (combobox.getValue() == "Gate four")
        {
            GATE = "4";
            timebox.setItems(list4);
        }
        else if (combobox.getValue() == "Gate five")
        {
            GATE = "5";
            timebox.setItems(list5);
        }
        else if (combobox.getValue() == "Gate six")
        {
            GATE = "6";
            timebox.setItems(list6);
        }
        else if (combobox.getValue() == "Gate seven")
        {
            GATE = "7";
            timebox.setItems(list7);
        }
        else if (combobox.getValue() == "Gate eight")
        {
            GATE = "8";
            timebox.setItems(list8);
        }
        else if (combobox.getValue() == "Gate nine")
        {
            GATE = "9";
            timebox.setItems(list9);
        }
        else if (combobox.getValue() == "Gate ten")
        {
            GATE = "10";
            timebox.setItems(list10);
        }
        else
        {
            GATE = null;
        }
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
        {
            TIMESLOT = null;
        }
    }

    public void CtimeSlots (ActionEvent SeeTimeSlots) throws Exception
    {
        if(GATE == null || TIMESLOT == null)
        {
            disError("Gate or TimeSlot hasn't been chosen");

        }
        else
        {
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {

            Socket client = new Socket(serverName, port);

            //Sending output
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("4" + "\n" + currentUser.getUsername() + "\n" + GATE + "\n" + TIMESLOT);

            //Getting input
            DataInputStream in = new DataInputStream(client.getInputStream());
            String reserved = in.readUTF();

            if(Integer.parseInt(reserved) == 2)
            {
                disError("You have exceeded your reservation limit");
            }
            else if(Integer.parseInt((reserved))== 3)
            {
                disError("Error, Try again!");
            }
            else if (Integer.parseInt(reserved) == 4)
            {
                disError("Already reserved, try again later");
            }
            else
            {
                disError("Successfully Reserved!");
            }


            out.close();
            in.close();
            client.close();
            System.out.println("Reservation successful");
            refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    public void History (ActionEvent event) throws IOException, Exception
    {
        Stage stage = new Stage();
        Ports_Controller po = new Ports_Controller();
        WebView browser = new WebView();
        WebEngine we = browser.getEngine();
        ScrollPane sc = new ScrollPane();
        sc.setFitToHeight(true);
        sc.setFitToWidth(true);
        sc.setContent(browser);
//        sc.fitToHeightProperty(true);
//        sc.fitToWidthProperty();

        we.loadContent(getLogFiles());
        VBox vb = new VBox(10);
        vb.getChildren().add(sc);

        Scene scene = new Scene(vb,500,500);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public void GoBack (ActionEvent backwards) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("javafx.fxml"));
        Stage window = (Stage)((Node)backwards.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 600, 500));
        window.setResizable(false);
        window.show();
    }

    public void disError (String error)
    {
        reserve_success.setText(error);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
        visiblePause.setOnFinished(e -> { reserve_success.setVisible(false); });
        visiblePause.play();
        reserve_success.setVisible(true);
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

    public String getLogFiles ()
    {
        String history = null;
        String temp= null;
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {
            Socket client = new Socket(serverName, port);

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

    public void GO (ActionEvent event)
    {
        String serverName = "localhost";
        int port = Integer.parseInt("6066");
        try {
            Socket client = new Socket(serverName, port);

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
                    } else if (Integer.parseInt(temp) == 5) {
                        gatess.add("Gate five");
                    }else if (Integer.parseInt(temp) == 6) {
                        gatess.add("Gate six");
                    }else if (Integer.parseInt(temp) == 7) {
                        gatess.add("Gate seven");
                    }else if (Integer.parseInt(temp) == 8) {
                        gatess.add("Gate eight");
                    }else if (Integer.parseInt(temp) == 9) {
                        gatess.add("Gate nine");
                    }else if (Integer.parseInt(temp) == 10) {
                        gatess.add("Gate ten");
                    }

                    else {
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

    public void Transfer (ActionEvent event)
    {
        if(listview.getSelectionModel().getSelectedItem() == null)
        {
            disError("Select a gate first");
        }
        else {
            combobox.setValue(listview.getSelectionModel().getSelectedItem().toString());
            if(combobox.getValue() == "Gate one") {
                GATE = "1";

            }
            else if (combobox.getValue() == "Gate two")
            {
                GATE = "2";
            }
            else if (combobox.getValue() == "Gate three")
            {
                GATE = "3";
            }
            else if (combobox.getValue() == "Gate four")
            {
                GATE = "4";
            }
            else if (combobox.getValue() == "Gate five")
            {
                GATE = "5";
            }
            else if (combobox.getValue() == "Gate six")
            {
                GATE = "6";
            }
            else if (combobox.getValue() == "Gate seven")
            {
                GATE = "7";
            }
            else if (combobox.getValue() == "Gate eight")
            {
                GATE = "8";
            }
            else if (combobox.getValue() == "Gate nine")
            {
                GATE = "9";
            }
            else if (combobox.getValue() == "Gate ten")
            {
                GATE = "10";
            }
            else
            {
                GATE = null;
            }
        }

        if(STime_slot.getText().matches("[0-9]+"))
        {
            if(Integer.parseInt(STime_slot.getText()) < 24 && Integer.parseInt(STime_slot.getText()) > 0)
            {
                timebox.setValue(convertit(Integer.parseInt(converttime(STime_slot.getText()))));
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
            else
            {
                disError("Timeslot doesn't exist");
            }
        }
        else
        {
            disError("Your timeslot is invalid");
        }





    }


//    public void GetRes ()
//    {
//
//    }


    public String converttime (String numb)
    {
        int num = Integer.parseInt(numb);
        if(num < 4 && num >= 0)
        {
            return "4";
        }
        else if (num < 8)
        {
            return "8";
        }
        else if (num < 12)
        {
            return "12";
        }
        else if ( num < 16)
        {
            return "16";
        }
        else if (num < 20)
        {
            return  "20";
        }
        else if (num < 24)
        {
            return "24";
        }
        else
        {
            return "0";
        }
    }


}

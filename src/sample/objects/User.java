package sample.objects;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

public class User {


    public String username = null;

    public User (String username)
    {
        this.username = username;
    }

    public void setUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }



}
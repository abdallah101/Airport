package sample.objects;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

public class User {


    private String username = null;

    public void setUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }



}
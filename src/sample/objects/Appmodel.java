package sample.objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appmodel {

    private String username = null;

    public final String getText() {
        return username;
    }

    public final void setText(String text) {
        username = text;
    }
}
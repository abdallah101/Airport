package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimerTask;

public class Reset extends TimerTask {
    private String dbURL = "jdbc:mysql://localhost:3306/airportdb";
    private String dbname = "airportdb";
    private String usernamedb = "root";
    private String passworddb = "";

    public void run()
    {
        //Creating Connection
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbURL, usernamedb, passworddb);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        //UPDATE USER SET reserve_limit = '"+how_many+"' WHERE Username = '"+userReservingString+"'"
        String Time1 = "UPDATE gates SET Gate_one = 1, Gate_two = 1, Gate_three = 1, Gate_four = 1, Gate_five = 1, Gate_six = 1, Gate_seven = 1" +
                ", Gate_eight = 1, Gate_nine = 1, Gate_ten = 1  WHERE Time_slot = 4";
        String Time2 = "UPDATE gates SET Gate_one = 1, Gate_two = 1, Gate_three = 1, Gate_four = 1, Gate_five = 1, Gate_six = 1, Gate_seven = 1" +
                ", Gate_eight = 1, Gate_nine = 1, Gate_ten = 1  WHERE Time_slot = 8";
        String Time3 = "UPDATE gates SET Gate_one = 1, Gate_two = 1, Gate_three = 1, Gate_four = 1, Gate_five = 1, Gate_six = 1, Gate_seven = 1" +
                ", Gate_eight = 1, Gate_nine = 1, Gate_ten = 1  WHERE Time_slot = 12";
        String Time4 = "UPDATE gates SET Gate_one = 1, Gate_two = 1, Gate_three = 1, Gate_four = 1, Gate_five = 1, Gate_six = 1, Gate_seven = 1" +
                ", Gate_eight = 1, Gate_nine = 1, Gate_ten = 1  WHERE Time_slot = 16";
        String Time5 = "UPDATE gates SET Gate_one = 1, Gate_two = 1, Gate_three = 1, Gate_four = 1, Gate_five = 1, Gate_six = 1, Gate_seven = 1" +
                ", Gate_eight = 1, Gate_nine = 1, Gate_ten = 1  WHERE Time_slot = 20";
        String Time6 = "UPDATE gates SET Gate_one = 1, Gate_two = 1, Gate_three = 1, Gate_four = 1, Gate_five = 1, Gate_six = 1, Gate_seven = 1" +
                ", Gate_eight = 1, Gate_nine = 1, Gate_ten = 1  WHERE Time_slot = 24";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(Time1);
            stmt.executeUpdate(Time2);
            stmt.executeUpdate(Time3);
            stmt.executeUpdate(Time4);
            stmt.executeUpdate(Time5);
            stmt.executeUpdate(Time6);

            String users = "UPDATE USER SET reserve_limit = 0";
            stmt.executeUpdate(users);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        System.out.println("Server Reseted!");


    }
}

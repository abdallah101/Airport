package Connectivity;

import sample.Reset;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;


public class GreetingServer extends Thread
{
    private int port = 6066;
    private ServerSocket serverSocket;
    private String dbURL = "jdbc:mysql://localhost:3306/airportdb";
    long delay = 8640000; // reset server at delay 24hours (delay is in ms here)
    Reset task = new Reset();
    Timer timer = new Timer("TaskName");

    //Class constructor to get proper sql driver
    public GreetingServer() throws ClassNotFoundException
    {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("JDBC Driver failed to load...");
                e.printStackTrace();
            }
    }

    //listener function for clients trying to create a socket connection
    public void acceptConnections()
    {
        timer.cancel();
        timer = new Timer("TaskName");
        Date executionDate = new Date(); // no params = now
        timer.scheduleAtFixedRate(task, delay, delay);
        try {
            serverSocket = new ServerSocket(port);
            //serverSocket.setSoTimeout(1000000);
        }
        catch (IOException e) {
            System.err.println("ServerSocket instantiation failure");
            e.printStackTrace();
            System.exit(0);
        }
        while(true) {
            try {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                ServerThread st = new ServerThread(server);
                new Thread(st).start();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    //Main starting point of server where it is created and initiated on a port to start listening on it
    public static void main(String [] args)
    {
        GreetingServer t = null;
        try {
            t = new GreetingServer();
        } catch (ClassNotFoundException e1)
        {
            System.out.println("unable to load JDBC driver");
            e1.printStackTrace();
            System.exit(1);
        }
        t.acceptConnections();
    }

    //Thread functions
    class ServerThread implements Runnable
    {
        private Socket socket;
        private DataInputStream datain;
        private DataOutputStream dataout;

        //Inside the constructor: store the passed object in the data member
        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        //recieve input from client and parse the command then perform appropriate function and finally send back output
        public void run() {

            try {
                //Input and output streams, obtained from the member socket object
                datain = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                dataout = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            } catch (IOException e) {
                System.out.println("Failed to get i/o streams");
                return;
            }

            boolean conversationActive = true;
            boolean login = false;
            String output = null;
            while (conversationActive) {


                try {
                    BufferedReader reader = new BufferedReader(new StringReader(datain.readUTF()));
                    int command = Integer.parseInt(reader.readLine());
                    System.out.println("Command = " + command);

                    //cmd1 indicates if user is in database or not
                    if (command == 1) {
                        String username = reader.readLine();
                        String password = reader.readLine();
                        login = check(username, password);
                        dataout.writeBoolean(login);
                        dataout.flush();
                    }
                    //cmd2 registers users in the database
                    else if (command == 2) {
                        String username = reader.readLine();
                        String password = reader.readLine();
                        String fullName = reader.readLine();
                        login = register(username, password, fullName);
                        dataout.writeBoolean(login);
                        dataout.flush();
                    }
                    //cmd3 returns timeslots available for all gates
                    else if (command == 3) {
                        String table = null;
                        table = AvailableSlot(reader.readLine());
                        dataout.writeUTF(table);
                    }
                    //cmd4 reserves a timeslot for a gate
                    else if (command == 4) {
                        output = reserve(reader.readLine(), reader.readLine(), reader.readLine());
                        dataout.writeUTF(output);
                        dataout.flush();
                    }
                    //cmd5 returns the history of a given user
                    else if (command == 5) {
                        output = getLogFile(reader.readLine());
                        dataout.writeUTF(output);
                        dataout.flush();
                    }
                    //cmd6 checks which gates have the searched timeslot inputed by the user
                    else if (command == 6) {
                        output = checkGates(reader.readLine(), reader.readLine());
                        dataout.writeUTF(output);
                    } else {
                        conversationActive = false;
                    }

                } catch (IOException e) {
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                conversationActive = false;
            }
            try {
                dataout.close();
                datain.close();
                socket.close();
                System.out.println("Connection close");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Function checks USER table in database whether username and password combination is present
        //returns true if user is present in database and false otherwise
        private boolean check(String username, String password) throws SQLException {
            String dbname = "airportdb";
            String usernamedb = "root";
            String passworddb = "";
            //Creating Connection
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(dbURL, usernamedb, passworddb);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Creating statement with username, password, and full name of client
            String SQL = "SELECT Username, Password FROM USER WHERE Username = '" + username + "' && Password = '" + password + "' ";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            //if combination not found or at least one of the boxes is empty then fail
            if (!rs.next()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String user1 = "INSERT INTO loghistory(username,date,Interaction, success) VALUES('" + username + "','" + dateFormat.format(date) + "','" + "Login" + "','0')";
                Statement stat = conn.createStatement();
                stat.executeUpdate(user1);
                conn.close();
                System.out.println("False");
                return false;
            }
            //If succeeded then go to AirPort main Page
            else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String user1 = "INSERT INTO loghistory(username,date,Interaction, success) VALUES('" + username + "','" + dateFormat.format(date) + "','" + "Login" + "','1')";
                Statement stat = conn.createStatement();
                stat.executeUpdate(user1);
                conn.close();
                System.out.println("True");
                return true;
            }
        }

        //Function return string containing available time slot in a format i defined and interpreted
        //by the client side correctly
        //the format is defined by starting with a 3 digit same number representing a gate then available time slots
        //are written on a new line
        //ex. 111 \n 4 \n 8 \n 222 20 \n 24 \n
        private String AvailableSlot(String userName) throws Exception, SQLException {
            String dbname = "airportdb";
            String usernamedb = "root";
            String passworddb = "";
            //Creating Connection
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(dbURL, usernamedb, passworddb);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String out = null;
            try {
                //Creating statement with username, password, and full name of client
                String SQL = "SELECT * FROM GATES";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);

//            while(rs.next())
//            {
//                out = rs.getString("Time_slot") + "\n";
//            }rs.beforeFirst();

                out = "111" + "\n";
                while (rs.next()) {
                    if (Integer.parseInt(rs.getString("Gate_one")) == 1) {
                        out = out + rs.getString("Time_slot") + "\n";
                    }
                }
                rs.beforeFirst();
                out = out + "222" + "\n";
                while (rs.next()) {
                    if (Integer.parseInt(rs.getString("Gate_two")) == 1) {
                        out = out + rs.getString("Time_slot") + "\n";
                    }
                }
                rs.beforeFirst();
                out = out + "333" + "\n";
                while (rs.next()) {
                    if (Integer.parseInt(rs.getString("Gate_three")) == 1) {
                        out = out + rs.getString("Time_slot") + "\n";
                    }
                }
                rs.beforeFirst();
                out = out + "444" + "\n";
                while (rs.next()) {
                    if (Integer.parseInt(rs.getString("Gate_four")) == 1) {
                        out = out + rs.getString("Time_slot") + "\n";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return out;
        }

        //Simple function to insert a new user defined by a username, password and full name and returns false if
        //username is already being used
        private boolean register(String username, String password, String fullName) throws SQLException {
            String dbname = "airportdb";
            String usernamedb = "root";
            String passworddb = "";
            //Creating Connection
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(dbURL, usernamedb, passworddb);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            //prepare statement for executing update
            String insert = "INSERT INTO USER (Username,Password,Name,reserve_limit) VALUES('" + username + "','" + password + "','" + fullName + "','" + 1 + "')";
            Statement statement = connection.createStatement();

            //Getting a boolean to whether the username is already being used
            String SQL = "SELECT Username, Password FROM USER WHERE Username = '" + username + "'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {
                System.out.println("Username Already Being Used");
                return false;
            } else {
                System.out.println("database updated with new user");
                statement.executeUpdate(insert);
                return true;
            }

        }

        //function to reserve a time slot which increases reserve limit for reserving client
        //also has conditions for errors that might occur and appropriate output that the client
        //will understand and display appropriate error messages
        public String reserve(String userReservingString, String GATE, String TIMESLOT) throws SQLException {
            int GATEINT = Integer.parseInt(GATE);
            int TIMESLOTINT = Integer.parseInt(TIMESLOT);
            String dbname = "airportdb";
            String usernamedb = "root";
            String passworddb = "";
            //Creating Connection
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(dbURL, usernamedb, passworddb);
            } catch (SQLException e) {
                e.printStackTrace();
                return "3";
            }

            String update = "UPDATE gates SET ";
            String Gate = null;

            if (GATEINT == 1) {
                Gate = "Gate_one";
            } else if (GATEINT == 2) {
                Gate = "Gate_two";
            } else if (GATEINT == 3) {
                Gate = "Gate_three";
            } else if (GATEINT == 4) {
                Gate = "Gate_four";
            }

            try {

                String search = "SELECT " + Gate + " FROM gates WHERE Time_slot = '" + TIMESLOTINT * 4 + "'";
                Statement searchstmt = connection.createStatement();
                ResultSet searchRS = searchstmt.executeQuery(search);
                searchRS.next();
                if (Integer.parseInt(searchRS.getString(Gate)) == 0) {

                    //LOGGING RESERVE FAIL
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String logit = "INSERT INTO loghistory(username,date,Interaction, success) VALUES('" + userReservingString + "','" + dateFormat.format(date) + "','" + "Error: already reserved" + "','0')";
                    Statement logitstmt = connection.createStatement();
                    logitstmt.executeUpdate(logit);
                    return "4";

                } else {

                    //CREATING STRINGS FOR GATE TIME SLOT UPDATE AND SEARCHING FOR RESERVE LIMIT OF USER
                    update = update + Gate + " = 0 WHERE Time_slot = '" + TIMESLOTINT * 4 + "'";
                    String ReserveLimit = "SELECT Username, reserve_limit FROM USER WHERE Username = '" + userReservingString + "'";
                    Statement res = connection.createStatement();
                    ResultSet rs1 = res.executeQuery(ReserveLimit);
                    rs1.next();

                    //CONVERTING STRING TO INTEGER
                    int how_many = Integer.parseInt(rs1.getString("reserve_limit"));
                    //ADDING TO TEMPORARY RESERVE_LIMIT TO CHECK IF IT IS OVER 3
                    how_many++;

                    if (how_many > 3) {

                        //LOGGING RESERVE FAIL
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String logit = "INSERT INTO loghistory(username,date,Interaction, success) VALUES('" + userReservingString + "','" + dateFormat.format(date) + "','" + "Failed to reserve" + "','0')";
                        Statement logitstmt = connection.createStatement();
                        logitstmt.executeUpdate(logit);
                        return "2";

                    } else {

                        //RESERVING
                        Statement stmt = connection.createStatement();
                        stmt.executeUpdate(update);

                        //INCREASING RESERVE LIMIT
                        String ReserveNext = "UPDATE USER SET reserve_limit = '" + how_many + "' WHERE Username = '" + userReservingString + "'";
                        Statement stt = connection.createStatement();
                        stt.executeUpdate(ReserveNext);

                        ///LOGGING RESERVE
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String logit = "INSERT INTO loghistory(username,date,Interaction, success) " +
                                "VALUES('" + userReservingString + "','" + dateFormat.format(date) + "','" + "Reserving " + Gate + " at time " + convertit(TIMESLOTINT * 4) + "','1')";
                        Statement logitstmt = connection.createStatement();
                        logitstmt.executeUpdate(logit);
                        return "1";
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
                    return "3";
                }



        }

        //Getting history of user with server from the database
        public String getLogFile(String username) throws SQLException {
            String out = null;
            String dbname = "airportdb";
            String usernamedb = "root";
            String passworddb = "";
            //Creating Connection
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(dbURL, usernamedb, passworddb);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //"SELECT Username, reserve_limit FROM USER WHERE Username = '"+userReservingString+"'";
            out = "SELECT date, Interaction FROM loghistory WHERE Username = '" + username + "'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(out);
            rs.next();
            out = rs.getString("date") + ":   " + rs.getString("Interaction") + "\n";
            while (true) {
                rs.next();
                out = out + rs.getString("date") + ":   " + rs.getString("Interaction") + "\n";
                if (rs.isLast() == true) {
                    break;
                }
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String logit = "INSERT INTO loghistory(username,date,Interaction, success) VALUES('" + username + "','"
                    + dateFormat.format(date) + "','" + "Accessed User History" + "','1')";
            Statement logitstmt = connection.createStatement();
            logitstmt.executeUpdate(logit);

            return out;
        }

        //Check which gates have particular time slot user is asking for
            public String checkGates (String username, String timeslot) throws SQLException {
                String out = null;

                try {
                    int num = Integer.parseInt(timeslot);
                } catch (NumberFormatException e) {
                    out = null + "\n" + "999";
                    return out;
                }

                String TIMESLOT = converttime(timeslot);
                int TS = Integer.parseInt(TIMESLOT);
                String usernamedb = "root";
                String passworddb = "";
                //Creating Connection
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(dbURL, usernamedb, passworddb);
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }

                if(TS == 0)
                {
                    return "0";
                }
                else
                {
                    out = "SELECT Time_slot, Gate_one, Gate_two, Gate_three, Gate_four FROM gates WHERE Time_slot = '"+TS+"'";
                    Statement stat = connection.createStatement();
                    ResultSet rs = stat.executeQuery(out);
                    rs.next();
                    out = null + "\n";

                    if (Integer.parseInt(rs.getString("Gate_one")) == 1)
                    {
                        out = out + "1" +"\n";
                    }
                    if (Integer.parseInt(rs.getString("Gate_two")) == 1)
                    {
                        out = out + "2" +"\n";
                    }
                    if (Integer.parseInt(rs.getString("Gate_three")) == 1)
                    {
                        out = out + "3" +"\n";
                    }
                    if (Integer.parseInt(rs.getString("Gate_four")) == 1)
                    {
                        out = out + "4" +"\n";
                    }


                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String logit = "INSERT INTO loghistory(username,date,Interaction, success) VALUES('" + username + "','"
                            + dateFormat.format(date) + "','" + "Searched for Gates at time " + convertit(Integer.parseInt(converttime(timeslot))) + "','1')";
                    Statement logitstmt = connection.createStatement();
                    logitstmt.executeUpdate(logit);
                    return out;
                }




            }
        }

        //If user inputs a time inside a certain interval, the function returns the number indicating that interval in the databse
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

        //converting database number indicating interval into String to use in logging history of user interactions with server
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




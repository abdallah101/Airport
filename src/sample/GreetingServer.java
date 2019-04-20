package sample;

//import java.net.*;
//import java.io.*;
//
//class GreetingServer
//{
//    public static void main (String[] args) throws IOException
//    {
//
//        ServerSocket server = new ServerSocket(3000);
//        Socket s = server.accept();
//
//        System.out.println("Connected");
//
//    }
//}

// File Name GreetingServer.java



import java.io.*;
import java.net.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import Connectivity.ConnectionClass;
import java.util.Date;

public class GreetingServer extends Thread {


    private int port = 6066;
    private ServerSocket serverSocket;

    private String dbURL = "jdbc:mysql://localhost:3306/airportdb";


    public GreetingServer() throws ClassNotFoundException {


            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("JDBC Driver failed to load...");
                e.printStackTrace();
            }


    }



    public void acceptConnections() {


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
        //System.out.println("here");
        t.acceptConnections();
    }

    class ServerThread implements Runnable
    {
        private Socket socket;
        private DataInputStream datain;
        private DataOutputStream dataout;

        public ServerThread(Socket socket)
        {
            //Inside the constructor: store the passed object in the data member
            this.socket = socket;
        }

        public void run() {

            try
            {
                 //Input and output streams, obtained from the member socket object
                  datain = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                  dataout = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            }
                catch (IOException e)
                {
                    System.out.println("Failed to get i/o streams");
                    return;
                }

                boolean conversationActive = true;
                boolean login = false;
                while(conversationActive) {


                    try {
                        BufferedReader reader = new BufferedReader(new StringReader(datain.readUTF()));
                        int command = Integer.parseInt(reader.readLine());
                        System.out.println("Command = " + command);
                        if(command == 1)
                        {
                            String username = reader.readLine();
                            String password = reader.readLine();
                            login = check(username, password);
                            dataout.writeBoolean(login);
                            dataout.flush();
                        }
                        else if (command == 2)
                        {
                            String username = reader.readLine();
                            String password = reader.readLine();
                            String fullName = reader.readLine();
                            login = register(username, password, fullName);
                            dataout.writeBoolean(login);
                            dataout.flush();
                        }
                        else if (command == 3)
                        {
                            String table = null;
                            table = AvailableSlot();
                            dataout.writeUTF(table);
                        }
                        else if (command == 4)
                        {
                            reserve(reader.readLine(),reader.readLine());
                        }
                        else if (command == 5)
                        {
//                            dataout.writeUTF(getUsername());
//                            dataout.flush();
                        }
                        else
                        {
                            conversationActive = false;
                        }

                    }

                    catch (IOException e) {}
                    catch (SQLException e)
                    {
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

        private boolean check (String username, String password) throws SQLException
        {
            String dbname = "airportdb";
            String usernamedb = "root";
            String passworddb = "";
            //Creating Connection
           Connection conn = null;
           try {
               conn = DriverManager.getConnection(dbURL, usernamedb, passworddb);
           }catch (SQLException e)
           {
               e.printStackTrace();
           }

            //Creating statement with username, password, and full name of client
            String SQL = "SELECT Username, Password FROM USER WHERE Username = '"+username+"' && Password = '"+password+"' ";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            //if combination not found or at least one of the boxes is empty then fail
            if(!rs.next())
            {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String user1 = "INSERT INTO loghistory(username,date,LoggedIn) VALUES('"+username+"','"+ dateFormat.format(date)+"','0')";
                Statement stat = conn.createStatement();
                stat.executeUpdate(user1);
                conn.close();
                System.out.println("False");
                return false;
            }
            //If succeeded then go to AirPort main Page
            else
            {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String user1 = "INSERT INTO loghistory(username,date,LoggedIn) VALUES('"+username+"','"+ dateFormat.format(date)+"','1')";
                Statement stat = conn.createStatement();
                stat.executeUpdate(user1);
                conn.close();
                System.out.println("True");
                return true;
            }
        }


        private String AvailableSlot () throws Exception, SQLException
        {
            String dbname = "airportdb";
            String usernamedb = "root";
            String passworddb = "";
            //Creating Connection
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(dbURL, usernamedb, passworddb);
            }catch (SQLException e)
            {
                e.printStackTrace();
            }
            //Creating statement with username, password, and full name of client
            String SQL = "SELECT * FROM GATES";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            String out = null;
//            while(rs.next())
//            {
//                out = rs.getString("Time_slot") + "\n";
//            }rs.beforeFirst();

            out = "111" + "\n";
            while(rs.next())
            {
                if(Integer.parseInt(rs.getString("Gate_one")) == 1)
                {
                 out = out + rs.getString("Time_slot") + "\n";
                }
            }rs.beforeFirst();
            out = out + "222" + "\n";
            while (rs.next())
            {
                if(Integer.parseInt(rs.getString("Gate_two")) == 1)
                {
                    out = out + rs.getString("Time_slot") + "\n";
                }
            }rs.beforeFirst();
            out = out + "333" + "\n";
            while (rs.next())
            {
                if(Integer.parseInt(rs.getString("Gate_three")) == 1)
                {
                    out = out + rs.getString("Time_slot") + "\n";
                }
            }rs.beforeFirst();
            out = out + "444" + "\n";
            while (rs.next())
            {
                if(Integer.parseInt(rs.getString("Gate_four")) == 1)
                {
                    out = out + rs.getString("Time_slot") + "\n";
                }
            }
            return out;
        }


        private boolean register(String username, String password, String fullName) throws SQLException
        {
            String dbname = "airportdb";
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


            //prepare statement for executing update
            String insert="INSERT INTO USER (Username,Password,Name) VALUES('"+username+"','"+password+"','"+fullName+"')";
            Statement statement = connection.createStatement();

            //Getting a boolean to whether the username is already being used
            String SQL = "SELECT Username, Password FROM USER WHERE Username = '"+username+"'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next())
            {
                System.out.println("Username Already Being Used");
                return false;
            }
            else
            {
                System.out.println("database updated with new user");
                statement.executeUpdate(insert);
                return true;
            }

        }

        public void reserve (String GATE, String TIMESLOT) throws SQLException
        {
            int GATEINT = Integer.parseInt(GATE);
            int TIMESLOTINT = Integer.parseInt(TIMESLOT);
            String dbname = "airportdb";
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
            String update="UPDATE gates SET ";

            if (GATEINT == 1)
            {
                update = update + "Gate_one = 0 WHERE Time_slot = '"+TIMESLOTINT*4+"'";
                System.out.println(update);
            }
            else if (GATEINT == 2)
            {
                update = update + "Gate_two = 0 WHERE Time_slot = '"+TIMESLOTINT*4+"'";
            }
            else if (GATEINT == 3)
            {
                update = update + "Gate_three = 0 WHERE Time_slot = '"+TIMESLOTINT*4+"'";
            }
            else if (GATEINT == 4)
            {
                update = update + "Gate_four = 0 WHERE Time_slot = '"+TIMESLOTINT*4+"'";
            }

//            String SQL = "SELECT reserve_limit FROM USER WHERE Username = '"+username+"'";
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(SQL);
//
//            String insert="INSERT INTO user (reserve_limit) VALUES()";
//            Statement statement = connection.createStatement();

            //prepare statement for executing update
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(update);
        }

//        public String getUsername () throws SQLException
//        {
//            String user = null;
//            String dbname = "airportdb";
//            String usernamedb = "root";
//            String passworddb = "";
//            //Creating Connection
//            Connection conn = null;
//            try {
//                conn = DriverManager.getConnection(dbURL, usernamedb, passworddb);
//            }catch (SQLException e)
//            {
//                e.printStackTrace();
//            }
//            //Creating statement with username, password, and full name of client
//            String SQL = "SELECT * FROM loghistory";
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(SQL);
//
//
//
//            return user;
//        }

        }
    }




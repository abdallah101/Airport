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
//import Connectivity.ConnectionClass;

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
                        System.out.println(command);
                        if(command == 1)
                        {
                            String username = reader.readLine();
                            String password = reader.readLine();
                            System.out.println(username);
                            System.out.println(password);
                            login = check(username, password);
                            dataout.writeBoolean(login);
                            dataout.flush();
                        }
                        else if (command == 2)
                        {
                            String username = reader.readLine();
                            String password = reader.readLine();
                            String fullName = reader.readLine();
                            System.out.println(username);
                            System.out.println(password);
                            System.out.println(fullName);
                            login = register(username, password, fullName);
                            dataout.writeBoolean(login);
                            dataout.flush();
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
                conn.close();
                System.out.println("False");
                return false;
            }
            //If succeeded then go to AirPort main Page
            else
            {
                conn.close();
                System.out.println("True");
                return true;
            }




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
        }
    }




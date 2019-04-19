//package sample2;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.net.ServerSocket;
//
//import java.sql.*;
//
//
//////////////////////////////////////////////////////////
//public class SkillsServer {
//
//    private int port = 1927;
//
//    private ServerSocket serverSocket;
//
//    //URL of DB server and authentication string
//    static final String dbURL =
//            "jdbc:mysql://localhost/j2mebook?" +
//                    "user=root&password=123";
//
//    //In the constructor, load the Java driver for the MySQL DB server
//    //to enable this program to communicate with the database
//    public SkillsServer() throws ClassNotFoundException {
//        Class.forName("com.mysql.jdbc.Driver");
//    }
//
//    ////////////////////////////////////////////////////////
//    public void acceptConnections() {
//
//        try {
//            //similar to the WelcomeSocket in the PowerPoint slides
//            serverSocket = new ServerSocket(port);
//        }
//        catch (IOException e) {
//            System.err.println("ServerSocket instantiation failure");
//            e.printStackTrace();
//            System.exit(0);
//        }
//
////Entering the infinite loop
//        while (true) {
//            try {
//
//                Socket newConnection = serverSocket.accept();
//                System.out.println("accepted connection");
//                ServerThread st = new ServerThread(newConnection);
//                new Thread(st).start();
//            }
//            catch (IOException ioe) {
//                System.err.println("server accept failed");
//            }
//        }
//    }
//
//    ///////////////////////////////////////////////////
//    public static void main(String args[]) {
//
//        SkillsServer server = null;
//        try {
//            //Instantiate an object of this class. This will load the JDBC database driver
//            server = new SkillsServer();
//        }
//        catch (ClassNotFoundException e) {
//            System.out.println("unable to load JDBC driver");
//            e.printStackTrace();
//            System.exit(1);
//        }
//
////call this function, which will start it all...
//        server.acceptConnections();
//    }
//
//
//    /////////////////////////////////////////////////////
//    //Internal class
//    class ServerThread implements Runnable {
//
//        private Socket socket;
//        private DataInputStream datain;
//        private DataOutputStream dataout;
//
//        ////////////////////////////////////
//        public ServerThread(Socket socket) {
//            //Inside the constructor: store the passed object in the data member
//            this.socket = socket;
//        }
//
//        ////////////////////////////////////
//        //This is where you place the code you want to run in a thread
//        //Every instance of a ServerThread will handle one client (TCP connection)
//        public void run() {
//            try {
//                //Input and output streams, obtained from the member socket object
//                datain = new DataInputStream(new BufferedInputStream
//                        (socket.getInputStream()));
//                dataout = new DataOutputStream(new BufferedOutputStream
//                        (socket.getOutputStream()));
//            }
//            catch (IOException e) {
//                return;
//            }
//            byte[] ba = new byte[6];
//            boolean conversationActive = true;
//
//            while(conversationActive) {
//                String skill = null;
//                try {
//                    //read from the input stream buffer (read a message from client)
//                    datain.read(ba,0,6);
//                    skill = new String(ba);
//                    //Exit when a "Q" is received from a client
//                    if ((skill.length() == 1) &&
//                            (skill.toUpperCase().charAt(0) == 'Q')) {
//                        conversationActive = false;
//                    }
//                    else {
//                        System.out.println("requested skill = " + skill);
//                        String names = getNames(skill);
//                        System.out.println("names: " + names);
//                        System.out.println("writing " + names.length() + " bytes");
//
//                        //Write to the output stream buffer (send a message to client)
//                        dataout.write(names.getBytes(),0,names.length());
//                        dataout.write("\\n".getBytes(),0,1);
//                        dataout.flush();
//                    }
//                }
//                catch (IOException ioe) {
//                    conversationActive = false;
//                }
//            }
//            try {
//                System.out.println("closing socket");
//                datain.close();
//                dataout.close();
//                //When the server receives a "Q", we arrive here
//                socket.close();
//            }
//            catch (IOException e) {
//            }
//        }
//
//        ////////////////////////////////////////////////////////////////////////////
//        //This function is for communicating with the database server
//        //using API provided by the JDBC driver loaded at the top
//        private String getNames(String skill) {
//            String result = "None available";
//            Connection conn = null;
//            try {
//                conn = DriverManager.getConnection(dbURL);
//
//                Statement stmt = conn.createStatement();
//                String query = "SELECT lastname, firstname " +
//                        "FROM skills " + "WHERE skill = " +
//                        "'" + skill.trim() + "'" +
//                        " ORDER BY lastname";
//                System.out.println("query = " + query);
//                ResultSet rs = stmt.executeQuery(query);
//                StringBuffer sb = new StringBuffer();
//                while (rs.next()) {
//                    sb.append(rs.getString(1));
//                    sb.append(", ");
//                    sb.append(rs.getString(2));
//                    sb.append('$');
//                }
//                result = sb.toString();
//            }
//            catch (SQLException e) {
//                System.out.println(e.getMessage());
//                result = "server error";
//            }
//            finally {
//                if (conn != null) {
//                    try {
//                        conn.close();
//                    }
//                    catch (SQLException e) {
//                    }
//                }
//            }
//            return result;
//        }
//    }
//}
//
//
//}

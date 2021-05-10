package JPoker24GameServer;

import Common.*;
import Common.Messages.JMSMessage;
import jakarta.jms.JMSException;

import javax.naming.NamingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

public class JPokerServer extends UnicastRemoteObject implements JPokerInterface {
    //    private static final String userInfoPath = "UserInfo.txt";
    private static final String onlineUserPath = "OnlineUser.txt";
    private final Connection conn;

    private final JMSHelperServer jmsHelper = new JMSHelperServer();
    private final ReadMessage messageReader = new ReadMessage();

    private final JPokerRoomManager roomManager = new JPokerRoomManager();

    private final ArrayList<JPokerUser> players = new ArrayList<>();

    protected JPokerServer() throws RemoteException, SQLException, NamingException, JMSException {

        super();
        String DB_USER = "c3358";
        String DB_PASS = "c3358PASS";
        String DB_HOST = "localhost";
        String DB_NAME = "c3358";
        conn = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + "/" + DB_NAME, DB_USER, DB_PASS);
        System.out.println("Database connection successful.");
    }

    public static void main(String[] args) {
        try {
            JPokerServer server = new JPokerServer();
            System.setSecurityManager(new SecurityManager());
            Naming.rebind("Server.JPokerServer", server);
            setupOnlineUser();
            new Thread(server.messageReader).start();
        } catch (Exception e) {
            System.err.println("Remote Exception thrown: " + e);
            e.printStackTrace();
        }
    }

    private static void setupOnlineUser() {
        synchronized (onlineUserPath) {
            File onlineUserFile = new File(onlineUserPath);
            try {
                if (!onlineUserFile.createNewFile()) {
                    // File exists
                    PrintWriter writer = new PrintWriter(onlineUserFile);
                    writer.print("");
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error(e);
            }
        }
    }

    public ArrayList<String> readOnlineUser() {
        ArrayList<String> onlineUsers = null;
        synchronized (onlineUserPath) {
            try (ObjectInputStream onlineUserReader = new ObjectInputStream(new FileInputStream(onlineUserPath))) {
                {
                    onlineUsers = (ArrayList<String>) onlineUserReader.readObject();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                throw new Error(e);
            }
        }

        return onlineUsers;
    }

    public void writeOnlineUser(String username) throws IOException {
        synchronized (onlineUserPath) {
            ArrayList<String> onlineUsers = readOnlineUser();

            if (onlineUsers == null) {
                onlineUsers = new ArrayList<>();
            }
            onlineUsers.add(username);

            try (ObjectOutputStream onlineUserWriter = new ObjectOutputStream(new FileOutputStream(onlineUserPath))) {
                onlineUserWriter.writeObject(onlineUsers);
            }
        }
    }

    public void writeOnlineUser(ArrayList<String> onlineUsers) throws IOException {
        synchronized (onlineUserPath) {
            try (ObjectOutputStream onlineUserWriter = new ObjectOutputStream(new FileOutputStream(onlineUserPath))) {
                onlineUserWriter.writeObject(onlineUsers);
            }
        }
    }

    private boolean isOnlineUserLoggedIn(ArrayList<String> users, String username) {
        boolean isLoggedIn = false;
        for (String onlineUser : users) {
            if (onlineUser.equals(username)) {
                isLoggedIn = true;
                break;
            }
        }
        return isLoggedIn;
    }

    private boolean validateUser(String loginName, String password) throws SQLException {
        boolean isValidated = false;
        PreparedStatement stmt = conn.prepareStatement("SELECT password FROM c3358.user WHERE username=?");
        stmt.setString(1, loginName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            if (rs.getString(1).equals(password))
                isValidated = true;
        }
        return isValidated;
    }

    private boolean checkRepeatLogin(String loginName) {
        ArrayList<String> onlineUsers = readOnlineUser();
        if (onlineUsers == null) {
            onlineUsers = new ArrayList<>();
        }
        return isOnlineUserLoggedIn(onlineUsers, loginName);
    }

    private JPokerUser getUserFromDatabase(String username) {
        PreparedStatement stmt;
        JPokerUser user = null;
        try {
            String selectStatement = "SELECT username, password, games_played, win" +
                    " FROM user" +
                    " WHERE user.username = ?";
            stmt = conn.prepareStatement(selectStatement);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString(1);
                String password = rs.getString(2);
                Integer gamesPlayed = rs.getInt(3);
                Integer wins = rs.getInt(4);
                // TODO: change the average win time and rank
                user = new JPokerUser(name, password, gamesPlayed, wins, 0, 0.0);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean login(String loginName, String password) throws RemoteException {
        try {
            if (!validateUser(loginName, password))
                throw new Error("Could not Validate User");
            synchronized (onlineUserPath) {
                if (checkRepeatLogin(loginName))
                    throw new Error("Repeat Login");
                writeOnlineUser(loginName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }
        return true;
    }

    @Override
    public boolean register(String name, String password) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO c3358.user (username, password) VALUES (?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, password);
            return stmt.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new Error("UserName is taken");
        }
    }

    @Override
    public boolean logout(String name) throws RemoteException {
        synchronized (onlineUserPath) {
            try {
                ArrayList<String> onlineUsers = readOnlineUser();
                if (!onlineUsers.remove(name)) {
                    throw new Error("User not online");
                }
                writeOnlineUser(onlineUsers);
                roomManager.removeFromRoom(name);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error(e);
            }
        }
        return true;
    }

    @Override
    public JPokerUserTransferObject getUser(String name) throws RemoteException {
        JPokerUser user = getUserFromDatabase(name);
        if (user != null) {
            return new JPokerUserTransferObject(user);
        } else {
            throw new Error("Unable to fetch user " + name + " from database");
        }
    }

    class CheckPlayer implements Runnable {
        public JPokerUserTransferObject user;

        public CheckPlayer(JPokerUserTransferObject user) {
            this.user = user;
        }

        @Override
        public void run() {

            if (roomManager.isUserPlaying(user.getName())) {
                System.err.println("User " + user.getName()
                        + " is already playing reject join message.");
                return;
            }
            JPokerUser player = getUserFromDatabase(user.getName());
            roomManager.allocateRoom(player);
        }
    }

    private class ReadMessage implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    JMSMessage message = jmsHelper.receiveMessage(jmsHelper.queueReader);
                    new Thread(new CheckPlayer(message.getUser())).start();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//	Use this login function when not working with a database
//	@Override
//	public boolean register(String name, String password) throws RemoteException {
//		ArrayList<JPokerUser> users = null;
//		JPokerUser newUser = null;
//		synchronized (userInfoPath) {
//			try {
//				users = readUserInfo();
//				int userCount = 0;
//				if(users != null)
//					userCount = users.size();
//				if (users != null && existsSameLoginNameUser(users, name)) {
//					throw new Error("UserName is taken");
//				}
//				newUser = new JPokerUser(name, password, userCount);
//				writeUserInfo(newUser);
//			} catch (Exception e) {
//				System.err.print(e);
//				e.printStackTrace();
//			}
//		}
//		return (newUser != null);
//	}
//	private boolean validateUser(String loginName, String password) throws ClassNotFoundException {
//		boolean isValidated = false;
//		ArrayList<JPokerUser> users = readUserInfo();
//		JPokerUser user = getUserWithName(users, loginName);
//		if (user != null && user.verifyPassword(password)) {
//			isValidated = true;
//		}
//		return isValidated;
//	}
    //    private JPokerUser getUserWithName(ArrayList<JPokerUser> users, String username) {
//        JPokerUser user = null;
//        for (JPokerUser registeredUser : users) {
//            if (registeredUser.getName().equals(username)) {
//                user = registeredUser;
//            }
//        }
//        return user;
//    }
//    private boolean existsSameLoginNameUser(ArrayList<JPokerUser> users, String username) {
//        return (getUserWithName(users, username) != null);
//    }
//    public ArrayList<JPokerUser> readUserInfo() {
//        ArrayList<JPokerUser> users;
//        synchronized (userInfoPath) {
//            try (ObjectInputStream userInfoReader = new ObjectInputStream(new FileInputStream(userInfoPath))) {
//                {
//                    users = (ArrayList<JPokerUser>) userInfoReader.readObject();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new Error(e);
//            }
//        }
//
//        return users;
//    }
//    public void writeUserInfo(JPokerUser user) throws IOException, ClassNotFoundException {
//        synchronized (userInfoPath) {
//            ArrayList<JPokerUser> users = readUserInfo();
//
//            if (users == null) {
//                users = new ArrayList<>();
//            }
//            users.add(user);
//
//            try (ObjectOutputStream userInfoWriter = new ObjectOutputStream(new FileOutputStream(userInfoPath))) {
//                userInfoWriter.writeObject(users);
//            }
//        }
//    }

}

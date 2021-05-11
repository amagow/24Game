package JPokerGame;

import Common.JPokerInterface;
import Common.JPokerUserTransferObject;
import JPokerGame.Dialog.LoginDialog;
import JPokerGame.Dialog.RegisterDialog;
import jakarta.jms.JMSException;

import java.awt.BorderLayout;
import java.rmi.Naming;

import javax.naming.NamingException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class JPokerClient extends JFrame implements Runnable {
    private JMSHelperClient jmsHelper;

    private JPokerInterface gameProvider;
    private JPokerUserTransferObject user = null;
    private int roomId = -1;
    private MyTabbedPane tabbedPane;

    public JPokerClient() throws JMSException, NamingException {
        try {
            gameProvider = (JPokerInterface) Naming.lookup("Server.JPokerServer");
        } catch (Exception e) {
            System.err.println("Failed accessing RMI: " + e);
        }
    }

    public static void main(String[] args) throws JMSException, NamingException {
        SwingUtilities.invokeLater(new JPokerClient());
    }

    public MyTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void sendUserMessage() {
        this.jmsHelper.sendMessage(user);
    }

    @Override
    public void run() {
        JPokerClient frame;
        try {
            frame = new JPokerClient();

            RegisterDialog registerDialog = new RegisterDialog(frame, true, gameProvider);
            LoginDialog loginDialog = new LoginDialog(frame, true, registerDialog, gameProvider);
            loginDialog.setVisible(true);

            this.user = loginDialog.getUser();
            this.jmsHelper = new JMSHelperClient(this, user);

            tabbedPane = new MyTabbedPane(this, gameProvider, loginDialog.getUser());
            frame.setTitle("JPoker 24-Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.add(tabbedPane, BorderLayout.CENTER);

            frame.pack();
            frame.setVisible(true);
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }

    public JPokerUserTransferObject getUser() {
        return user;
    }
}

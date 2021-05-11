package Common;

import javax.script.ScriptException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface JPokerInterface extends Remote {
    boolean login(String loginName, String password) throws RemoteException;

    boolean logout(String name) throws RemoteException;

    boolean register(String name, String password) throws RemoteException, SQLException;

    JPokerUserTransferObject getUser(String name) throws RemoteException;

    String getAnswer(JPokerUserTransferObject user, String answer) throws RemoteException, ScriptException;
}

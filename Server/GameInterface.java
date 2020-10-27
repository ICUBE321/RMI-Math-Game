
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {

    public String setUserName(String userName) throws
    RemoteException;

    public String generateEquation() throws 
    RemoteException;

    public double getCorrectResult() throws
    RemoteException;

    public void submitAnswer(double userInput, double rightAnswer) throws
    RemoteException;

    public int getTime() throws
    RemoteException;

    public String setGameMode(String userMode) throws
    RemoteException;

    public void runGame() throws 
    RemoteException;

    public String printScore(double question_count) throws
    RemoteException;

    // public void exitGame() throws
    // RemoteException;

    // public void displayUserHistory() throws
    // RemoteException;
}


import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class GameServer {
    public static void main(String[] args) {
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            System.out.println("GameServer running...");
            String name = "GameServer";
            GameInterface gi = new GameImpl();
            GameInterface stub = (GameInterface) UnicastRemoteObject.exportObject(gi, 0);
            Registry reg = LocateRegistry.getRegistry();
            reg.rebind(name, stub);
            System.out.println("GameServer bound in registry");
        } catch (Exception e) {
            System.out.println("GameServer: "+e.getMessage());
            e.printStackTrace();
        }
    }
}

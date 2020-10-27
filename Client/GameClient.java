import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class GameClient {
    
    public GameClient() {

    }

    private void launchGame() {
        System.out.println("Enter yes to start game or no to change game mode: ");//ask if user is ready
    }

    private void getUserName() {
        System.out.println("Please enter your preferred user name: ");
    }

    private void getGameMode() {
        System.out.println("Choose game mode: type 1 for 30s or type 2 for 60s");
    }

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: java GameClient machineName");
            System.exit(0);
        }

        String serverMessage;
        String clientMessage;
        Scanner streamMessage;
        int time_left;
        double question_count; //keeps track of the number of questions answered
    
        try {
            GameClient user = new GameClient();
            String name = "GameServer";
            Registry registry = LocateRegistry.getRegistry();
            GameInterface gi = (GameInterface) registry.lookup(name);
            //run game gi functions; setting up the game
            user.getUserName(); //print welcome message

            streamMessage = new Scanner(System.in);
            clientMessage = streamMessage.nextLine(); //get username

            serverMessage = gi.setUserName(clientMessage); //set username

            System.out.println(serverMessage); //display message from server

            user.getGameMode();//select game mode

            streamMessage = new Scanner(System.in);
            clientMessage = streamMessage.nextLine(); //get username

            serverMessage = gi.setGameMode(clientMessage);//set game mode in the server
            System.out.println(serverMessage);//receive set game mode message

            //if yes, start game and if no ask for game mode again
            int status = 0;//status of user's readiness
            while(status != 1) {
                user.launchGame();//check if user is ready to play

                streamMessage = new Scanner(System.in);
                clientMessage = streamMessage.nextLine(); //get username


                if(clientMessage.contains("yes")) {
                    status = 1;
                } else {
                    user.getGameMode();//select game mode

                    streamMessage = new Scanner(System.in);
                    clientMessage = streamMessage.nextLine(); //get game mode

                    serverMessage = gi.setGameMode(clientMessage);//set game mode in the server
                    System.out.println(serverMessage);//receive set game mode message
                }
            }
            //run the game logic
            question_count = 0;
            gi.runGame(); //start timer
            time_left = gi.getTime();//get the game time
            while(time_left > 0) {
                //gameplay
                serverMessage = gi.generateEquation();//get equation from server
                System.out.println(serverMessage);//display equation
                question_count++; //count question

                streamMessage = new Scanner(System.in);

                gi.submitAnswer(streamMessage.nextDouble(), gi.getCorrectResult());//submit user answer
                time_left = gi.getTime();
            }
            
            serverMessage = gi.printScore(question_count); //get the user's score
            System.out.println(serverMessage); //display the score

            // gi.exitGame();
        } catch (Exception e) {
            System.err.println("GameServer exception: "+ e.getMessage());
            e.printStackTrace();
        }
    }
}

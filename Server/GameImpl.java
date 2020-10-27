import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameImpl implements GameInterface {
    
    private String username; //the user's name
    private String gameMode; //the user's game mode
    private int timer; //the time limit for the game mode
    private double correct_result, score; //the right answer to a question, the user's answer and the total score

    public GameImpl() throws RemoteException {
        super();
    }

    public int getTime() {
        int remaining_time = timer;
        return remaining_time;
    }

    public double getCorrectResult() {
        double result = correct_result;
        return result;
    }

    public String setUserName(String userName) {
        username = userName;
        String message = "Hi " + username + "!";
        return message;
    }

    public void runGame() {
        Clock user_timer = new Clock();
        score = 0;
        user_timer.start();
    }

    public String setGameMode(String userMode) {
        int status;
        String message = "Failed";
        if(userMode.contains("1")) {
            gameMode = "30s";
            status = 1;
            timer = 30;
        } else if(userMode.contains("2")) {
            gameMode = "60s";
            status = 1;
            timer = 60;
        } else {
            status = 0;
        }
        if(status == 1) {
            message = gameMode + " Game mode set!";
        }
        return message;
    };


    class Clock extends Thread{
        //function to start timer
        Timer time_counter;
        public Clock() {
            time_counter = new Timer(); //create new timer object
        }
        public void run() {
            
            try {
                time_counter.scheduleAtFixedRate( new TimerTask(){
                    public void run() {
                        if(timer > 0) {
                            --timer;
                            System.out.println("Time left is:"+ timer);
                        } else {
                            time_counter.cancel();
                            time_counter.purge();
                        }
                        
                    }
                }, 0, 1000);
            } catch (Exception e) {
                //TODO: handle exception
                System.out.println("Timer: "+e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //function to generate random equations
    public String generateEquation() {
        Random rand = new Random();
        Random operatorChoice = new Random();
        double a, b; 
        int opChoice;
        String operator, equation;
        a = rand.nextInt(50);
        b = rand.nextInt(50);
        opChoice = operatorChoice.nextInt(5);
        switch(opChoice) {
            case 0: 
                operator = "+";
                break;
            case 1:
                operator = "-";
                break;
            case 2:
                operator = "*";
                break;
            case 3:
                operator = "/";
                break;
            case 4:
                operator = "%";
                break;
            default: operator = "";
        }

        correct_result = equationSolver(a, b, opChoice);
        
        equation = " "+ a +" "+ operator +" "+ b + " ?";
        return equation;
    }

    //function to solve the equation
    private double equationSolver(double a, double b, int op) {
        double result;
        switch(op) {
            case 0: 
                result = a + b;
                break;
            case 1:
                result = a - b;
                break;
            case 2:
                result = a * b;
                break;
            case 3:
                result = a / b;
                break;
            case 4:
                result = a % b;
                break;
            default: result = Double.POSITIVE_INFINITY;
        }

        return result;
    }

    public void submitAnswer(double userInput, double rightAnswer) {
        if(userInput == (Math.floor(rightAnswer * 100)/100)) {
            score++;
        }
    }

    public String printScore(double question_count) {
        try {
            File fileObject = new File("../Client/" + username + "'s Score");
            //current date and time
            Date current_date = Calendar.getInstance().getTime();  
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
            String dateString = dateFormat.format(current_date);
            //save game info
            if(fileObject.createNewFile()) {
                FileWriter myWriter = new FileWriter(fileObject);
                myWriter.write("\nDate: " + dateString + ", Game mode: " + gameMode + ", Final score: " + score + "/" + question_count);
                myWriter.close();
            } else {
                // open file in append mode
                Writer output = new BufferedWriter(new FileWriter(fileObject, true));
                output.append("\nDate: " + dateString + ", Game mode: " + gameMode + ", Final score: " + score + "/"
                        + question_count);
                output.close();
            }
            // return total score
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "You've got " + score + " right out of " + question_count + " questions!";
    }

    // public void displayUserHistory() {

    // }

    // public void exitGame() {
    //     System.out.println(userName + "'s game closed...");
    //     System.exit(0);
    // };
    
}

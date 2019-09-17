import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Game implements GameInterface
{
    private PlayerInterface player1;
    private PlayerInterface player2;
    private Board shipStoreBoard1;
    private Board shipStoreBoard2;
    private int noOfShipsPlaced1;
    private int noOfShipsPlaced2;
    private HashSet<Position> shotsTaken1;
    private HashSet<Position> shotsTaken2;
    private int turnCounter = 1;
    
    public static void main(String args[]){
        GameInterface newGame = null;
        Scanner sc = new Scanner(System.in);
        boolean continuing = true;
        String player1Type = "human";
        String player2Type = "cpu";
        
        System.out.println("==========BattleShips==========");
        while (continuing == true){
            System.out.println("a - Start a new game");
            System.out.println("b - Load a saved game");
            System.out.println("c - Continue a loaded or paused game");
            System.out.println("d - Save the current game");
            System.out.println("e - Set the player types");
            System.out.println("f - Exit the program");
            System.out.println("Enter the letter for the command you want to run: ");
            String letter = sc.next();
            
            switch (letter){
                case "a":
                    System.out.println("Enter the name of player 1: ");
                    String player1Name = sc.next();
                    
                    System.out.println("Enter the name of player 2: ");
                    String player2Name = sc.next();
                 
                    if (player1Type.equals("human") && player2Type.equals("human")){
                        newGame = new Game(new HumanConsolePlayer(player1Name), new HumanConsolePlayer(player2Name));
                    }
                    
                    else if (player1Type.equals("human") && player2Type.equals("cpu")){
                        newGame = new Game(new HumanConsolePlayer(player1Name), new ComputerPlayer(player2Name));
                    }
                    else if (player1Type.equals("cpu") && player2Type.equals("human")){
                        newGame = new Game(new ComputerPlayer(player1Name), new HumanConsolePlayer(player2Name));
                    }
                    else{
                        newGame = new Game(new ComputerPlayer(player1Name), new ComputerPlayer(player2Name));
                    }
                    
                    newGame.play();
                    break;
                    
                case "b":
                    try{
                        String dir = System.getProperty("user.dir");
                        String filename = dir + "\\gameData.txt";
                        newGame = new Game(new HumanConsolePlayer("1"), new ComputerPlayer("2"));
                        newGame.loadGame(filename);
                        System.out.println("Game loaded");
                    }
                    catch(IOException e){
                        System.out.println("An error occured. Please try again.");
                    }
                    break;
                    
                case "c":
                    if (newGame == null){
                        System.out.println("No game data found");
                    }
                    else{
                        newGame.play();
                    }
                    break;
                
                case "d":
                    int counter = 0;
                    boolean saved = false;
                    
                    while (saved == false){
                        try{
                            String dir = System.getProperty("user.dir");
                            String filename = dir + "\\gameData.txt";
                            newGame.saveGame(filename);
                            System.out.println("Game saved");
                            saved = true;
                        }
                        catch (IOException e){
                            if (counter < 3){
                                System.out.println("Please enter a valid directory");
                                counter += 1;
                            }
                            else{
                                System.out.println("You have entered an invalid directory 3 times now. Please try again later.");
                                saved = true;
                            }
                        }
                    }
                    break;
                    
                case "e":
                    System.out.println("Enter 'human' if player 1 is a human, or 'cpu' if it is a computer: ");
                    player1Type = sc.next();
                    
                    System.out.println("Enter 'human' if player 2 is a human, or 'cpu' if it is a computer: ");
                    player2Type = sc.next();
                    break;
                    
                case "f":
                    continuing = false;
                    break;
                    
                default:
                    System.out.print('\u000C');
                    System.out.println("Invalid command");
                    break;
            }
        }
    }
    
    public Game(PlayerInterface player1, PlayerInterface player2){
        this.player1 = player1;
        this.player2 = player2;
        shipStoreBoard1 = new Board();
        shipStoreBoard2 = new Board();
        shotsTaken1 = new HashSet<Position>();
        shotsTaken2 = new HashSet<Position>();
    }
    
    public PlayerInterface play(){
        int[] shipSizesStore = {5, 4, 3, 3, 2}; //meant to be {5, 4, 3, 3, 2}
        boolean player1turn = true;
        
        try{
            System.out.println(player1.toString() + " is placing their ships");
            for (int i = noOfShipsPlaced1; i < 5; i++){ // this should be i < 5, but it is changed for testing purposes
                ShipInterface ship = new Ship(shipSizesStore[i]);
                System.out.println(shipStoreBoard1.toString());
                Placement placement = player1.choosePlacement(ship, shipStoreBoard1.clone());
                System.out.println("Now placing a " + shipSizesStore[i] + "-length ship");
                
                shipStoreBoard1.placeShip(ship, placement.getPosition(), placement.isVertical());
                noOfShipsPlaced1 = i + 1;
            }
            System.out.print('\u000C');
            System.out.println(player2.toString() + " is placing their ships");
            for (int j = noOfShipsPlaced2; j < 5; j++){
                ShipInterface ship = new Ship(shipSizesStore[j]);
                
                System.out.println(shipStoreBoard2.toString());
                //System.out.println(player2.toString() + " is placing a " + shipSizesStore[j] + "-length ship");
                Placement placement = player2.choosePlacement(ship, shipStoreBoard2.clone());
                System.out.println("Now placing a " + shipSizesStore[j] + "-length ship");
                shipStoreBoard2.placeShip(ship, placement.getPosition(), placement.isVertical());
                noOfShipsPlaced2 = j + 1;
            }
            System.out.print('\u000C');
            while (shipStoreBoard1.allSunk() == false && shipStoreBoard2.allSunk() == false){
                if (player1turn == true){
                    System.out.println("------------------Turn " + turnCounter + " ------------------");
                    System.out.println(player1.toString() + "'s turn to shoot");
                    Position shotPosition = player1.chooseShot();
                    shipStoreBoard2.shoot(shotPosition);
                    shotsTaken1.add(shotPosition);
                    
                    ShipStatus sStat = shipStoreBoard2.getStatus(shotPosition);
                    ShotStatus status = convertStatus(sStat);
                    player1.shotResult(shotPosition, status);
                    player2.opponentShot(shotPosition);
                    
                    player1turn = false;
                }
                else{
                    System.out.println(player2.toString() + "' turn to shoot");
                    Position shotPosition = player2.chooseShot();
                    shipStoreBoard1.shoot(shotPosition);
                    shotsTaken2.add(shotPosition);
                    
                    ShipStatus sStat = shipStoreBoard1.getStatus(shotPosition);
                    ShotStatus status = convertStatus(sStat);
                    player2.shotResult(shotPosition, status);
                    player1.opponentShot(shotPosition);
                    
                    turnCounter++;
                    player1turn = true;
                }

            }
            if (shipStoreBoard1.allSunk() == true){
                System.out.println(player2.toString() + " sank all of " + player1.toString() + "'s ships and they are the winner!");
                return player2;
            }
            else{
                System.out.println(player1.toString() + " sank all of " + player2.toString() + "'s ships and they are the winner!");
                return player1;
            }
        }
        catch (PauseException e){
            System.out.println("Game paused");
            return null;
        }
        catch (InvalidPositionException e){
            if (player1turn == true){
                System.out.println(player1.toString() + " entered an invalid position so they forefit the match");
                System.out.println(player2.toString() + " is the winner!");
                return player2;
            }
            else{
                System.out.println(player2.toString() + " entered an invalid position so they forefit the match");
                System.out.println(player1.toString() + " is the winner!");
                return player1;
            }
        }
        catch (ShipOverlapException e){
            if (player1turn == true){
                System.out.println(player1.toString() + " placed the ship on top of another ship so they forefit the match");
                System.out.println(player2.toString() + " is the winner!");
                return player2;
            }
            else{
                System.out.println(player2.toString() + " placed the ship on top of another ship so they forefit the match");
                System.out.println(player1.toString() + " is the winner!");
                return player1;
            }
        }
    }
    
    public ShotStatus convertStatus(ShipStatus status){
        if (status == ShipStatus.HIT){
            return ShotStatus.HIT;
        }
        else if (status == null){
            return ShotStatus.MISS;
        }
        else if (status == ShipStatus.SUNK){
            return ShotStatus.SUNK;
        }
        else{
            return null;
        }
    }
    
    public void saveGame(String filename) throws IOException{
        ArrayList<String> savedGame = new ArrayList<String>();
        
        if (player1 instanceof HumanConsolePlayer){
            savedGame.add("human");
        }
        else{
            savedGame.add("cpu");
        }
        savedGame.add(player1.toString());
        
        HashMap<ShipInterface, Placement> ships1 = shipStoreBoard1.getHashMap();
        for (ShipInterface ship: ships1.keySet()){  
            savedGame.add(ships1.get(ship).getPosition() + "," + ship.getSize() + "," + ships1.get(ship).isVertical());
        }
        
        savedGame.add("#");
        
        for (Position p: shotsTaken1){
            savedGame.add(p.toString());
        }
        
        savedGame.add("#");
        
        if (player1 instanceof HumanConsolePlayer){
            savedGame.add("human");
        }
        else{
            savedGame.add("cpu");
        }
        savedGame.add(player2.toString());
                
        HashMap<ShipInterface, Placement> ships2 = shipStoreBoard2.getHashMap();
        for (ShipInterface ship: ships2.keySet()){  
            savedGame.add(ships2.get(ship).getPosition() + "," + ship.getSize() + "," + ships2.get(ship).isVertical());
        }
        
        savedGame.add("#");
        
        for (Position p: shotsTaken2){
            savedGame.add(p.toString());
        }
        
        File saveFile = new File(filename);
        FileWriter writer = null;
        Scanner sc = new Scanner(System.in);
        
        if (saveFile.exists() == true){
            System.out.println("Are you sure you want to overwrite this file?");
            System.out.println("Enter 'yes' or 'no': ");
            String answer = sc.next();
            
            if (answer.equals("yes")){
                boolean del = saveFile.delete();
                if (del == true){
                    writer = new FileWriter(saveFile);
                    for (String s: savedGame){
                        writer.write(s + System.getProperty("line.separator"));
                    }
                    System.out.println("File overwritten successfully");
                }
                else{
                    System.out.println("Error: failed to delete file");
                }
            }
        }
        else{
            saveGame(filename);
        }
           
        writer.flush();
        writer.close();
    }
    
    public void loadGame(String filename) throws IOException{
        try{
            BufferedReader bReader = new BufferedReader(new FileReader(new File(filename)));
            
            String line = bReader.readLine();
            Scanner sc = null;
            
            if (line.equals("human")){
                String name = bReader.readLine();
                player1 = new HumanConsolePlayer(name);
            } 
            else{
                String name = bReader.readLine();
                player1 = new ComputerPlayer(name);
            }
            
            while ((line = bReader.readLine()).equals("#") == false){
                sc = new Scanner(line);
                sc.useDelimiter(",");
                
                int x = sc.nextInt();
                int y = sc.nextInt();
                int size = sc.nextInt();
                boolean isVertical = sc.nextBoolean();
                
                shipStoreBoard1.placeShip(new Ship(size), new Position(x, y), isVertical);
                noOfShipsPlaced1 += 1;
            }
            
            while ((line = bReader.readLine()).equals("#") == false){
                sc = new Scanner(line);
                sc.useDelimiter(",");
                int x = sc.nextInt();
                int y = sc.nextInt();
                
                shotsTaken1.add(new Position(x, y));
            }
            
            if ((line = bReader.readLine()).equals("human")){
                String name = bReader.readLine();
                player2 = new HumanConsolePlayer(name);
            }
            else{
                String name = bReader.readLine();
                player2 = new ComputerPlayer(name);
            }
            
            while ((line = bReader.readLine()).equals("#") == false){
                sc = new Scanner(line);
                sc.useDelimiter(",");
                int x = sc.nextInt();
                int y = sc.nextInt();
                int size = sc.nextInt();
                boolean isVertical = sc.nextBoolean();
                
                shipStoreBoard2.placeShip(new Ship(size), new Position(x, y), isVertical);
                noOfShipsPlaced2 += 1;
            }
            while ((line = bReader.readLine()) != null){
                sc = new Scanner(line);
                sc.useDelimiter(",");
                int x = sc.nextInt();
                int y = sc.nextInt();
                
                shotsTaken2.add(new Position(x, y));
            }
            
            for (Position p: shotsTaken1){
                shipStoreBoard2.shoot(p);
                ShipStatus sStat = shipStoreBoard2.getStatus(p);
                ShotStatus status = convertStatus(sStat);
                player1.shotResult(p, status);
                turnCounter++;
            }
            for (Position p: shotsTaken2){
                shipStoreBoard1.shoot(p);
                ShipStatus sStat = shipStoreBoard1.getStatus(p);
                ShotStatus status = convertStatus(sStat);
                player2.shotResult(p, status);
            }
            System.out.print('\u000C');

            bReader.close();
        }
        catch (InvalidPositionException e){
            System.out.println("Well hello again Steven! You have gathered all 2 Easter eggs and now you earn my full respecc. Congratulations!");
        }
        catch (ShipOverlapException e){
            System.out.println("We're done now");
        }
    }
}

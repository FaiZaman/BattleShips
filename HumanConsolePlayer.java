import java.util.Scanner;

public class HumanConsolePlayer implements PlayerInterface
{
    private String name;
    private PlayerBoard humanPlayerBoard;
    
    public HumanConsolePlayer(String name){
        this.name = name;
        humanPlayerBoard = new PlayerBoard();   //this is the hits and misses board
    }
    
    public Placement choosePlacement(ShipInterface ship, BoardInterface board) throws PauseException{ //This board is a copy of the actual board. It is used in order to return a placement.
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter the x-coordinate of the top/left position of the ship: ");
        String line = sc.next();
        if (line.equals("pause")){
            throw new PauseException();
        }
        int x = Integer.parseInt(line); //asks the user for an x-coordinate and checks if it is an integer
        
        System.out.println("Enter the y-coordinate of the top/left position of the ship: ");
        line = sc.next();
        if (line.equals("pause")){
            throw new PauseException(); //checks if the line entered is pause and if it is then pause the game
        }
        int y = Integer.parseInt(line); //asks the user for a y-coordinate and checks if it is an integer
        
        System.out.println("Enter true if the ship is to be placed vertically, or false if horizontally: ");
        line = sc.next();
        if (line.equals("pause")){
            throw new PauseException();
        }
        boolean z = Boolean.parseBoolean(line); //asks the user for the orientation and checks if it is a boolean
        while (z != true && z != false){
            System.out.println("Please enter either true or false: ");
            line = sc.next();
            if (line.equals("pause")){
                throw new PauseException();
            }
            z = Boolean.parseBoolean(line);
        }
        
        try{
            board.placeShip(ship, new Position(x, y), z);   //checks if the chosen position is valid so that the ship does not trail off the board or is on top of another ship
            return new Placement(new Position(x, y), z);
        }   
        catch (InvalidPositionException e){
            System.out.println("Please choose x and y coordinates between 1 and 10");
            return choosePlacement(ship, board);
        }
        catch (ShipOverlapException e){
            System.out.println("You placed a ship on top of another ship. Please try again.");
            return choosePlacement(ship, board);
        }
    }
    
    public Position chooseShot() throws PauseException{
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter the x-coordinate of where you want to shoot: ");
        String line = sc.next();
        if (line.equals("pause")){
            throw new PauseException();
        }
        int x = Integer.parseInt(line);
        
        System.out.println("Enter the y-coordinate of where you want to shoot ");
        line = sc.next();
        if (line.equals("pause")){
            throw new PauseException();
        }
        int y = Integer.parseInt(line);
        
        try{
            return new Position(x, y);
        }
        catch (InvalidPositionException e){
            System.out.println("Please choose x and y coordinates between 1 and 10");
            return chooseShot();
        }
    }
    
    public void shotResult(Position position, ShotStatus status){
        System.out.println("You shot at position " + position.toString() + " and the result was " + status);
        humanPlayerBoard.shoot(position, status);   //shoots at the hits and misses board and prints it for the user to see
        System.out.println(humanPlayerBoard.toString());
    }
    
    public void opponentShot(Position position){
        System.out.println("Your opponent shot at position " + position.toString());
    }
    
    public String toString(){
        return name;
    }
}

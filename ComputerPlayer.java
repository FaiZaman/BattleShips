import java.util.Random;

public class ComputerPlayer implements PlayerInterface
{
    private String name;
    private PlayerBoard computerPlayerBoard;
    private Random rand;
    private Position shipHit;
    private int xChange = 0;
    private int yChange = -1;
    private int counter = 1;
    
    public ComputerPlayer(String name){
        this.name = name;
        computerPlayerBoard = new PlayerBoard();
        rand = new Random(115);
    }
    
    public Placement choosePlacement(ShipInterface ship, BoardInterface board) throws PauseException{
        int x = rand.nextInt(10) + 1;
        int y = rand.nextInt(10) + 1;
        boolean z = rand.nextBoolean();
        try{
            board.placeShip(ship, new Position(x, y), z);
            return new Placement(new Position(x, y), z);
        }
        catch (InvalidPositionException e)
        {
            System.out.println("Please choose x and y coordinates between 1 and 10 where the ship does not go off the board");
            return choosePlacement(ship, board);
        }
        catch (ShipOverlapException e){
            System.out.println("You placed a ship on top of another ship. Please try again.");
            return choosePlacement(ship, board);
        }
    }
    
    public Position chooseShot() throws PauseException{
        int x = rand.nextInt(10) + 1;
        int y = rand.nextInt(10) + 1;
        try{
            if (shipHit == null){
                Position p = new Position(x, y);
                ShotStatus status = computerPlayerBoard.getStatus(p);
                if (status == null) {
                    return p;
                }
                else{
                    return chooseShot();
                }
            }
            else{
                Position p = new Position(shipHit.getX() + xChange*counter, shipHit.getY() + yChange*counter);
                while (computerPlayerBoard.getStatus(p) == ShotStatus.HIT){
                    counter++;
                    p = new Position(shipHit.getX() + xChange*counter, shipHit.getY() + yChange*counter);
                }
                if (computerPlayerBoard.getStatus(p) == null){
                    return p;
                }
                else{
                    changeCoordinates();
                    counter = 1;
                    return chooseShot();
                }
            }
        }
        catch (InvalidPositionException e){
            changeCoordinates();
            counter = 1;
            return chooseShot();
        }
    }
    
    public void shotResult(Position position, ShotStatus status){
        System.out.println("You shot at position " + position.toString() + " and the result was " + status);
        computerPlayerBoard.shoot(position, status);
        if ((computerPlayerBoard.getStatus(position) == ShotStatus.HIT)){
            if (shipHit == null){
                shipHit = position;
            } 
            else{
                counter++;
            }
        }
        else if (computerPlayerBoard.getStatus(position) == ShotStatus.SUNK){
            counter = 1;
            shipHit = null;
        } 
        else{
            counter = 1;
        }
        System.out.println(computerPlayerBoard.toString());   
    }
    
    public void opponentShot(Position position){
        System.out.println("Your opponent shot at position " + position.toString());
    }
    
    public String toString(){
        return name;
    }
    
    public void changeCoordinates(){
        if (xChange == 0 && yChange == -1){
            xChange = 1;
            yChange = 0;
        }
        else if (xChange == 1 && yChange == 0){
            xChange = 0;
            yChange = 1;
        }
        else if (xChange == 0 && yChange == 1){
            xChange = -1;
            yChange = 0;
        }
        else{
            xChange = 0;
            yChange = -1;
        }
    }
}

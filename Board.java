import java.util.HashMap;

public class Board implements BoardInterface
{
    private int rows = 10;
    private int cols = 10;
    private ShipInterface[][] playingBoard;
    private HashMap<ShipInterface, Placement> ships;
    
    public Board(){
        playingBoard = new ShipInterface[rows][cols];   
        ships = new HashMap<ShipInterface, Placement>();    //stores ships and their placements
        
    }
    
    public void placeShip(ShipInterface ship, Position position, boolean isVertical) throws InvalidPositionException, ShipOverlapException{
        if (position.getX() > 10 || position.getX() < 1 || position.getY() > 10 || position.getY() < 1){
            throw new InvalidPositionException();
        }
        Position[] positionArray = new Position[ship.getSize()];    //creates an array of positions. array size = size of the ship
       
        for (int i = 0; i < ship.getSize(); i++){  //checks if the positions ahead are valid
            if (isVertical == true){
                Position pos = new Position(position.getX(), position.getY() + i);  
                if (playingBoard[pos.getX() - 1][pos.getY() - 1] == null){  
                    positionArray[i] = pos;
                }
                else{
                    throw new ShipOverlapException();
                }
            }
            else {
                Position pos2 = new Position(position.getX() + i, position.getY());
                if (playingBoard[pos2.getX() - 1][pos2.getY() - 1] == null){
                    positionArray[i] = pos2;
                }
                else{
                    throw new ShipOverlapException();
                }
            }
        }
        ships.put(ship, new Placement(position, isVertical));   //if everything is valid then add ship and placement to hashmap
        for (int j = 0; j < ship.getSize(); j++){
            playingBoard[positionArray[j].getX() - 1][positionArray[j].getY() - 1] = ship;  //loop through hashmap and assign board coordinates to ships
        }
    }
    
    public void shoot(Position position) throws InvalidPositionException{
        if (position.getX() > 10 || position.getX() < 1 || position.getY() > 10 || position.getY() < 1){
            throw new InvalidPositionException();
        }
        ShipInterface ship = playingBoard[position.getX() - 1][position.getY() - 1];
    
        if (ship != null){  //checks if the position where we want to shoot contains a ship and calls the method to shoot its offset if it does
            Position startPosition = ships.get(ship).getPosition();
            int offset = (position.getX() - startPosition.getX()) + (position.getY() - startPosition.getY());
            ship.shoot(offset);
        }
    }
    
    public ShipStatus getStatus(Position position) throws InvalidPositionException{
        if (position.getX() > 10 || position.getX() < 1 || position.getY() > 10 || position.getY() < 1){
            throw new InvalidPositionException();
        }
        ShipInterface ship = playingBoard[position.getX() - 1][position.getY() - 1];
        if (ship != null){  //if there is a ship at the position then create the offset and find its status by calling that method in ship
            Position startPosition = ships.get(ship).getPosition();
            int offset = (position.getX() - startPosition.getX()) + (position.getY() - startPosition.getY());   //find the distance from the top/left position which is the offset
            return ship.getStatus(offset);
        }
        else{
            return null;    //if no ship return null
        }
    }
    
    public boolean allSunk(){
        for (ShipInterface ship: ships.keySet()){  
            if (ship.isSunk() == false){
                return false;   //loop through and check each ship. if at least one is not sunk then return false
            }
        }
        return true;
    }
    
    public String toString(){   //making the board nice and neat
        String board = "";
        try{
            board += "   ";
            for (int i = 0; i < 10; i++){
                board += "   " + (int) (i + 1) + "    ";
            }
            board += "\n";
            for (int c = 0; c < 10; c++){
                board += "\n";
                if (c == 9){
                    board += "10 ";
                }
                else{
                    board += (int) (c + 1) + "  ";
                }
                for (int r = 0; r < 10; r++){
                    Position testPosition = new Position(r + 1, c + 1);
                    if (playingBoard[r][c] != null){
                        if (getStatus(testPosition) == ShipStatus.SUNK){
                            board += " [SUNK] ";
                        }
                        else if (getStatus(testPosition) == ShipStatus.HIT){
                            board += "  [HIT] ";
                        }
                        else{
                            board += "[INTACT]";
                        }
                    }
                    else{
                        board += " [----] ";
                    }
                    if (r == 9){
                        board += "  ";
                        board += c + 1;
                    }
                }
                board += System.lineSeparator();
            }
            board += "\n" + "   ";
            for (int i = 0; i < 10; i++){
                board += "   " + (int) (i + 1) + "    ";
            }
            board += "\n";
        }
        catch (InvalidPositionException e){
            System.out.println("Oh hi Steven, it seems you have discovered the first programming Easter egg (out of 2)! Congratulations - you win half of my respecc.");
        }
        return board;
    }
    
    public BoardInterface clone(){
        Board boardClone = new Board();
        for (int c = 0; c < 10; c++){   //loop through the board and assign each coordinate of the old board to the same value in the new one
            for (int r = 0; r < 10; r++){
                boardClone.playingBoard[r][c] = playingBoard[r][c];
            }
        }
        return boardClone;
    }
    
    public HashMap<ShipInterface, Placement> getHashMap(){
        return ships;
    }
}

import java.util.*;

public class Ship implements ShipInterface
{
    private int size;
    private ShipStatus[] shipSquares;
    
    public Ship(int size){
        this.size = size;
        shipSquares = new ShipStatus[size];
        
        for (int i = 0; i < size; i++){
            shipSquares[i] = ShipStatus.INTACT; //created an array of ShipStatuses for the ships and set them all to intact originally
        }
    }
    
    public int getSize(){
        return size;
    }
    
    public boolean isSunk(){
        if (shipSquares[0] == ShipStatus.SUNK){
            return true;   //if one ship square is sunk then the entire ship is sunk
        }
        return false;
    }
    
    public void shoot(int offset) throws InvalidPositionException{
        shipSquares[offset] = ShipStatus.HIT;   //assign the offset shot to be hit
        int j = 0;
        
        for (int i = 0; i < size; i++){
            if (shipSquares[i] == ShipStatus.HIT){
                j += 1; //increment j depending on how many squares of the ship are sunk
            }
        }
        if (j == size){
            for (int i = 0; i < size; i++){
                shipSquares[i] = ShipStatus.SUNK;   //if j equals the size then set all the ship squares to be sunk
            }
        }
    }
    
    public ShipStatus getStatus(int offset) throws InvalidPositionException{
        if (offset >= size){
            throw new InvalidPositionException();
        }
        return shipSquares[offset];     //return the status at the offset
    }
}

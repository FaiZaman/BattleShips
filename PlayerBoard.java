
public class PlayerBoard
{
    private int rows = 10;
    private int cols = 10;
    private ShotStatus[][] playerPlayingBoard;
    
    public PlayerBoard(){
        playerPlayingBoard = new ShotStatus[rows][cols];    //create the hits and misses board
    }
    
    public void shoot(Position position, ShotStatus status){
        playerPlayingBoard[position.getX() - 1][position.getY() - 1] = status;  //assigns statuses at the position
    }
    
    public ShotStatus getStatus(Position position){
        return playerPlayingBoard[position.getX() - 1][position.getY() - 1];    //returns the status at the position
    }
    
    public String toString(){   //creates the hits and misses board nicely and neatly
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
                    if (playerPlayingBoard[r][c] != null){
                        if (getStatus(testPosition) == ShotStatus.SUNK){
                            board += " [SUNK] ";
                        }
                        else if (getStatus(testPosition) == ShotStatus.HIT){
                            board += " [HIT]  ";
                        }
                        else{
                            board += " [MISS] ";
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
            System.out.println("Oh hi Steven, it seems you have discovered a programming Easter egg! Congratulations - you win my respect.");
        }
        return board;
    }
}

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.util.Random;

/**
 * Handles display, movement, and fly eating capabalities for frogs
 */
public class Frog
{
    protected static final String imgFile = "frog.png";

    protected GridLocation location;

    protected FlyWorld world;

    protected BufferedImage image;
    

    /**
     * Creates a new Frog object.<br>
     * The image file for a frog is frog.jpg<br>
     *
     * @param loc a GridLocation
     * @param fw the FlyWorld the frog is in
     */
    public Frog(GridLocation loc, FlyWorld fw)
    {
    // FILL IN
        location=loc;
        world=fw;
        try
        {
            image = ImageIO.read(new File(imgFile));
        }
        catch (IOException ioe)
        {
            System.out.println("Error");
        }
        location.setFrog(this);
  
    }

    /**
     * @return BufferedImage the image of the frog
     */
    public BufferedImage getImage()
    {
    return image;
    }

    /**
     * @return GridLocation the location of the frog
     */
    public GridLocation getLocation()
    {
    return location;
    }

    /**
     * @return boolean, always true
     */
    public boolean isPredator()
    {
    return true;
    }

    /**
    * Returns a string representation of this Frog showing
    * the location coordinates and the world.
    *
    * @return the string representation
    */
    public String toString(){
        String s = "Frog in world:  " + this.world + "  at location (" + this.location.getRow() + ", " + this.location.getCol() + ")";
        return s;
    }

    /**
     * Generates a list of <strong>ALL</strong> possible legal moves<br>
     * for a frog.<br>
     * You should select all possible grid locations from<br>
     * the <strong>world</strong> based on the following restrictions<br>
     * Frogs can move one space in any of the four cardinal directions but<br>
     * 1. Can not move off the grid<br>
     * 2. Can not move onto a square that already has frog on it<br>
     * GridLocation has a method to help you determine if there is a frog<br>
     * on a location or not.<br>
     *
     * @return GridLocation[] a collection of legal grid locations from<br>
     * the <strong>world</strong> that the frog can move to
     */
    // public GridLocation[] generateLegalMoves() {
    //     int currentRow = location.getRow();
    //     int currentCol = location.getCol();
    
    //     ArrayList<GridLocation> frogPositions = new ArrayList<>();
    
    //            // Check if frog can move north
    //            if (world.isValidLoc(currentRow - 1, currentCol) && !world.getLocation(currentRow - 1, currentCol).hasPredator()) {
    //             frogPositions.add(world.getLocation(currentRow - 1, currentCol));
    //         }
        
    //         // Check if frog can move south
    //         if (world.isValidLoc(currentRow + 1, currentCol) && !world.getLocation(currentRow + 1, currentCol).hasPredator()) {
    //             frogPositions.add(world.getLocation(currentRow + 1, currentCol));
    //         }
        
    //         // Check if frog can move east
    //         if (world.isValidLoc(currentRow, currentCol - 1) && !world.getLocation(currentRow, currentCol - 1).hasPredator()) {
    //             frogPositions.add(world.getLocation(currentRow, currentCol - 1));
    //         }
        
    //         // Check if frog can move west
    //         if (world.isValidLoc(currentRow, currentCol + 1) && !world.getLocation(currentRow, currentCol + 1).hasPredator()) {
    //             frogPositions.add(world.getLocation(currentRow, currentCol + 1));
    //         }
        
    //     GridLocation[] moves = new GridLocation[frogPositions.size()];
    //     for (int i = 0; i < moves.length; i++) {
    //         moves[i] = frogPositions.get(i);
    //     }
    
    //     return moves;
    // }
    public GridLocation[] generateLegalMoves() {
        int currentRow = location.getRow();
        int currentCol = location.getCol();
    
        ArrayList<GridLocation> frogPositions = new ArrayList<>();
    
        // Check if frog can move north
        if (world.isValidLoc(currentRow - 1, currentCol) && !world.getLocation(currentRow - 1, currentCol).hasPredator()) {
            frogPositions.add(world.getLocation(currentRow - 1, currentCol));
            
        }
        
        // Check if frog can move south
        if (world.isValidLoc(currentRow + 1, currentCol) && !world.getLocation(currentRow + 1, currentCol).hasPredator()) {
            frogPositions.add(world.getLocation(currentRow + 1, currentCol));
        }
    
        // Check if frog can move east
        if (world.isValidLoc(currentRow, currentCol - 1) && !world.getLocation(currentRow, currentCol - 1).hasPredator()) {
            frogPositions.add(world.getLocation(currentRow, currentCol - 1));
        }
    
        // Check if frog can move west
        if (world.isValidLoc(currentRow, currentCol + 1) && !world.getLocation(currentRow, currentCol + 1).hasPredator()) {
            frogPositions.add(world.getLocation(currentRow, currentCol + 1));
        }
    
        // Convert ArrayList to array
        GridLocation[] moves = new GridLocation[frogPositions.size()];
        for (int i = 0; i < moves.length; i++) {
            moves[i] = frogPositions.get(i);
        }
    
        return moves;
    }    
     
    /**s
     * This method updates the frog's position.<br>
     * It should randomly select one of the legal locations(if there any)<br>
     * and set the frog's location to the chosen updated location.
     */
    public void update() {
        GridLocation[] legalMoves = generateLegalMoves();
        if (legalMoves.length > 0) {

            Random random = new Random();
            int ranidx = random.nextInt(legalMoves.length);
            GridLocation newLocation = legalMoves[ranidx];
            location.removeFrog();
            newLocation.setFrog(this);
            location = newLocation;
        }
        else{
            System.out.println("no legal moves");
        }
    }
    
    
    /**
     * This method helps determine if a frog is in a location<br>
     * where it can eat a fly or not. A frog can eat the fly if it<br>
     * is on the same square as the fly or 1 spaces away in<br>
     * one of the cardinal directions
     *
     * @return boolean true if the fly can be eaten, false otherwise
     */ 
    public boolean eatsFly()
    {   
       GridLocation flyLocation= world.getFlyLocation();
       int frogRow= location.getRow();
       int frogCol= location.getCol();
       int flyRow= flyLocation.getRow();
       int flyCol= flyLocation.getCol();
       if (frogRow == flyRow && frogCol== flyCol){
        return true;
       } 
       else if((Math.abs(frogRow - flyRow) == 1 && frogCol == flyCol) ||
       (frogRow == flyRow && Math.abs(frogCol - flyCol) == 1)){
        return true;
       }
       else{
        return false;
       }
    }   
}


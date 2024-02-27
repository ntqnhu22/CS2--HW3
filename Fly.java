import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Contains several methods that aid in the<br>
 * display and movement of Mosca
 */
public class Fly {
    protected static final String imgFile = "fly.png";
    
    protected GridLocation location;
    protected FlyWorld world;
    protected BufferedImage image;

    /**
     * Creates a new Fly object.<br>
     * The image file for a fly is fly.jpg<br>
     *
     * @param loc a GridLocation
     * @param fw the FlyWorld the fly is in
     */
    public Fly(GridLocation loc, FlyWorld fw) {
        location = loc;
        world = fw;

        try {
            image = ImageIO.read(new File(imgFile));
        } catch (IOException ioe) {
            System.out.println("Unable to read image file: " + imgFile);
            // Handle gracefully instead of exiting
            ioe.printStackTrace();
        }
        location.setFly(this);
    }

    /**
     * @return BufferedImage, the image of the fly
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @return GridLocation, the location of the fly
     */
    public GridLocation getLocation() {
        return location;
    }

    /**
     * @return boolean, always false, Mosca is not a predator
     */
    public boolean isPredator() {
        return false;
    }

    /**
    * Returns a string representation of this Fly showing
    * the location coordinates and the world.
    *
    * @return the string representation
    */
    public String toString() {
        return "Fly in world: " + world + " at location (" + location.getRow() + ", " + location.getCol() + ")";
    }
    
    /**
     * This method updates the fly's location in the world
     * The fly can move in one of the four cardinal (N, S, E, W) directions.
     *
     * @param direction one of the four cardinal directions
     */
    public void update(int direction) {
        int nextRow = location.getRow();
        int nextCol = location.getCol();
        if (direction == FlyWorldGUI.NORTH) {
            nextRow--;
        } else if (direction == FlyWorldGUI.SOUTH) {
            nextRow++;
        } else if (direction == FlyWorldGUI.WEST) {
            nextCol--;
        } else {
            nextCol++;
        }
        if (world.isValidLoc(nextRow, nextCol)) {
            location.removeFly(); // Remove the fly from its current location
            location = world.getLocation(nextRow, nextCol); // Update the location
            location.setFly(this); // Set the fly to the new location
        }
    }
}

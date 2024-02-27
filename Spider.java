import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.util.Random;

public class Spider {

    protected static final String imgFile = "spider.png";

    protected GridLocation location;
    protected FlyWorld world;
    protected BufferedImage image;

    public Spider(GridLocation loc, FlyWorld fw) {
        location = loc;
        world = fw;
        try {
            image = ImageIO.read(new File(imgFile));
        } catch (IOException ioe) {
            System.out.println("Unable to read image file: " + imgFile);
            // Handle gracefully instead of exiting
            ioe.printStackTrace();
        }
        location.setSpider(this);
    }

    public BufferedImage getImage() {
        return image;
    }

    public GridLocation getLocation() {
        return location;
    }

    public boolean isPredator() {
        return true;
    }
    public GridLocation[] generateLegalMoves() {
        int currentRow = location.getRow();
        int currentCol = location.getCol();
        int flyRow = world.getFlyLocation().getRow();
        int flyCol = world.getFlyLocation().getCol();
    
        ArrayList<GridLocation> spiderPositions = new ArrayList<>();
    
        // Check if spider can move towards the fly horizontally or vertically
        if (currentRow == flyRow || currentCol == flyCol) {
            if (currentRow == flyRow) {
                if (currentCol > flyCol && !world.getLocation(currentRow, currentCol - 1).hasPredator()) {
                    spiderPositions.add(world.getLocation(currentRow, currentCol - 1));
                } else if (currentCol < flyCol && !world.getLocation(currentRow, currentCol + 1).hasPredator()) {
                    spiderPositions.add(world.getLocation(currentRow, currentCol + 1));
                }
            } else { // currentCol == flyCol
                if (currentRow > flyRow && !world.getLocation(currentRow - 1, currentCol).hasPredator()) {
                    spiderPositions.add(world.getLocation(currentRow - 1, currentCol));
                } else if (currentRow < flyRow && !world.getLocation(currentRow + 1, currentCol).hasPredator()) {
                    spiderPositions.add(world.getLocation(currentRow + 1, currentCol));
                }
            }
        } 
        // Check if spider can move towards the fly diagonally
        else {
            // Move vertically towards the fly
            if (currentRow > flyRow && !world.getLocation(currentRow - 1, currentCol).hasPredator()) {
                spiderPositions.add(world.getLocation(currentRow - 1, currentCol));
            } else if (currentRow < flyRow && !world.getLocation(currentRow + 1, currentCol).hasPredator()) {
                spiderPositions.add(world.getLocation(currentRow + 1, currentCol));
            }
            
            // Move horizontally towards the fly
            if (currentCol > flyCol && !world.getLocation(currentRow, currentCol - 1).hasPredator()) {
                spiderPositions.add(world.getLocation(currentRow, currentCol - 1));
            } else if (currentCol < flyCol && !world.getLocation(currentRow, currentCol + 1).hasPredator()) {
                spiderPositions.add(world.getLocation(currentRow, currentCol + 1));
            }
        }
    
        // Convert ArrayList to array
        GridLocation[] moves = new GridLocation[spiderPositions.size()];
        for (int i = 0; i < moves.length; i++) {
            moves[i] = spiderPositions.get(i);
        }
    
        return moves;
    }
    

    public void update() {
        GridLocation[] legalMoves = generateLegalMoves();
        if (legalMoves.length > 0) {
            Random random = new Random();
            int ranIdx = random.nextInt(legalMoves.length);
            GridLocation newLocation = legalMoves[ranIdx];
            location.removeSpider();
            newLocation.setSpider(this);
            location = newLocation;
        } else {
            System.out.println("No legal moves");
        }
    }

    public boolean eatsFly() {
        GridLocation flyLocation = world.getFlyLocation();
        int spiderRow = location.getRow();
        int spiderCol = location.getCol();
        int flyRow = flyLocation.getRow();
        int flyCol = flyLocation.getCol();
        return spiderRow == flyRow && spiderCol == flyCol;
    }
}


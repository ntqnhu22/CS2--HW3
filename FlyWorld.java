import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FlyWorld {
    protected int numRows;
    protected int numCols;
    protected GridLocation[][] world;
    protected GridLocation start;
    protected GridLocation goal;
    protected Fly mosca;
    protected Frog[] frogs;
    protected Spider[] spiders;

    public FlyWorld(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scan = new Scanner(file);
            numRows = scan.nextInt();
            numCols = scan.nextInt();
            world = new GridLocation[numRows][numCols];
            frogs = new Frog[4];
            spiders = new Spider[4];
            int countFrogs = 0;
            int countSpiders = 0;
            for (int row = 0; row < numRows; row++) {
                String line = scan.next();
                for (int column = 0; column < numCols; column++) {
                    world[row][column] = new GridLocation(row, column);
                    char symbol = line.charAt(column);
                    if (symbol == 's') {
                        world[row][column].setBackgroundColor(Color.GREEN);
                        start = world[row][column];
                        mosca = new Fly(start, this);
                    } else if (symbol == 'h') {
                        world[row][column].setBackgroundColor(Color.RED);
                        goal = world[row][column];
                    } else if (symbol == 'f') {
                        if (countFrogs == frogs.length) {
                            resizeFrogsArray();
                        }
                        frogs[countFrogs++] = new Frog(world[row][column], this);
                    } else if (symbol == 'a') {
                        if (countSpiders == spiders.length) {
                            resizeSpidersArray();
                        }
                        spiders[countSpiders++] = new Spider(world[row][column], this);
                    }
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            e.printStackTrace();
            // Handle gracefully instead of continuing
            System.exit(1);
        }

        System.out.println("numRows: " + this.numRows + "   numCols: " + this.numCols);
        System.out.println("start: " + this.start + "   goal: " + this.goal);
        System.out.println("Mosca: " + this.mosca.toString());
    }

    private void resizeFrogsArray() {
        Frog[] newArray = new Frog[frogs.length * 2];
        for (int i = 0; i < frogs.length; i++) {
            newArray[i] = frogs[i];
        }
        frogs = newArray;
    }

    private void resizeSpidersArray() {
        Spider[] newArraySpider = new Spider[spiders.length * 2];
        for (int i = 0; i < spiders.length; i++) {
            newArraySpider[i] = spiders[i];
        }
        spiders = newArraySpider;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public boolean isValidLoc(int r, int c) {
        return r >= 0 && r < numRows && c >= 0 && c < numCols;
    }

    public GridLocation getLocation(int r, int c) {
        return world[r][c];
    }

    public GridLocation getFlyLocation() {
        return mosca.getLocation();
    }

    public int moveFly(int direction) {
        mosca.update(direction);
        GridLocation flyLocation = mosca.getLocation();
        if (flyLocation.equals(goal)) {
            return FlyWorldGUI.ATHOME;
        } else {
            if (movePredators()) {
                return FlyWorldGUI.EATEN;
            }
            return FlyWorldGUI.NOACTION;
        }
    }

    public boolean movePredators() {
        boolean flyGotEaten = false;

        for (int i = 0; i < frogs.length; i++) {
            if (frogs[i] != null) {
                frogs[i].update();
                if (frogs[i].eatsFly()) {
                    flyGotEaten = true;
                    break;
                }
            }
        }

        if (!flyGotEaten) {
            for (int i = 0; i < spiders.length; i++) {
                if (spiders[i] != null) {
                    spiders[i].update();
                    if (spiders[i].eatsFly()) {
                        flyGotEaten = true;
                        break;
                    }
                }
            }
        }

        return flyGotEaten;
   }
   
}


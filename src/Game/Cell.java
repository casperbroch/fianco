package Game;

public class Cell {
    private Stone stone;

    // Default constructor: Cell is initially empty
    public Cell() {
        this.stone = null;
    }

    // Copy constructor: Creates a new Cell by copying the provided Cell
    public Cell(Cell cell) {
        if (cell.hasStone()) {
            // Make a new Stone object by copying the stone from the given cell
            this.stone = new Stone(cell.getStone());
        } else {
            this.stone = null;
        }
    }

    public boolean hasStone() {
        return stone != null;
    }

    public Stone getStone() {
        return this.stone;
    }

    public void placeStone(Stone stone) {
        this.stone = stone;
    }

    public Stone removeStone() {
        Stone temp = stone;
        stone = null;
        return temp;
    }

    @Override
    public String toString() {
        if (hasStone()) {
            return stone.getColour(); // Assuming Stone has a method getColour()
        } else {
            return ".";
        }
    }
}

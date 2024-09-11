package Game;
public class Cell {
    private Stone stone;

    public Cell() {
        this.stone = null; // Cell is initially empty
    }

    public Cell(Cell cell) {
        this();
        cell.stone = stone;
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
            return stone.getCol();
        } else {
            return ".";
        }
    }
}

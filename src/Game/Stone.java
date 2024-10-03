package Game;
public class Stone {

    private final String col;

    public Stone(String col) {
        this.col = col;
    }

    public Stone(Stone other) {
        this.col = other.col;
    }

    public String getColour() {
        return this.col;
    }
    
    @Override
    public String toString() {
        return "Stone{colour=" + col + "'}'";
    }

    
}

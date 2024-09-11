package Game;
public class Stone {

    private final String col;

    public Stone(String col) {
        this.col = col;
    }

    public String getCol() {
        return this.col;
    }
    
    @Override
    public String toString() {
        return "Stone{colour=" + col + "'}'";
    }
}

package Game;

import Utils.GameUtils;

public class Game {
    private Cell[][] board;
    private int size;
    private GameUtils gameUtils;

    public Game(int size) {
        this.size = size;
        board = new Cell[size][size];
        gameUtils = new GameUtils();
        initializeBoard();
        initializeStones();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    private void initializeStones() {
        // Place Back pieces of black
        for (int i = 0; i < size; i++) {
            Stone stone = new Stone("BLACK");
            board[0][i].placeStone(stone);
        }

        // Place Back pieces of white
        for (int i = 0; i < size; i++) {
            Stone stone = new Stone("WHITE");
            board[size-1][i].placeStone(stone);
        }

        String[] diagW = {"b2", "c3", "d4", "f4", "g3", "h2"};
        String[] diagB = {"b8", "c7", "d6", "f6", "g7", "h8"};
        for (int i=0; i<6; i++) {
            board[gameUtils.translatePosition(diagW[i])[1]][gameUtils.translatePosition(diagW[i])[0]].placeStone(new Stone("WHITE"));
            board[gameUtils.translatePosition(diagB[i])[1]][gameUtils.translatePosition(diagB[i])[0]].placeStone(new Stone("BLACK"));
        }
    }


    public Cell[][] getBoard() {
        return board;
    }

    public int getSize() {
        return size;
    }
}

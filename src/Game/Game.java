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
            board[size-gameUtils.translatePosition(diagW[i])[1]][gameUtils.translatePosition(diagW[i])[0]].placeStone(new Stone("WHITE"));
            board[size-gameUtils.translatePosition(diagB[i])[1]][gameUtils.translatePosition(diagB[i])[0]].placeStone(new Stone("BLACK"));
        }
    }


    public void placeStone(int x, int y, Stone stone) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            board[x][y].placeStone(stone);
        } else {
            System.out.println("Invalid coordinates.");
        }
    }

    public void removeStone(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            board[x][y].removeStone();
        } else {
            System.out.println("Invalid coordinates.");
        }
    }

    public boolean moveStone(String Colour, String from, String to) {
        int[] fromInt = gameUtils.translatePosition(from);
        int[] toInt = gameUtils.translatePosition(to);

        return gameUtils.moveStone(board, Colour, fromInt[0], fromInt[1], toInt[0], toInt[1], size, false);
    }

    public boolean gameOver() {
        for (int i = 0; i < size; i++) {
            if (board[0][i].hasStone() && board[0][i].getStone().getCol().equals("WHITE")) {
                System.out.println("WHITE WON!");
                return true;
            }
        }

        for (int i = 0; i < size; i++) {
            if (board[size-1][i].hasStone() && board[size-1][i].getStone().getCol().equals("BLACK")) {
                System.out.println("BLACK WON!");
                return true;
            }
        }

        return false;
    }

    public Cell[][] getBoard() {
        return board;
    }
}

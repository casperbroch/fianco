package Game;

import Utils.GameUtils;
import java.util.ArrayList;

public class Game {
    private int[][] board;
    private int size;
    private GameUtils gameUtils;
    private ArrayList<int[][]> boardHistory;  // History of board states for undo functionality

    public Game(int size) {
        this.size = size;
        board = new int[size][size];
        gameUtils = new GameUtils();
        boardHistory = new ArrayList<>();  // Initialize the history
        initializeBoard();
        initializeStones();
        saveBoard();  // Save the initial board state
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
    }

    private void initializeStones() {
        // Place Back pieces of black
        for (int i = 0; i < size; i++) {
            board[0][i] = 2;
        }

        // Place Back pieces of white
        for (int i = 0; i < size; i++) {
            board[size-1][i] = 1;
        }

        String[] diagW = {"b2", "c3", "d4", "f4", "g3", "h2"};
        String[] diagB = {"b8", "c7", "d6", "f6", "g7", "h8"};
        for (int i=0; i<6; i++) {
            board[gameUtils.translatePosition(diagW[i])[1]][gameUtils.translatePosition(diagW[i])[0]] = 1;
            board[gameUtils.translatePosition(diagB[i])[1]][gameUtils.translatePosition(diagB[i])[0]] = 2;
        }
    }

    // Save the current board state to the history
    public void saveBoard() {
        int[][] boardCopy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, size);
        }
        boardHistory.add(boardCopy);
    }

    // Undo the last move by reverting to the previous board state
    public boolean undo() {
        System.out.println("undoing move ... ");
        
        if (boardHistory.size() > 1) {
            boardHistory.remove(boardHistory.size() - 1);  // Remove the current board state
            board = boardHistory.get(boardHistory.size() - 1);  // Revert to the previous state
            return true;
        } else {
            System.out.println("No more moves to undo!");
            return false;
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public int getSize() {
        return size;
    }
}

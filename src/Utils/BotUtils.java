package Utils;

import Utils.GameUtils;
import java.util.ArrayList;
import java.util.List;

public class BotUtils {

    public int evalBoard(int[][] board, String colour) {
        GameUtils gameUtils = new GameUtils();

        if(colour.equals("WHITE")) {
            if(gameUtils.gameOver(board, 9)==1) {
                return 1000;
            } else if (gameUtils.gameOver(board, 9)==2) {
                return -1000;
            }
        } else if(colour.equals("BLACK")) {
            if(gameUtils.gameOver(board, 9)==1) {
                return -1000;
            } else if (gameUtils.gameOver(board, 9)==2) {
                return 1000;
            }
        }
        

        int whiteEval = 0;
        int blackEval = 0;

        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                int curr = board[i][j];
                if (curr == 1) {

                    // * One point for each position more forward
                    whiteEval=whiteEval+((board[0].length-i)*(board[0].length-i));
                    
                    
                } else if (curr == 2) {

                    // * One point for each position more forward
                    blackEval=blackEval+((i+1)*(i+1));

                }
            }
        }

        if (colour.equals("WHITE")) {
            return whiteEval-blackEval;
        } else {
            return blackEval-whiteEval;
        }
    }

    

    public int[][] cloneBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board.length];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {

                newBoard[row][col] = Integer.valueOf(board[row][col]); // Assuming a copy constructor exists in Cell class

            }
        }
        return newBoard;
    }

    public List<int[]> getAllPossibleMoves(int player, int[][] board) {
        GameUtils gameUtils = new GameUtils();
        List<int[]> moves = new ArrayList<>();
        // Iterate over the board and add valid moves for the given player
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                int cell = board[y][x];
                if (cell == player) {
                    List<int[]> validDestinations = gameUtils.getValidMoves(x, y, board, 9, false);
                    for (int[] destination : validDestinations) {
                        moves.add(destination);
                    }
                }
            }
        }

        // Check if any attack moves exist in the collected moves
        List<int[]> attackMoves = new ArrayList<>();
        for (int[] move : moves) {
            if (Math.abs(move[0] - move[2]) == 2) { // Check if the move is an attack
                attackMoves.add(move);
            }
        }

        // Return attack moves if any exist; otherwise, return all collected moves
        return attackMoves.isEmpty() ? moves : attackMoves;
    }

    public boolean isQuiet(int[][] board) {
        GameUtils gameUtils = new GameUtils();
    
        // Check if the game is over. A finished game is considered quiet.
        if (gameUtils.gameOver(board, 9) != 0) {
            return true; // Game is over, no more moves possible, so it's quiet.
        }
    
        // Check for any attack moves (captures) for both players (WHITE and BLACK).
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int curr = board[i][j];
                
                // Check valid moves for WHITE stones (1)
                if (curr == 1) {
                    List<int[]> whiteValidMoves = gameUtils.getValidMoves(j, i, board, 9, false);
                    for (int[] move : whiteValidMoves) {
                        // If WHITE has an attack move, the position is not quiet
                        if (Math.abs(move[0] - move[2]) == 2) {
                            return false; // Attack move found for WHITE
                        }
                    }
                }
    
                // Check valid moves for BLACK stones (2)
                else if (curr == 2) {
                    List<int[]> blackValidMoves = gameUtils.getValidMoves(j, i, board, 9, false);
                    for (int[] move : blackValidMoves) {
                        // If BLACK has an attack move, the position is not quiet
                        if (Math.abs(move[0] - move[2]) == 2) {
                            return false; // Attack move found for BLACK
                        }
                    }
                }
            }
        }
    
        // If no attack moves are found for either player, the board is quiet
        return true;
    }
    
    

    
}

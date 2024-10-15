package Bots;

import java.util.ArrayList;
import java.util.List;
import Utils.BotUtils;
import Utils.GameUtils;

import Game.Cell;
import Game.Game;

public class NegaMaxAB {

    BotUtils botUtils;
    GameUtils gameUtils;

    public NegaMaxAB() {
        botUtils = new BotUtils();
        gameUtils = new GameUtils();
    }

    public boolean makeNMMove(String player, Cell[][] board, int d) {
        int[] bestMove = null;
        int bestMoveValue = Integer.MIN_VALUE; // For negamax, we always maximize

        for (int[] move : getAllPossibleMoves(player, board)) {
            Cell[][] newBoard = cloneBoard(board);
            gameUtils.moveStone(newBoard, 9, player, move[0], move[1], move[2], move[3], false);

            int moveValue = -negamax(newBoard, d - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, player.equals("WHITE")? "BLACK" : "WHITE");
            //System.out.println(moveValue);
            if (moveValue > bestMoveValue) {
                bestMoveValue = moveValue;
                bestMove = move;
            }
        }

        // After finding the best move, make it on the actual game board
        if (bestMove != null) {
            gameUtils.moveStone(board, 9, player, bestMove[0], bestMove[1], bestMove[2], bestMove[3], true);
            return true;
        } else {
            return false;
        }
    }

    public int negamax(Cell[][] board, int depth, int alpha, int beta, String player) {
        if (depth == 0 || !(gameUtils.gameOver(board, 9) == 0)) {
            return botUtils.evalBoard(board, player); // Evaluate from the opponent's perspective
        }

        int maxEval = Integer.MIN_VALUE;
        for (int[] move : getAllPossibleMoves(player, board)) {
            Cell[][] newBoard = cloneBoard(board);
            gameUtils.moveStone(newBoard, 9, player, move[0], move[1], move[2], move[3], false);

            int eval = -negamax(newBoard, depth - 1, -beta, -alpha, player.equals("BLACK") ? "WHITE" : "BLACK");
            maxEval = Math.max(maxEval, eval);
            
            
            alpha = Math.max(alpha, eval);
            
            if (alpha >= beta) {
                break; // Beta cutoff
            }
        }

        return maxEval;
    }

    public List<int[]> getAllPossibleMoves(String player, Cell[][] board) {
        List<int[]> moves = new ArrayList<>();
        // Iterate over the board and add valid moves for the given player
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                Cell cell = board[y][x];
                if (cell.hasStone() && cell.getStone().getColour().equals(player)) {
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

    public Cell[][] cloneBoard(Cell[][] board) {
        Cell[][] newBoard = new Cell[board.length][board.length];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                newBoard[row][col] = new Cell(board[row][col]); // Assuming a copy constructor exists in Cell class
            }
        }
        return newBoard;
    }
}

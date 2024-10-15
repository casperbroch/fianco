package Bots;

import java.util.ArrayList;
import java.util.List;
import Utils.BotUtils;
import Utils.GameUtils;


import Game.Cell;
import Game.Game;

public class Minimax {

    BotUtils botUtils;
    GameUtils gameUtils;
    private int nodeCount; // To track the number of nodes discovered

    public Minimax() {
        botUtils = new BotUtils();
        gameUtils = new GameUtils();
        nodeCount = 0; // Initialize the node counter
    }

    public boolean makeMinimaxMove(String player, Cell[][] board, int d) {
        nodeCount = 0; // Reset node count at the beginning of a new move
        long startTime = System.nanoTime(); // Start timing
        Cell[][] currentBoard = cloneBoard(board);
        int bestMoveValue = player.equals("WHITE") ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = null;

        for (int[] move : getAllPossibleMoves(player, currentBoard)) {
            Cell[][] newBoard = cloneBoard(currentBoard);
            gameUtils.moveStone(newBoard, 9, player, move[0], move[1], move[2], move[3], false);

            int moveValue = minimax(newBoard, d-1, player.equals("BLACK"));
            if (player.equals("WHITE") && moveValue > bestMoveValue) {
                bestMoveValue = moveValue;
                bestMove = move;
            } else if (player.equals("BLACK") && moveValue < bestMoveValue) {
                bestMoveValue = moveValue;
                bestMove = move;
            }
        }

        // After finding the best move, make it on the actual game board
        if (bestMove != null) {
            gameUtils.moveStone(board, 9, player, bestMove[0], bestMove[1], bestMove[2], bestMove[3], true);
            long endTime = System.nanoTime(); // End timing
            double durationInSeconds = (endTime - startTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
            double nodesPerSecond = nodeCount / durationInSeconds; // Calculate nodes per second
            
            System.out.println("From: " + gameUtils.translateCoordinates(bestMove[0], bestMove[1]) + " To: " + gameUtils.translateCoordinates(bestMove[2], bestMove[3]));
            System.out.println("Nodes discovered in this move: " + nodeCount);
            System.out.println("Time taken: " + durationInSeconds + " seconds");
            System.out.println("Nodes per second: " + nodesPerSecond);
            System.out.println(" ");
            return true;
        } else {
            return false;
        }
    }

    public int minimax(Cell[][] board, int depth, boolean isMaximizing) {
        nodeCount++; // Increment the node counter

        // Base case: if the game is over or max depth is reached
        if (depth == 0 || !(gameUtils.gameOver(board, 9) == 0)) {
            return botUtils.evalBoard(board, "WHITE");
        }

        if (isMaximizing) {  // WHITE's turn (maximizing)
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : getAllPossibleMoves("WHITE", board)) {
                Cell[][] newBoard = cloneBoard(board);  // Clone the board for the new move
                gameUtils.moveStone(newBoard, 9, "WHITE", move[0], move[1], move[2], move[3], false);
                int eval = minimax(newBoard, depth - 1, false);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {  // BLACK's turn (minimizing)
            int minEval = Integer.MAX_VALUE;
            for (int[] move : getAllPossibleMoves("BLACK", board)) {
                Cell[][] newBoard = cloneBoard(board);  // Clone the board for the new move
                gameUtils.moveStone(newBoard, 9, "BLACK", move[0], move[1], move[2], move[3], false);
                int eval = minimax(newBoard, depth - 1, true);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    // The rest of your methods (getAllPossibleMoves, cloneBoard) remain the same


    

    public List<int[]> getAllPossibleMoves(String player, Cell[][] board) {
        List<int[]> moves = new ArrayList<>();
        // Iterate over the board and add valid moves for the given player
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                Cell cell = board[y][x];
                if (cell.hasStone() && cell.getStone().getColour().equals(player)) {
                    List<int[]> validDestinations = gameUtils.getValidMoves(x,y, board, 9, false);
                    for (int[] destination : validDestinations) {
                        //System.out.println("from: "+ destination[0] +" "+ destination[1]+" to: "+ destination[2]+" "+destination[3]);
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

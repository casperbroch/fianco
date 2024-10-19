package Bots;

import java.util.ArrayList;
import java.util.List;
import Utils.BotUtils;
import Utils.GameUtils;

public class NMABQS {

    BotUtils botUtils;
    GameUtils gameUtils;
    private long startTime;
    private int timeLimitMs; // Time limit in milliseconds
    private int nodesEvaluated; // Number of nodes evaluated
    private List<Integer> plys;

    public NMABQS(int timeLimitSeconds) {
        botUtils = new BotUtils();
        gameUtils = new GameUtils();
        this.timeLimitMs = timeLimitSeconds * 1000; // Convert seconds to milliseconds
        this.plys = new ArrayList<Integer>();
    }

    public int[] makeMove(String player, int[][] board) {
        int[] bestMove = null;
        int bestMoveValue = Integer.MIN_VALUE; // For negamax, we always maximize
        int playerint = player.equals("WHITE") ? 1 : 2;
        nodesEvaluated = 0; // Reset node count

        // Start iterative deepening
        int maxDepth = 1; // Initial depth
        startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeLimitMs) {
            int[] currentBestMove = null;
            int currentBestMoveValue = Integer.MIN_VALUE;

            // Search to the current maxDepth
            List<int[]> possibleMoves = getAllPossibleMoves(playerint, board);
            if (possibleMoves.size() == 1 ) {
                bestMove = possibleMoves.get(0);
                bestMoveValue = 0;
                startTime = timeLimitMs;
            } else {
                for (int[] move : possibleMoves) {
                    int[][] newBoard = botUtils.cloneBoard(board);
                    gameUtils.moveStone(newBoard, 9, player, move[0], move[1], move[2], move[3], false);
    
                    int moveValue = -negamax(newBoard, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, player, player.equals("WHITE") ? "BLACK" : "WHITE");
    
                    if (moveValue > currentBestMoveValue) {
                        currentBestMoveValue = moveValue;
                        currentBestMove = move;
                    }
                }
            }


            if (System.currentTimeMillis() - startTime < timeLimitMs) {
                bestMove = currentBestMove; // Update the best move if within time
                bestMoveValue = currentBestMoveValue;
                System.out.println("Depth " + maxDepth + ": Best move value = " + bestMoveValue);
            }

            maxDepth++; // Increase depth
        }

        // Print statistics
        long totalTime = System.currentTimeMillis() - startTime;
        double nodesPerSecond = (double) nodesEvaluated / (totalTime*1000);
        if (maxDepth-2>4 && maxDepth-2 < 25) {
            plys.add(maxDepth-2);

        }
        System.out.println("Search completed.");
        System.out.println("Average ply in this game: "+averagePly());
        System.out.println("Nodes evaluated: " + nodesEvaluated);
        System.out.println("Nodes per second: " + nodesPerSecond+"million/s");

        // After finding the best move, make it on the actual game board
        if (bestMove != null) {
            gameUtils.moveStone(board, 9, player, bestMove[0], bestMove[1], bestMove[2], bestMove[3], true);
            System.out.println("Playing: "+gameUtils.translateCoordinates(bestMove[0], bestMove[1])+" to: "+gameUtils.translateCoordinates(bestMove[2], bestMove[3]));
            return bestMove;
        } else {
            return null;
        }
    }

    public int negamax(int[][] board, int depth, int alpha, int beta, String player, String simulator) {
        nodesEvaluated++;
        if (depth == 0 || !(gameUtils.gameOver(board, 9) == 0)) {
            return player.equals(simulator) ? botUtils.evalBoard(board, player) : -botUtils.evalBoard(board, player);
        }
    
        int simint = simulator.equals("WHITE") ? 1 : 2;
        int maxEval = Integer.MIN_VALUE;
    
        for (int[] move : getAllPossibleMoves(simint, board)) {
            int[][] newBoard = botUtils.cloneBoard(board);
            gameUtils.moveStone(newBoard, 9, simulator, move[0], move[1], move[2], move[3], false);
    
            int eval = -negamax(newBoard, depth - 1, -beta, -alpha, player, simulator.equals("BLACK") ? "WHITE" : "BLACK");
            maxEval = Math.max(maxEval, eval);
    
            alpha = Math.max(alpha, eval);
    
            if (alpha >= beta) {
                break; // Beta cutoff
            }
    
            // Check if the time limit has been exceeded
            if (System.currentTimeMillis() - startTime >= timeLimitMs) {
                break;
            }
        }
    
        return maxEval;
    }

    public int quiescence(int[][] board, int alpha, int beta, String player, String simulator) {
        if (botUtils.isQuiet(board)) {
            nodesEvaluated++;
            return player.equals(simulator) ? botUtils.evalBoard(board, player) : -botUtils.evalBoard(board, player);
        }
        nodesEvaluated++;


        int simint = simulator.equals("WHITE") ? 1 : 2;
        int maxEval = Integer.MIN_VALUE;
    
        // Get noisy moves (captures or tactical moves)
        List<int[]> noisyMoves = getAllPossibleMoves(simint, board);
    
        for (int[] move : noisyMoves) {
            int[][] newBoard = botUtils.cloneBoard(board);
            gameUtils.moveStone(newBoard, 9, simulator, move[0], move[1], move[2], move[3], false);
    
            int eval = -quiescence(newBoard, -beta, -alpha, player, simulator.equals("BLACK") ? "WHITE" : "BLACK");
            maxEval = Math.max(maxEval, eval);
    
            alpha = Math.max(alpha, eval);
    
            if (alpha >= beta) {
                break; // Beta cutoff
            }
    
            // Check if the time limit has been exceeded
            if (System.currentTimeMillis() - startTime >= timeLimitMs) {
                break;
            }
        }
    
        return maxEval;
    }
    

    public List<int[]> getAllPossibleMoves(int player, int[][] board) {
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

    public double averagePly() {
        double average = 0;
        for (int ply : plys) {
            average += ply;
        }
        average = average / plys.size();
        return average;
    }
}

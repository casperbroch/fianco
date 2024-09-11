package Utils;

import Game.Cell;
import Game.Stone;

public class GameUtils {

    public int[] translatePosition(String pos) {
        try {
            // Extract the letter and number from the input string
            char letter = pos.charAt(0);
            int number = Integer.parseInt(pos.substring(1));
            
            // Translate letter to x-coordinate (0-based index)
            int x = letter - 'a';
            
            // Translate number to y-coordinate (0-based index)
            int y = number;
            
            // Return the coordinates as an array [x, y]
            return new int[]{x, y};

        } catch (Exception e) {
            return new int[]{-1, -1};
        }
    }

    public void printBoard(Cell[][] board, int size) {    
        // Print the board with y-axis labels and board content
        for (int i = 0; i < size; i++) {
            // Print the left y-axis label
            System.out.print(size-i + "  ");
            
            // Print the row of the board
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j].toString().charAt(0) + " ");
            }

            System.out.println();
        }
        
        // Print bottom x-axis labels
        System.out.print("   "); // Initial space for the y-axis labels
        for (int i = 0; i < size; i++) {
            System.out.print((char) ('a' + i) + " ");
        }
        System.out.println();
    }

    public boolean isValidMove(Cell[][] board, String playerColour, int fromX, int fromY, int toX, int toY, int size, boolean DEBUG) {
        // Define constants for colours
        final String WHITE = "WHITE";
        final String BLACK = "BLACK";

        // Check if the coordinates are within bounds
        if (!isInBounds(fromX, fromY, size) || !isInBounds(toX, toY, size)) {
            if (DEBUG) System.out.println("Invalid coordinates.");
            return false;
        }

        Cell sourceCell = board[fromY][fromX];
        Cell destinationCell = board[toY][toX];

        // Check if there is a stone in the source cell
        if (!sourceCell.hasStone()) {
            if (DEBUG) System.out.println("No stone to move in the source cell.");
            return false;
        }

        String stoneColour = sourceCell.getStone().getCol();

        // Check if the stone belongs to the player
        if (!stoneColour.equals(playerColour)) {
            if (DEBUG) System.out.println("You cannot move your opponent's stone.");
            return false;
        }

        // Check if the destination cell is free
        if (destinationCell.hasStone()) {
            if (DEBUG) System.out.println("You cannot move onto a stone.");
            return false;
        }

        // Process moves based on the stone colour
        if (stoneColour.equals(WHITE)) {
            return processWhiteMove(board, fromX, fromY, toX, toY, DEBUG);
        } else if (stoneColour.equals(BLACK)) {
            return processBlackMove(board, fromX, fromY, toX, toY, DEBUG);
        }

        // If we reach here, something went wrong
        return false;
    }

    // Helper function to check if coordinates are within bounds
    private boolean isInBounds(int x, int y, int size) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    // Process the move logic for white stones
    private boolean processWhiteMove(Cell[][] board, int fromX, int fromY, int toX, int toY, boolean DEBUG) {
        // Normal forward move
        if (fromX == toX && fromY == toY + 1) {
            if (DEBUG) System.out.println("Normal move forward.");
            return true;
        }

        // Normal side moves
        if ((fromX == toX + 1 || fromX == toX - 1) && fromY == toY) {
            if (DEBUG) System.out.println("Normal move sideways.");
            return true;
        }

        // Attack moves
        if (fromX == toX + 2 && fromY == toY + 2) {
            return checkAttackMove(board, fromX - 1, fromY - 1, "BLACK", DEBUG, "Attack move left");
        }
        if (fromX == toX - 2 && fromY == toY + 2) {
            return checkAttackMove(board, fromX + 1, fromY - 1, "BLACK", DEBUG, "Attack move right");
        }

        if (DEBUG) System.out.println("This move isn't legal for WHITE.");
        return false;
    }

    // Process the move logic for black stones
    private boolean processBlackMove(Cell[][] board, int fromX, int fromY, int toX, int toY, boolean DEBUG) {
        // Normal forward move
        if (fromX == toX && fromY == toY - 1) {
            if (DEBUG) System.out.println("Normal move forward.");
            return true;
        }

        // Normal side moves
        if ((fromX == toX + 1 || fromX == toX - 1) && fromY == toY) {
            if (DEBUG) System.out.println("Normal move sideways.");
            return true;
        }

        // Attack moves
        if (fromX == toX + 2 && fromY == toY - 2) {
            return checkAttackMove(board, fromX - 1, fromY + 1, "WHITE", DEBUG, "Attack move right");
        }
        if (fromX == toX - 2 && fromY == toY - 2) {
            return checkAttackMove(board, fromX + 1, fromY + 1, "WHITE", DEBUG, "Attack move left");
        }

        if (DEBUG) System.out.println("This move isn't legal for BLACK.");
        return false;
    }

    // Helper function to check if an attack move is valid
    private boolean checkAttackMove(Cell[][] board, int attackX, int attackY, String opponentColour, boolean DEBUG, String message) {
        Cell attackedCell = board[attackY][attackX];

        if (attackedCell.hasStone() && attackedCell.getStone().getCol().equals(opponentColour)) {
            if (DEBUG) System.out.println(message);
            return true;
        }

        if (DEBUG) System.out.println("This attack move isn't legal.");
        return false;
    }

    
}

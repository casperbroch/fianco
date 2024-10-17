package Utils;

import java.util.ArrayList;
import java.util.List;

public class GameUtils {


        // Method with String inputs
    public boolean moveStone(int[][] board, int size, String Colour, String from, String to, boolean DEBUG) {
        int[] fromInt = translatePosition(from);
        int fromX = fromInt[0];
        int fromY = fromInt[1];

        int[] toInt = translatePosition(to);
        int toX = toInt[0];
        int toY = toInt[1];
        


        return moveStone(board, size, Colour, fromX, fromY, toX, toY, DEBUG);
    }

    // Overloaded method with int inputs
    public boolean moveStone(int[][] board, int size, String Colour, int fromX, int fromY, int toX, int toY, boolean DEBUG) {
        // Check if the move is valid without modifying the board
        if (isValidMove(board, Colour, fromX, fromY, toX, toY, size, DEBUG)) {

            // If it's an attack move (check if the move is two steps diagonally)
            if (Math.abs(fromX - toX) == 2 && Math.abs(fromY - toY) == 2) {
                // Calculate the coordinates of the attacked stone
                int attackX = (fromX + toX) / 2;
                int attackY = (fromY + toY) / 2;

                // Remove the attacked stone
                board[attackY][attackX] = 0;
            }

        if (Colour.equals("WHITE")) {
            board[toY][toX] = 1;
        } else if(Colour.equals("BLACK")) {
            board[toY][toX] = 2;
        }
        board[fromY][fromX] = 0;

            return true;
        }

        return false;
    }

    

    public int gameOver(int[][] board, int size) {
        for (int i = 0; i < size; i++) {
            if (board[0][i]==1) {
                return 1;
            }
        }

        for (int i = 0; i < size; i++) {
            if (board[size-1][i]==2) {
                return 2;
            }
        }

        return 0;
    }

    public int[] translatePosition(String pos) {
        try {
            // Extract the letter and number from the input string
            char letter = pos.charAt(0);
            int number = Integer.parseInt(pos.substring(1));
            
            // Translate letter to x-coordinate (0-based index)
            int x = letter - 'a';
            
            // Translate number to y-coordinate (0-based index)
            int y = 9-number;
            
            // Return the coordinates as an array [x, y]
            return new int[]{x, y};

        } catch (Exception e) {
            return new int[]{-1, -1};
        }
    }

    public String translateCoordinates(int x, int y) {
        try {
            // Translate x-coordinate (0-based index) to letter
            char letter = (char) ('a' + x);
            
            // Translate y-coordinate (0-based index) to number
            int number = 9 - y;
            
            // Return the position as a string "letter + number"
            return String.valueOf(letter) + number;
    
        } catch (Exception e) {
            return null;  // Return null in case of an error
        }
    }
    

    public void printBoard(int[][] board, int size) {    
        // Print the board with y-axis labels and board content
        for (int i = 0; i < size; i++) {
            // Print the left y-axis label
            System.out.print(size-i + "  ");
            
            // Print the row of the board
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 1) {
                    System.out.print("W ");
                } else if (board[i][j] == 2) {
                    System.out.print("B ");
                } else {
                    System.out.print(". ");
                }
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

    public boolean isValidMove(int[][] board, String playerColour, int fromX, int fromY, int toX, int toY, int size, boolean DEBUG) {
        // Define constants for colours
        final String WHITE = "WHITE";
        final String BLACK = "BLACK";

        // Check if the coordinates are within bounds
        if (!isInBounds(fromX, fromY, size) || !isInBounds(toX, toY, size)) {
            if (DEBUG) System.out.println("Invalid coordinates.");
            return false;
        }

        int sourceCell = board[fromY][fromX];
        int destinationCell = board[toY][toX];

        // Check if there is a stone in the source cell
        if (sourceCell == 0) {
            if (DEBUG) System.out.println("No stone to move in the source cell.");
            return false;
        }

        String stoneColour = null;

        if (sourceCell == 1) {
            stoneColour = "WHITE";
        } else {
            stoneColour = "BLACK";
        }


        // Check if the stone belongs to the player
        if (!stoneColour.equals(playerColour)) {
            if (DEBUG) System.out.println("You cannot move your opponent's stone.");
            return false;
        }

        // Check if the destination cell is free
        if (destinationCell != 0) {
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
    private boolean processWhiteMove(int[][] board, int fromX, int fromY, int toX, int toY, boolean DEBUG) {
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
            return checkAttackMove(board, fromX - 1, fromY - 1, 2, DEBUG, "Attack move left");
        }
        if (fromX == toX - 2 && fromY == toY + 2) {
            return checkAttackMove(board, fromX + 1, fromY - 1, 2, DEBUG, "Attack move right");
        }

        if (DEBUG) System.out.println("This move isn't legal for WHITE.");
        return false;
    }

    // Process the move logic for black stones
    private boolean processBlackMove(int[][] board, int fromX, int fromY, int toX, int toY, boolean DEBUG) {
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
            return checkAttackMove(board, fromX - 1, fromY + 1, 1, DEBUG, "Attack move right");
        }
        if (fromX == toX - 2 && fromY == toY - 2) {
            return checkAttackMove(board, fromX + 1, fromY + 1, 1, DEBUG, "Attack move left");
        }

        if (DEBUG) System.out.println("This move isn't legal for BLACK.");
        return false;
    }

    // Helper function to check if an attack move is valid
    private boolean checkAttackMove(int[][] board, int attackX, int attackY, int opponent, boolean DEBUG, String message) {
        int attackedCell = board[attackY][attackX];

        if (attackedCell == opponent) {
            if (DEBUG) System.out.println(message);
            return true;
        }

        if (DEBUG) System.out.println("This attack move isn't legal.");
        return false;
    }




    public List<int[]> getValidMoves(int fromX, int fromY, int[][] board, int size, boolean DEBUG) {
        List<int[]> validMoves = new ArrayList<>();
    
        // Get the coordinates of the current cell
        int sourceCell = board[fromY][fromX];
        
    
        // Check if the cell contains a stone
        if (sourceCell == 0) {
            if (DEBUG) System.out.println("No stone in the given cell.");
            return validMoves;
        }
        
        // Directions for white stones
        if (sourceCell == 1) {
            int[][] directionsW = {
                {-1, 0},  // Up 
                {0, 1},  // Right
                {0, -1},  // Left
                {-2, 2}, // Attack Right
                {-2, -2}   // Attack Left
            };
    
            for (int[] move : directionsW) {
                int toY = fromY + move[0];
                int toX = fromX + move[1];

                if (isValidMove(board, "WHITE", fromX, fromY, toX, toY, size, DEBUG)) {
                    // Add {fromX, fromY, toX, toY} to the valid moves list
                    validMoves.add(new int[]{fromX, fromY, toX, toY});
                }
            }
        }
    
        // Directions for black stones
        if (sourceCell == 2) {
            int[][] directionsB = {
                {1, 0},  // Up 
                {0, -1},   // Right
                {0, 1},  // Left
                {2, -2},  // Attack Right
                {2, 2}  // Attack Left
            };
    
            for (int[] move : directionsB) {
                int toY = fromY + move[0];
                int toX = fromX + move[1];
    
                if (isValidMove(board, "BLACK", fromX, fromY, toX, toY, size, DEBUG)) {
                    // Add {fromX, fromY, toX, toY} to the valid moves list
                    validMoves.add(new int[]{fromX, fromY, toX, toY});
                }
            }
        }
    
        return validMoves;
    }
    


    
}

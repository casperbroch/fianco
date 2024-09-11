package Utils;

import Game.Cell;

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

    public boolean moveStone(Cell[][] board, String Colour, int fromX, int fromY, int toX, int toY, int size, boolean DEBUG) {

        fromY = size - fromY;
        toY = size - toY;

        // * Check if the given positions are within bounds of the game
        if (fromX >= 0 && fromX < size && fromY >= 0 && fromY < size &&
            toX >= 0 && toX < size && toY >= 0 && toY < size) {

            Cell sourceCell = board[fromY][fromX];
            Cell destinationCell = board[toY][toX];

            // * Check if the source cell has a stone
            if (sourceCell.hasStone()) {
                String colour = sourceCell.getStone().getCol();

                // * Check if user is trying to move the correct stone
                if (!colour.equals(Colour)) {
                    System.out.println("You cannot move your oppononent's stone");
                    return false;
                }

                // * Check if the user is going to a free cell
                if (destinationCell.hasStone()) {
                    System.out.println("You cannot move on a stone.");
                    return false;
                }

                // ! Normal moves and attack moves

                if (colour.equals("WHITE")) {
                    if (fromX == toX && fromY == toY + 1) {
                        System.out.println("Normal move forward");

                    } else if (fromX == toX + 1 && fromY == toY) {
                        System.out.println("Normal move left");

                    } else if (fromX == toX - 1 && fromY == toY) {
                        System.out.println("Normal move right");

                    } else if (fromX == toX + 2 && fromY == toY + 2) {
                        Cell attackedCell = board[fromY - 1][fromX - 1];
                        if (attackedCell.hasStone() && attackedCell.getStone().getCol().equals("BLACK")) {
                            attackedCell.removeStone();
                            System.out.println("Attack move left");
                        } else {
                            System.out.println("This attack move isn't legal.");
                            return false;
                        }                    
                    } else if (fromX == toX - 2 && fromY == toY + 2) {
                        Cell attackedCell = board[fromY - 1][fromX + 1];
                        System.out.println(fromX);
                        System.out.println(fromY);
                        if (attackedCell.hasStone() && attackedCell.getStone().getCol().equals("BLACK")) {
                            attackedCell.removeStone();
                            System.out.println("Attack move right");
                        } else {
                            System.out.println("This attack move isn't legal.");
                            return false;
                        }
                    } else {
                        System.out.println("This normal move isn't legal.");
                        return false;
                    }

                } else if (colour.equals("BLACK")) {
                    if (fromX == toX && fromY == toY - 1) {
                        System.out.println("Normal move forward");
                    } else if (fromX == toX + 1 && fromY == toY) {
                        System.out.println("Normal move left");
                    } else if (fromX == toX - 1 && fromY == toY) {
                        System.out.println("Normal move right");
                    } else if (fromX == toX + 2 && fromY == toY - 2) {
                        Cell attackedCell = board[fromY + 1][fromX - 1];
                        if (attackedCell.hasStone() && attackedCell.getStone().getCol().equals("WHITE")) {
                            attackedCell.removeStone();
                            System.out.println("Attack move right");
                        } else {
                            System.out.println("This attack move isn't legal.");
                            return false;
                        }                
                    } else if (fromX == toX - 2 && fromY == toY - 2) {
                        Cell attackedCell = board[fromY + 1][fromX + 1];
                        if (attackedCell.hasStone() && attackedCell.getStone().getCol().equals("WHITE")) {
                            attackedCell.removeStone();
                            System.out.println("Attack move right");
                        } else {
                            System.out.println("This attack move isn't legal.");
                            return false;
                        }  
                    } else {
                        System.out.println("This normal move isn't legal.");
                        return false;
                    }
                }
                    
                
                // If the move succeeds all above checks, we can play the move on the board
                destinationCell.placeStone(sourceCell.removeStone());     // Place the stone in destination cell
                return true;

            } else {
                System.out.println("No stone to move in the source cell.");
                return false; // Move failed
            }
        } else {
            System.out.println("Invalid coordinates.");
            return false; // Move failed
        }
    }
    
}

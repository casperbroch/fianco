package Utils;

import Game.Cell;

public class BotUtils {


    public int evalBoard(Cell[][] board, String colour) {

        int whiteEval = 0;
        int blackEval = 0;

        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                Cell curr = board[i][j];
                if (curr.hasStone() && curr.getStone().getCol().equals("WHITE")) {

                    // * One point for each position more forward
                    whiteEval=whiteEval+(board[0].length-i);
                    
                } else if (curr.hasStone() && curr.getStone().getCol().equals("BLACK")) {

                    // * One point for each position more forward
                    blackEval=blackEval+i+1;

                }
            }
        }

        if (colour.equals("WHITE")) {
            return whiteEval-blackEval;
        } else {
            return blackEval-whiteEval;
        }
    }


    public boolean isValidMove(Cell[][] board, int fromX, int fromY, int toX, int toY, boolean DEBUG) {

        int size = 9;

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
                if (!colour.equals(colour)) {
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
                        //System.out.println("Normal move forward");

                    } else if (fromX == toX + 1 && fromY == toY) {
                        //System.out.println("Normal move left");

                    } else if (fromX == toX - 1 && fromY == toY) {
                        //System.out.println("Normal move right");

                    } else if (fromX == toX + 2 && fromY == toY + 2) {
                        Cell attackedCell = board[fromY - 1][fromX - 1];
                        if (attackedCell.hasStone() && attackedCell.getStone().getCol().equals("BLACK")) {
                            attackedCell.removeStone();
                            //System.out.println("Attack move left");
                        } else {
                            //System.out.println("This attack move isn't legal.");
                            return false;
                        }                    
                    } else if (fromX == toX - 2 && fromY == toY + 2) {
                        Cell attackedCell = board[fromY - 1][fromX + 1];
                        //System.out.println(fromX);
                        //System.out.println(fromY);
                        if (attackedCell.hasStone() && attackedCell.getStone().getCol().equals("BLACK")) {
                            attackedCell.removeStone();
                            //System.out.println("Attack move right");
                        } else {
                            //System.out.println("This attack move isn't legal.");
                            return false;
                        }
                    } else {
                        //System.out.println("This normal move isn't legal.");
                        return false;
                    }

                } else if (colour.equals("BLACK")) {
                    if (fromX == toX && fromY == toY - 1) {
                        //System.out.println("Normal move forward");
                    } else if (fromX == toX + 1 && fromY == toY) {
                        //System.out.println("Normal move left");
                    } else if (fromX == toX - 1 && fromY == toY) {
                        //System.out.println("Normal move right");
                    } else if (fromX == toX + 2 && fromY == toY - 2) {
                        Cell attackedCell = board[fromY + 1][fromX - 1];
                        if (attackedCell.hasStone() && attackedCell.getStone().getCol().equals("WHITE")) {
                            attackedCell.removeStone();
                            //System.out.println("Attack move right");
                        } else {
                            //System.out.println("This attack move isn't legal.");
                            return false;
                        }                
                    } else if (fromX == toX - 2 && fromY == toY - 2) {
                        Cell attackedCell = board[fromY + 1][fromX + 1];
                        if (attackedCell.hasStone() && attackedCell.getStone().getCol().equals("WHITE")) {
                            attackedCell.removeStone();
                            //System.out.println("Attack move right");
                        } else {
                            //System.out.println("This attack move isn't legal.");
                            return false;
                        }  
                    } else {
                        //System.out.println("This normal move isn't legal.");
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

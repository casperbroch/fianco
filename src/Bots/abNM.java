package Bots;

import java.util.ArrayList;
import java.util.List;
import Utils.BotUtils;

import Game.Cell;

public class abNM {

    BotUtils botUtils;

    public abNM() {
        botUtils = new BotUtils();

    }

    public int[] makeMove(Cell[][] board) {



        return new int[]{1, 1};
    }

    // public List<Cell[][]> generatePossibleBoardsForPlayer(Cell[][] board, String playerColour) {
    //     List<Cell[][]> possibleBoards = new ArrayList<>();
    //     int size = 9;

    //     // Iterate through all cells to find movable stones of the current player
    //     for (int row = 0; row < size; row++) {
    //         for (int col = 0; col < size; col++) {
    //             Cell currentCell = board[row][col];

    //             // Check if the current cell has a stone of the given color
    //             if (currentCell.hasStone() && currentCell.getStone().getColour().equals(playerColour)) {

    //                 // For each valid move (you'll need to determine possible moves, this is just a placeholder)
    //                 for (int moveRow = 0; moveRow < size; moveRow++) {
    //                     for (int moveCol = 0; moveCol < size; moveCol++) {
    //                         if (botUtils.isValidMove(board, row, col, moveRow, moveCol)) {

    //                             // Create a new board configuration (clone the current board)
    //                             //Cell[][] newBoard = cloneBoard(board);

    //                             // Perform the move (remove the stone from the original cell, place it in the new cell)
    //                             newBoard[moveRow][moveCol].setStone(newBoard[row][col].getStone()); // Move the stone
    //                             newBoard[row][col].removeStone(); // Remove the stone from the original position

    //                             // Add the new board configuration to the list
    //                             possibleBoards.add(newBoard);
    //                         }
    //                     }
    //                 }
    //             }
    //         }
    //     }

    //     return possibleBoards; // Return the list of all possible boards for the given player
    // }

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

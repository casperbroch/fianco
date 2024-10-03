package Utils;

import Game.Cell;
import Utils.GameUtils;

public class BotUtils {


    public int evalBoard(Cell[][] board, String colour) {
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
                Cell curr = board[i][j];
                if (curr.hasStone() && curr.getStone().getColour().equals("WHITE")) {

                    // * One point for each position more forward
                    whiteEval=whiteEval+(board[0].length-i);
                    
                } else if (curr.hasStone() && curr.getStone().getColour().equals("BLACK")) {

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

    
}

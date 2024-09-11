import java.util.Scanner;

import Bots.RandomBot;
import Bots.abNM;
import Utils.BotUtils;
import Utils.GameUtils;
import Game.Game;

public class Main {
    public static void main(String[] args) {

        Game game = new Game(9);

        Scanner scanner = new Scanner(System.in);
        boolean playing = true;
        BotUtils botUtils = new BotUtils();
        GameUtils gameUtils = new GameUtils();
        abNM abnm = new abNM();

        RandomBot randomBot = new RandomBot();


        gameUtils.printBoard(game.getBoard(), 9);

        
        while (playing) {

            gameUtils.printBoard(abnm.cloneBoard(game.getBoard()), 9);

            boolean succesW = false;
            boolean succesB = false;

            // ! WHITE's TURN
            while (!succesW) {
                System.out.print("WHITE TURN: Enter which stone you want to choose: ");
                String stoneUserW = scanner.nextLine();
                
                System.out.print("WHITE TURN: Enter where you want the stone to go: ");
                String cellUserW = scanner.nextLine();

                succesW = game.moveStone("WHITE", stoneUserW, cellUserW);
                
                if (succesW) {
                    System.out.println("Move successful.");
                } else {
                    System.out.println("Move failed, try again.");
                }

                //String stoneUserW = randomBot.makeMove();
                //String cellUserW = randomBot.makeMove();

                succesW = game.moveStone("WHITE", stoneUserW, cellUserW);
            }

            gameUtils.printBoard(game.getBoard(), 9);
            if (game.gameOver()) {
                return;
            }

            // ! BLACK's TURN
            while (!succesB) {
                //System.out.print("BLACK TURN: Enter which stone you want to choose: ");
                //String stoneUserB = scanner.nextLine();
                
                //System.out.print("BLACK TURN: Enter where you want the stone to go: ");
                //String cellUserB = scanner.nextLine();
    
                String stoneUserB = randomBot.makeMove();
                String cellUserB = randomBot.makeMove();
                

                succesB = game.moveStone("BLACK", stoneUserB, cellUserB);

                if (succesB) {
                    //System.out.println("Move successful.");
                } else {
                    //System.out.println("Move failed, try again.");
                }
            }

            gameUtils.printBoard(game.getBoard(), 9);
            if (game.gameOver()) {
                return;
            }

        }
        
       scanner.close();

    

    }
}

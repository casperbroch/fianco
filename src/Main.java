import java.util.Scanner;

import Bots.RandomBot;
import Bots.Minimax;
import Bots.NMABQS;
import Bots.NegaMaxAB;
import Utils.BotUtils;
import Utils.GameUtils;
import Game.Game;

public class Main {
    public static void main(String[] args) {

        Game game = new Game(9);

        Scanner scanner = new Scanner(System.in);
        boolean playing = true;
        GameUtils gameUtils = new GameUtils();
        BotUtils botUtils = new BotUtils();
        Minimax minimax = new Minimax();
        RandomBot randomBot = new RandomBot();
        NegaMaxAB negaMaxAB = new NegaMaxAB(1);
        NMABQS nmabqs = new NMABQS(1);

        // Ask who will play as WHITE and BLACK
        System.out.println("Who will play as WHITE? (Enter 'user', 'random', 'miniamx', or 'negamax')");
        String whitePlayer = scanner.nextLine().toLowerCase();

        System.out.println("Who will play as BLACK? (Enter 'user', 'random', 'minimax', or 'negamax')");
        String blackPlayer = scanner.nextLine().toLowerCase();

        gameUtils.printBoard(game.getBoard(), 9);
        System.out.println(botUtils.evalBoard(game.getBoard(), "WHITE"));

        while (playing) {
            boolean succesW = false;
            boolean succesB = false;

            // WHITE's TURN
            while (!succesW) {
                if (whitePlayer.equals("user")) {
                    // User's turn
                    System.out.print("WHITE TURN: Enter which stone you want to choose: ");
                    String stoneUserW = scanner.nextLine();
                    System.out.print("WHITE TURN: Enter where you want the stone to go: ");
                    String cellUserW = scanner.nextLine();
                    succesW = gameUtils.moveStone(game.getBoard(), game.getSize(), "WHITE", stoneUserW, cellUserW, true);
                } else if (whitePlayer.equals("random")) {
                    // Random bot's turn
                    String stoneUserW = randomBot.makeMove();
                    String cellUserW = randomBot.makeMove();
                    succesW = gameUtils.moveStone(game.getBoard(), game.getSize(), "WHITE", stoneUserW, cellUserW, false);


                //! ! ! ! ! ! ! ! !  ! ! ! !  ! !   ! ! ! !  !
                } else if (whitePlayer.equals("minimax")) {
                    // Minimax bot's turn
                    succesW = minimax.makeMove("WHITE", game.getBoard(),4);
                } else if (whitePlayer.equals("negamax")) {
                    // Minimax bot's turn
                    succesW = nmabqs.makeMove("WHITE", game.getBoard());
                } 



            }
            game.saveBoard();
            gameUtils.printBoard(game.getBoard(), game.getSize());
            System.out.println(botUtils.evalBoard(game.getBoard(), "WHITE"));
            if (!(gameUtils.gameOver(game.getBoard(), game.getSize()) == 0)) {
                if (gameUtils.gameOver(game.getBoard(), game.getSize()) == 1) {
                    System.out.println("WHITE WON!");
                } else {
                    System.out.println("BLACK WON!");
                }
                return;
            }

            // BLACK's TURN
            while (!succesB) {
                if (blackPlayer.equals("user")) {
                    // User's turn
                    System.out.print("BLACK TURN: Enter which stone you want to choose: ");
                    String stoneUserB = scanner.nextLine();
                    System.out.print("BLACK TURN: Enter where you want the stone to go: ");
                    String cellUserB = scanner.nextLine();
                    succesB = gameUtils.moveStone(game.getBoard(), game.getSize(), "BLACK", stoneUserB, cellUserB, true);
                } else if (blackPlayer.equals("random")) {
                    // Random bot's turn
                    String stoneUserB = randomBot.makeMove();
                    String cellUserB = randomBot.makeMove();
                    succesB = gameUtils.moveStone(game.getBoard(), game.getSize(), "BLACK", stoneUserB, cellUserB, true);
                

                //! ! ! ! ! !  ! ! ! !  ! ! !  ! ! ! !  ! ! ! ! 
                } else if (blackPlayer.equals("minimax")) {
                    // Minimax bot's turn
                    succesB = minimax.makeMove("BLACK", game.getBoard(), 4);

                } else if (blackPlayer.equals("negamax")) {
                    // Minimax bot's turn
                    succesB = negaMaxAB.makeMove("BLACK", game.getBoard());
                }



            }
            game.saveBoard();

            gameUtils.printBoard(game.getBoard(), game.getSize());
            game.undo();
            game.undo();
            gameUtils.printBoard(game.getBoard(), game.getSize());

            System.out.println(botUtils.evalBoard(game.getBoard(), "WHITE"));
            if (!(gameUtils.gameOver(game.getBoard(), game.getSize()) == 0)) {
                if (gameUtils.gameOver(game.getBoard(), game.getSize()) == 1) {
                    System.out.println("WHITE WON!");
                } else {
                    System.out.println("BLACK WON!");
                }
                return;
            }
        }

        scanner.close();
    }
}

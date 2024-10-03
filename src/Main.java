import java.util.Scanner;

import Bots.RandomBot;
import Bots.abNM;
import Utils.BotUtils;
import Utils.GameUtils;
import Game.Cell;
import Game.Game;

public class Main {
    public static void main(String[] args) {

        Game game = new Game(9);

        Scanner scanner = new Scanner(System.in);
        boolean playing = true;
        GameUtils gameUtils = new GameUtils();
        abNM abnm = new abNM();
        RandomBot randomBot = new RandomBot();

        // Ask who will play as WHITE and BLACK
        System.out.println("Who will play as WHITE? (Enter 'user', 'random', or 'negamax')");
        String whitePlayer = scanner.nextLine().toLowerCase();

        System.out.println("Who will play as BLACK? (Enter 'user', 'random', or 'negamax')");
        String blackPlayer = scanner.nextLine().toLowerCase();

        gameUtils.printBoard(game.getBoard(), 9);

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
                    succesW = gameUtils.moveStone(game.getBoard(), game.getSize(), "WHITE", stoneUserW, cellUserW, true);
                } else if (whitePlayer.equals("negamax")) {
                    // Minimax bot's turn
                    succesW = abnm.makeNegamaxMove("WHITE", game.getBoard(), 3);
                }
            }

            gameUtils.printBoard(game.getBoard(), game.getSize());
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
                } else if (blackPlayer.equals("negamax")) {
                    // Minimax bot's turn
                    succesB = abnm.makeNegamaxMove("BLACK", game.getBoard(), 3);
                }
            }

            gameUtils.printBoard(game.getBoard(), game.getSize());
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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Game.Cell;
import Game.Game;
import Bots.abNM;
import Utils.GameUtils;

public class GameGUI extends JFrame {
    private JPanel boardPanel;
    private JTextArea moveLog;
    private JLabel whiteTimerLabel;
    private JLabel blackTimerLabel;
    private static Timer whiteTimer;
    private static Timer blackTimer;
    private int whiteTimeRemaining = 600; // 10 minutes in seconds
    private int blackTimeRemaining = 600;
    private Game game;
    private int boardSize;
    private JButton[][] buttons;
    private JComboBox<String> whitePlayerChoice;
    private JComboBox<String> blackPlayerChoice;
    private abNM abnm;
    private GameUtils gameUtils;

    public GameGUI(Game game) {
        this.game = game;
        this.boardSize = game.getSize();
        this.buttons = new JButton[boardSize][boardSize];  // Store buttons to update their size
        this.abnm = new abNM();
        this.gameUtils = new GameUtils();

        // Set up the frame
        setTitle("Board Game GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(false);
        setLayout(new BorderLayout());

        // Set up the extra panel with timer labels, player choice, and start button
        JPanel extraPanel = new JPanel();
        extraPanel.setLayout(new BoxLayout(extraPanel, BoxLayout.Y_AXIS));  // Vertical layout
        extraPanel.setBorder(new EmptyBorder(10, 20, 10, 20));  // Add padding

        // Timer labels
        whiteTimerLabel = new JLabel("White Time: 10:00");
        blackTimerLabel = new JLabel("Black Time: 10:00");

        // Drop-down menu for player selection (Player or Bot)
        String[] playerOptions = {"Player", "Bot"};
        whitePlayerChoice = new JComboBox<>(playerOptions);
        blackPlayerChoice = new JComboBox<>(playerOptions);

        // Start Game button
        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Add components to the extra panel
        extraPanel.add(new JLabel("Black Player:"));
        extraPanel.add(blackPlayerChoice);
        extraPanel.add(Box.createVerticalStrut(180));  // Add spacing
        extraPanel.add(startGameButton);
        extraPanel.add(Box.createVerticalStrut(30));  // Space between button and timers
        extraPanel.add(whiteTimerLabel);
        extraPanel.add(blackTimerLabel);
        extraPanel.add(Box.createVerticalStrut(180));  // Add more spacing
        extraPanel.add(new JLabel("White Player:"));
        extraPanel.add(whitePlayerChoice);

        // Set up the board panel
        boardPanel = new JPanel(new GridLayout(boardSize, boardSize)) {
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                int length = Math.min(size.width, size.height);
                return new Dimension(length, length);
            }
        };
        initializeBoard();

        // Set up the move log
        moveLog = new JTextArea(10, 20);
        moveLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveLog);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Move Log"));

        // Add components to the frame
        add(extraPanel, BorderLayout.WEST);  // Add the extra panel on the left
        add(boardPanel, BorderLayout.CENTER);  // Center panel for the game board
        add(scrollPane, BorderLayout.EAST);  // Right panel for move log


        setVisible(true);
        initializeTimers();
    }

    // Initialize the board with buttons
    private void initializeBoard() {
        Cell[][] board = game.getBoard();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton button = new JButton();
                if (board[row][col].hasStone()) {
                    if (board[row][col].getStone().getColour().equals("BLACK")) {
                        button.setBackground(Color.BLACK);
                    } else if (board[row][col].getStone().getColour().equals("WHITE")) {
                        button.setBackground(Color.WHITE);
                    }
                } else {
                    button.setBackground(Color.GRAY);  // Empty cell color
                }

                buttons[row][col] = button;
                boardPanel.add(button);
            }
        }
    }

        // Initialize timers for both players
    private void initializeTimers() {
        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (whiteTimeRemaining > 0) {
                    whiteTimeRemaining--;
                    updateTimerLabel(whiteTimerLabel, whiteTimeRemaining);
                } else {
                    whiteTimer.stop();
                    logMove("White player ran out of time!");
                }
            }
        });

        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (blackTimeRemaining > 0) {
                    blackTimeRemaining--;
                    updateTimerLabel(blackTimerLabel, blackTimeRemaining);
                } else {
                    blackTimer.stop();
                    logMove("Black player ran out of time!");
                }
            }
        });
    }

    // Update the timer label
    private void updateTimerLabel(JLabel label, int timeRemaining) {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        label.setText(String.format("%s Time: %02d:%02d", 
            label == whiteTimerLabel ? "White" : "Black", minutes, seconds));
    }

// Start the game based on player selections
private void startGame() {
    String whitePlayer = (String) whitePlayerChoice.getSelectedItem();
    String blackPlayer = (String) blackPlayerChoice.getSelectedItem();
    logMove("Game started: White is " + whitePlayer + ", Black is " + blackPlayer);

    Timer gameTimer = new Timer(1000, new ActionListener() {
        boolean playing = true;
        boolean whiteTurn = true;  // Start with white's turn

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playing) {
                if (whiteTurn) {
                    // White's turn
                    whiteTimer.start();
                    abnm.makeNegamaxMove("WHITE", game.getBoard(),4);
                    whiteTimer.stop();
                    updateBoard();
                    logMove("WHITE made a move.");
                } else {
                    // Black's turn
                    abnm.makeNegamaxMove("BLACK", game.getBoard(), 4);
                    updateBoard();
                    logMove("BLACK made a move.");
                }

                // Check if the game is over
                int gameOverStatus = gameUtils.gameOver(game.getBoard(), game.getSize());
                if (gameOverStatus != 0) {
                    playing = false;
                    if (gameOverStatus == 1) {
                        logMove("WHITE WON!");
                    } else {
                        logMove("BLACK WON!");
                    }
                    ((Timer) e.getSource()).stop();  // Stop the timer when the game ends
                }

                whiteTurn = !whiteTurn;  // Switch turns between white and black
            }
        }
    });

    gameTimer.setInitialDelay(0);  // Start immediately
    gameTimer.start();  // Start the game loop with a timer
}


    // Log moves in the move log area
    private void logMove(String move) {
        moveLog.append(move + "\n");
    }

    // Update the board display
    public void updateBoard() {
        boardPanel.removeAll();
        initializeBoard();
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public static void main(String[] args) {
        Game game = new Game(9);  // Example game initialization
        new GameGUI(game);
    }
}
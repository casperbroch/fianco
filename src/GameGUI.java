import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Game.Game;
import Bots.Minimax;
import Bots.NMABQS;
import Bots.NegaMaxAB;
import Utils.GameUtils;

public class GameGUI extends JFrame {
    private JPanel boardPanel;
    private JTextArea moveLog;
    private static Timer whiteTimer;
    private static Timer blackTimer;
    private Game game;
    private int boardSize;
    private JButton[][] buttons;
    private JButton undoButton;
    private JComboBox<String> whitePlayerChoice;
    private JComboBox<String> blackPlayerChoice;
    private Minimax minimax;
    private NegaMaxAB negaMaxAB;
    private NMABQS nmabqs;
    private GameUtils gameUtils;
    // Variables to track the selected cells
    private int[] selectedCell1 = null;
    private int[] selectedCell2 = null;

    private boolean whiteTurn = true;

    public GameGUI(Game game) {
        this.game = game;
        this.boardSize = game.getSize();
        this.buttons = new JButton[boardSize][boardSize];  // Store buttons to update their size
        this.minimax = new Minimax();
        this.negaMaxAB = new NegaMaxAB(5);
        this.nmabqs = new NMABQS(10);
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

        
        // Drop-down menu for player selection (Player or Bot)
        String[] playerOptions = {"NMABQS", "NegaMax", "Player"};
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

        // Undo button
        undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.undo();  // Call the undo method on the game
                updateBoard();  // Update the board display after undo
                logMove("Last move undone.");  // Log the undo action
                if (whiteTurn) {
                    whiteTurn = false ;
                } else {whiteTurn = true;}
            }
        });

        // Add components to the extra panel
        extraPanel.add(new JLabel("Black Player:"));
        extraPanel.add(blackPlayerChoice);
        extraPanel.add(Box.createVerticalStrut(180));  // Add spacing
        extraPanel.add(startGameButton);
        extraPanel.add(Box.createVerticalStrut(30));  // Space between button and timers
        extraPanel.add(undoButton);  // Add the undo button
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
    }

    private void initializeBoard() {
        int[][] board = game.getBoard();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton button = new JButton();
                if (board[row][col] == 1) {
                    button.setBackground(Color.WHITE);
                } else if (board[row][col] == 2) {
                    button.setBackground(Color.BLACK);
                } else {
                    button.setBackground(Color.GRAY);
                }
    
                // Add action listener to handle button clicks
                final int currentRow = row;
                final int currentCol = col;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleCellClick(currentRow, currentCol);
                    }
                });
    
                buttons[row][col] = button;
                boardPanel.add(button);
            }
        }
    }

    // Method to handle button clicks
    private void handleCellClick(int row, int col) {
        if (selectedCell1 == null) {
            // First cell selected
            selectedCell1 = new int[]{row, col};
            logMove("First cell selected at: (" + row + ", " + col + ")");
        } else if (selectedCell2 == null) {
            // Second cell selected
            selectedCell2 = new int[]{row, col};
            logMove("Second cell selected at: (" + row + ", " + col + ")");
        }
    }

    // Method to perform the move
    private void makeMove(int[] fromCell, int[] toCell, String colour) {
        logMove("Attempting move from (" + fromCell[0] + ", " + fromCell[1] + ") to (" + toCell[0] + ", " + toCell[1] + ")");
        gameUtils.moveStone(game.getBoard(), boardSize, colour, fromCell[1], fromCell[0], toCell[1], toCell[0], true);
        selectedCell1 = null;
        selectedCell2 = null;
        updateBoard();
    }



    // Start the game based on player selections
    private void startGame() {
        String whitePlayer = (String) whitePlayerChoice.getSelectedItem();
        String blackPlayer = (String) blackPlayerChoice.getSelectedItem();
        logMove("Game started: White is " + whitePlayer + ", Black is " + blackPlayer);

        Timer gameTimer = new Timer(1000, new ActionListener() {
            boolean playing = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (playing) {
                    if (whiteTurn) {
                        if (whitePlayer.equals("NMABQS")) {
                            nmabqs.makeMove("WHITE", game.getBoard());

                            
                            updateBoard();
                            logMove("WHITE made a move.");
                            game.saveBoard();
                            whiteTurn = false;

                        } else if (whitePlayer.equals("NegaMax")) {
                            negaMaxAB.makeMove("WHITE", game.getBoard());
                            
                            updateBoard();
                            logMove("WHITE made a move.");
                            game.saveBoard();
                            whiteTurn = false;
                        } else if (whitePlayer.equals("Player")) {
                            if (selectedCell1 != null && selectedCell2 != null) {
                                makeMove(selectedCell1, selectedCell2, "WHITE");
                                
                                updateBoard();
                                logMove("WHITE made a move.");
                                game.saveBoard();
                                whiteTurn = false;
                            }
                        }


                    } else {
                        if (blackPlayer.equals("NMABQS")) {
                            nmabqs.makeMove("BLACK", game.getBoard());

                            
                            updateBoard();
                            logMove("BLACK made a move.");
                            game.saveBoard();
                            whiteTurn = true;
                        } else if (blackPlayer.equals("NegaMax")) {
                            negaMaxAB.makeMove("BLACK", game.getBoard());

                            
                            updateBoard();
                            logMove("BLACK made a move.");
                            game.saveBoard();
                            whiteTurn = true;
                        } else if (blackPlayer.equals("Player")) {
                            if (selectedCell1 != null && selectedCell2 != null) {
                            makeMove(selectedCell1, selectedCell2, "BLACK");

                                
                            updateBoard();
                            logMove("BLACK made a move.");
                            game.saveBoard();
                            whiteTurn = true;
                            }
                        }
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
import java.util.Scanner;

public class EnglishDraughts {
    // Constant for board size
    private static final int BOARD_SIZE = 8;

    // The board is a 2D array of pieces. Initialized to BOARD_SIZE by BOARD_SIZE
    private static pieces[][] board = new pieces[BOARD_SIZE][BOARD_SIZE];

    // Track the current player Player1 = 0, Player2 = 1
    private static int currentPlayer = 0;

    public static void main(String[] args) {
        System.out.println("Welcome to English Draughts");
        System.out.println("P1 = player 1 pieces (K1 King)");
        System.out.println("P2 = player 2 pieces (K2 King)");
        System.out.println();
        // Initialize the board with pieces in their starting positions
        initializeBoard();

        // Print the initial board
        printBoard();

        // Start the game
        playGame();
    }

    // Initializes the board with pieces in their starting positions
    private static void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Place pieces only on dark squares (odd sum of i + j)
                if ((i + j) % 2 == 1) {
                    // Player 1's pieces are placed on rows 0, 1, 2
                    if (i < 3) {
                        board[i][j] = new pieces(j, i, 0); // Player 1 (P1)
                    }
                    // Player 2's pieces are placed on rows 5, 6, 7
                    else if (i > 4) {
                        board[i][j] = new pieces(j, i, 1); // Player 2 (P2)
                    } else {
                        board[i][j] = null; // Empty middle rows
                    }
                } else {
                    board[i][j] = null; // Light squares remain empty
                }
            }
        }
    }

    // Prints the current state of the board
    private static void printBoard() {
        System.out.println("  A B C D E F G H");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + 1 + " "); // Print the row number
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Check if the square is occupied by a piece
                if (board[i][j] != null) {
                    // If it's a king, print K1 or K2
                    if (board[i][j].isKing()) {
                        if (board[i][j].getPlayer() == 0) {
                            System.out.print("K1 "); // Player 1's king
                        } else {
                            System.out.print("K2 "); // Player 2's king
                        }
                    } else {
                        // If it's a regular piece, print P1 or P2
                        if (board[i][j].getPlayer() == 0) {
                            System.out.print("P1 "); // Player 1's piece
                        } else {
                            System.out.print("P2 "); // Player 2's piece
                        }
                    }
                } else {
                    System.out.print("- "); // Empty square
                }
            }
            System.out.println();
        }
    }

    // Main game loop that handles turns
    private static void playGame() {
        Scanner scanner = new Scanner(System.in);

        // Loop the game until there's a winner
        while (true) {
            System.out.println("Player " + (currentPlayer + 1) + "'s turn.");
            System.out.print("Enter the piece to move (e.g., A2): ");
            String pieceToMove = scanner.next().toUpperCase(); // Read piece to move
            System.out.print("Enter the destination (e.g., A4): ");
            String destination = scanner.next().toUpperCase(); // Read destination

            // Convert input coordinates to row/column indices
            int pieceRow = pieceToMove.charAt(1) - '1';
            int pieceCol = pieceToMove.charAt(0) - 'A';
            int destRow = destination.charAt(1) - '1';
            int destCol = destination.charAt(0) - 'A';

            // Check if the move is valid
            if (isValidMove(pieceRow, pieceCol, destRow, destCol)) {
                // Make the move if it's valid
                makeMove(pieceRow, pieceCol, destRow, destCol);

                // Print the updated board
                printBoard();

                // Check if the game is over
                if (isGameOver()) {
                    System.out.println("Game over. Player " + (currentPlayer + 1) + " wins!");
                    break;
                }

                // Switch turns between players
                currentPlayer = (currentPlayer + 1) % 2;
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }

    // Validates if a move is valid
    private static boolean isValidMove(int pieceRow, int pieceCol, int destRow, int destCol) {
        // Check if the piece is out of bounds
        if (pieceRow < 0 || pieceRow >= BOARD_SIZE || pieceCol < 0 || pieceCol >= BOARD_SIZE) {
            System.out.println("Enter a valid move.");
            return false;
        }
        if (destRow < 0 || destRow >= BOARD_SIZE || destCol < 0 || destCol >= BOARD_SIZE) {
            System.out.println("Enter a valid move.");
            return false;
        }

        // Check if the piece is owned by the current player
        if (board[pieceRow][pieceCol] == null || board[pieceRow][pieceCol].getPlayer() != currentPlayer) {
            return false;
        }

        // Check if the destination is empty
        if (board[destRow][destCol] != null) {
            return false;
        }

        // Check if the move is diagonal
        if (Math.abs(pieceRow - destRow) != Math.abs(pieceCol - destCol)) {
            return false;
        }

        // Check if the move is forward for regular pieces (not kings)
        if (!board[pieceRow][pieceCol].isKing() && (currentPlayer == 0 && pieceRow > destRow || currentPlayer == 1 && pieceRow < destRow)) {
            return false;
        }

        // Check if the move is a capture
        if (Math.abs(pieceRow - destRow) == 2) {
            int midRow = (pieceRow + destRow) / 2;
            int midCol = (pieceCol + destCol) / 2;
            if (board[midRow][midCol] == null || board[midRow][midCol].getPlayer() == currentPlayer) {
                return false;
            }
        }

        return true; // If all checks passed, the move is valid
    }

    // Makes the move on the board and handles captures
    private static void makeMove(int pieceRow, int pieceCol, int destRow, int destCol) {
        // Move the piece to the new position
        board[destRow][destCol] = board[pieceRow][pieceCol];
        board[pieceRow][pieceCol] = null;

        // If it's a capture, remove the captured piece
        if (Math.abs(pieceRow - destRow) == 2) {
            int midRow = (pieceRow + destRow) / 2;
            int midCol = (pieceCol + destCol) / 2;
            board[midRow][midCol] = null; // Remove the captured piece
        }

        // If the piece reaches the opposite side, promote it to a king
        if (currentPlayer == 0 && destRow == BOARD_SIZE - 1) {
            board[destRow][destCol].setKing(true);
        }
        if (currentPlayer == 1 && destRow == 0) {
            board[destRow][destCol].setKing(true);
        }
    }

    // Checks if the game is over (i.e., one player has no pieces left)
    private static boolean isGameOver() {
        // Loop through the board and check if the opponent has any pieces left
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null && board[i][j].getPlayer() == (currentPlayer + 1) % 2) {
                    return false; // There are still pieces for the opponent
                }
            }
        }
        return true; // The opponent has no pieces left
    }
}

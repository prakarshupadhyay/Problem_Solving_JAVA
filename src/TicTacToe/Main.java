package TicTacToe;
import java.util.Scanner;

public class Main {

    // Display the game board
    public static void printBoard(char[][] board) {

        System.out.println();

        for (int i = 0; i < 3; i++) {

            System.out.print(" ");

            for (int j = 0; j < 3; j++) {

                System.out.print(board[i][j]);

                if (j < 2) {
                    System.out.print(" | ");
                }
            }

            System.out.println();

            if (i < 2) {
                System.out.println("---+---+---");
            }
        }

        System.out.println();
    }

    // Check whether a player has won
    public static boolean checkWinner(char[][] board, char player) {

        // Check rows
        for (int i = 0; i < 3; i++) {

            if (board[i][0] == player &&
                board[i][1] == player &&
                board[i][2] == player) {

                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {

            if (board[0][j] == player &&
                board[1][j] == player &&
                board[2][j] == player) {

                return true;
            }
        }

        // Check first diagonal
        if (board[0][0] == player &&
            board[1][1] == player &&
            board[2][2] == player) {

            return true;
        }

        // Check second diagonal
        if (board[0][2] == player &&
            board[1][1] == player &&
            board[2][0] == player) {

            return true;
        }

        return false;
    }

    // Check whether the board is full
    public static boolean isBoardFull(char[][] board) {

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    // Main game
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        boolean playAgain = true;

        System.out.println("================================");
        System.out.println("       TIC TAC TOE GAME");
        System.out.println("================================");

        while (playAgain) {

            // Create empty board
            char[][] board = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
            };

            char currentPlayer = 'X';

            boolean gameOver = false;

            System.out.println("\nPlayer 1: X");
            System.out.println("Player 2: O");

            // Game loop
            while (!gameOver) {

                printBoard(board);

                System.out.println("Player " + currentPlayer + "'s turn");

                int row, col;

                while (true) {

                    System.out.print("Enter row (1-3): ");

                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input! Enter a number.");
                        sc.next();
                        continue;
                    }

                    row = sc.nextInt();

                    System.out.print("Enter column (1-3): ");

                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input! Enter a number.");
                        sc.next();
                        continue;
                    }

                    col = sc.nextInt();

                    // Convert to array indexes
                    row--;
                    col--;

                    // Check valid position
                    if (row < 0 || row > 2 || col < 0 || col > 2) {

                        System.out.println("Invalid position! Try again.");

                    } else if (board[row][col] != ' ') {

                        System.out.println("Position already occupied! Try again.");

                    } else {

                        break;
                    }
                }

                // Place player's symbol
                board[row][col] = currentPlayer;

                // Check winner
                if (checkWinner(board, currentPlayer)) {

                    printBoard(board);

                    System.out.println("================================");
                    System.out.println("PLAYER " + currentPlayer + " WINS!");
                    System.out.println("================================");

                    gameOver = true;
                }

                // Check draw
                else if (isBoardFull(board)) {

                    printBoard(board);

                    System.out.println("================================");
                    System.out.println("       IT'S A DRAW!");
                    System.out.println("================================");

                    gameOver = true;
                }

                // Switch player
                else {

                    if (currentPlayer == 'X') {
                        currentPlayer = 'O';
                    } else {
                        currentPlayer = 'X';
                    }
                }
            }

            // Ask whether to play again
            System.out.print("\nDo you want to play again? (yes/no): ");

            String choice = sc.next();

            if (choice.equalsIgnoreCase("no")) {
                playAgain = false;
            }
        }

        System.out.println("\nThanks for playing Tic Tac Toe!");

        sc.close();
    }
}

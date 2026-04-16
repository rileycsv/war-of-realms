import java.util.Scanner;

public class Empires 
{

    static final int SIZE = 8;
    static String[][] board = new String[SIZE][SIZE];
    static boolean playerOneTurn = true;

    public static void main(String[] args) 
    {
        Scanner input = new Scanner(System.in);
        initializeBoard();

        while (true) 
        {
            printBoard();
            System.out.println((playerOneTurn ? "Player 1" : "Player 2") + "'s turn");

            System.out.print("Enter piece position (row col): ");
            int sr = input.nextInt();
            int sc = input.nextInt();

            System.out.print("Enter destination (row col): ");
            int er = input.nextInt();
            int ec = input.nextInt();

            if (!isInBounds(sr, sc) || !isInBounds(er, ec)) 
            {
                System.out.println("Invalid coordinates. Try again.");
                continue;
            }

            String piece = board[sr][sc];

            if (piece.equals(".")) {
                System.out.println("No piece there.");
                continue;
            }

            // Enforce turns
            if (playerOneTurn && piece.equals(piece.toLowerCase())) 
            {
                System.out.println("That's Player 2's piece.");
                continue;
            }
            if (!playerOneTurn && piece.equals(piece.toUpperCase())) 
            {
                System.out.println("That's Player 1's piece.");
                continue;
            }

            if (isValidMove(piece.toUpperCase(), sr, sc, er, ec)) 
            {
                String target = board[er][ec];

                // Win condition: capture opponent Mongolian leader (M)
                if (target.equals(playerOneTurn ? "m" : "M")) {
                    System.out.println((playerOneTurn ? "Player 1" : "Player 2") + " wins!");
                    break;
                }

                board[er][ec] = piece;
                board[sr][sc] = ".";

                playerOneTurn = !playerOneTurn;
            } else {
                System.out.println("Invalid move.");
            }
        }

        input.close();
    }

    public static void initializeBoard() 
    {
        // Player 1 (uppercase)
        board[0][0] = "E";
        board[0][1] = "H";
        board[0][2] = "P";
        board[0][3] = "M";

        // Player 2 (lowercase)
        board[7][0] = "e";
        board[7][1] = "h";
        board[7][2] = "p";
        board[7][3] = "m";

        for (int i = 1; i < SIZE - 1; i++) 
        {
            for (int j = 0; j < SIZE; j++) 
            {
                board[i][j] = ".";
            }
        }
    }

    public static void printBoard() 
    {
        System.out.println("\nBoard:");
        for (int i = 0; i < SIZE; i++) 
        {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static boolean isInBounds(int r, int c) 
    {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    public static boolean isValidMove(String piece, int sr, int sc, int er, int ec) 
    {
        int dRow = Math.abs(er - sr);
        int dCol = Math.abs(ec - sc);

        switch (piece) 
        {
            case "H": // Hussite
                return (dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2);

            case "M": // Mongolian
                return (dRow == dCol) || (sr == er) || (sc == ec);

            case "E": // English
                return (sr == er) || (sc == ec);
                
            case "P": // Portuguese
                return (dRow == dCol);

            default:
                return false;
        }
    }
}

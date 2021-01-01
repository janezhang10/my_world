package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private Font font;
    private boolean gameOver;
    private boolean playerTurn;
    private int goal;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public MemoryGame(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        rand = new Random(seed);
    }

    /**
     * Draw the instruction page at the start of the game to tell the player
     * what round they need to get to complete the game.
     */
    private void drawInstructionPage() {
        goal = 2 + rand.nextInt(4);
        String one = "Get to Round " + goal + "!";
        String two = "Copy the strings exactly. Be careful,";
        String three = "it gets harder the more rounds you go for!";
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(width / 2, height / 2 + 2, one);
        StdDraw.text(width / 2, height / 2, two);
        StdDraw.text(width / 2, height / 2 - 2, three);

        for (int i = 0; i < 1500; i++) {
            StdDraw.show();
        }
    }

    /**
     * Creates a random string of length n for the player to type in.
     * @param n Integer of the length of the string.
     * @return String of a random string of length n.
     */
    private String generateRandomString(int n) {
        String result = "";
        while (n > 0) {
            result += CHARACTERS[rand.nextInt(26)];
            n--;
        }
        return result;
    }

    /**
     * Draws the game with the HUD and the given string at the center of the
     * screen.
     * @param s String of the text to be displayed on the screen.
     */
    private void drawFrame(String s) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(width / 2, height / 2, s);

        drawHUD();
    }

    /**
     * Draws the HUD with the round number, simple instruction, and
     * goal number.
     */
    private void drawHUD() {
        String rnd = "Round: " + round;
        String g = "Goal: " + goal;

        StdDraw.text(3, height - 1, rnd);
        StdDraw.line(0, height - 2, width, height - 2);
        if (playerTurn) {
            StdDraw.text(width / 2, height - 1, "Type!");
        } else {
            StdDraw.text(width / 2, height - 1, "Watch!");
        }
        StdDraw.text(width - 5, height - 1, g);
        StdDraw.show();
    }

    /**
     * Flashes the sequence of the given letters with one additional
     * letter at a time in the sequence.
     * @param letters String to be displayed on the screen for the player
     *                to type.
     */
    private void flashSequence(String letters) {
        drawHUD();
        String toDisplay = "";
        for (int i = 0; i < letters.length(); i++) {
            toDisplay += letters.charAt(i);
            drawFrame(toDisplay);
            StdDraw.show();
            for (int j = 0; j < 1000; j++) {
                StdDraw.show();
            }
            StdDraw.clear(StdDraw.BLACK);
            drawHUD();
            for (int j = 0; j < 500; j++) {
                StdDraw.show();
            }
        }
    }

    /**
     * Draws the letters of the characters inputted by the player as they
     * type. Continues to check for letters until the player has entered
     * n letters.
     * @param n Integer of the length of the expected number of letters
     *          typed by the player.
     * @return String typed by the player of length n.
     */
    private String solicitNCharsInput(int n) {
        StdDraw.clear(StdDraw.BLACK);
        drawHUD();
        String input = "";
        while (input.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                input += StdDraw.nextKeyTyped();
                drawFrame(input);
                StdDraw.show();
            }
        }
        return input;
    }

    /**
     * Starts and runs the game until the player loses.
     * @return Boolean of whether the player reached the goal or not.
     */
    public boolean startGame() {
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        drawInstructionPage();
        gameOver = false;
        round = 1;

        while (!gameOver) {
            playerTurn = false;
            String rnd = "Round: " + round;
            drawFrame(rnd);
            for (int j = 0; j < 1000; j++) {
                StdDraw.show();
            }
            String randStr = generateRandomString(round);
            flashSequence(randStr);
            playerTurn = true;
            String input = solicitNCharsInput(round);
            if (!input.equals(randStr)) {
                gameOver = true;
                String endGame = "Game Over! You made it to round: "  + round;
                drawFrame(endGame);
                StdDraw.show();
            } else if (round == goal) {
                gameOver = true;
            } else {
                round++;
            }
        }
        return round >= goal;
    }

}

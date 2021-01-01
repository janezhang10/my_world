package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private Font font;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};
    private String enc;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
//        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
        startGame();
    }

    public String generateRandomString(int n) {
        String result = "";
        while (n > 0) {
            result += CHARACTERS[rand.nextInt(26)];
            n--;
        }
        return result;
    }

    public void drawFrame(String s) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(width/2, height/2, s);

        drawHUD();
    }

    public void drawHUD() {
        String rnd = "Round: " + round;

        StdDraw.text(3, height - 1, rnd);
        StdDraw.text(width - 10, height - 1, enc);
        StdDraw.line(0, height - 2, width, height - 2);
        if (playerTurn) {
            StdDraw.text(width / 2, height - 1, "Type!");
        } else {
            StdDraw.text(width / 2, height - 1, "Watch!");
        }
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        drawHUD();
        String toDisplay = "";
        for (int i = 0; i < letters.length(); i++) {
            toDisplay += letters.charAt(i);
            drawFrame(toDisplay);
            StdDraw.show();
            StdDraw.pause(1000);
            StdDraw.clear(StdDraw.BLACK);
            drawHUD();
            StdDraw.show();
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
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

    public void startGame() {
        gameOver = false;
        round = 1;

        while (!gameOver) {
            enc = ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)];
            playerTurn = false;
            String rnd = "Round: " + round;
            drawFrame(rnd);
            StdDraw.show();
            StdDraw.pause(1000);
            String randStr = generateRandomString(round);
            System.out.println(randStr);
            flashSequence(randStr);
            playerTurn = true;
            String input = solicitNCharsInput(round);
            System.out.println(input);
            if (!input.equals(randStr)) {
                gameOver = true;
                String endGame = "Game Over! You made it to round: "  + round;
                drawFrame(endGame);
                StdDraw.show();
            } else {
                round++;
            }
        }
    }

}

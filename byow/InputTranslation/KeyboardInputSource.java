package byow.InputTranslation;

/**
 * @Source Created by hug with a few modifications.
 */
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class KeyboardInputSource implements InputSource {
    private boolean printKeys;
    String input;
    TERenderer ter;
    TETile[][] world;

    public KeyboardInputSource(TERenderer t, TETile[][] w, boolean print) {
        input = "";
        ter = t;
        world = w;
        printKeys = print;
    }

    /**
     * Uses StdDraw's keyboard input capabilities to detect the typed characters and
     * draws the text out based on the boolean value of print_keys.
     * @return Character of the most recently typed key using StdDraw.
     */
    public char getNextKey() {
        while (true) {
            if (world != null) {
                ter.renderFrame(world);
            }

            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                input += c;
                if (c == 'S' || c == 'L') {
                    printKeys = false;
                }
                if (printKeys) {
                    drawFrame(input, 35, 80);
                }
                return c;
            }
        }
    }

    /**
     * Draws each frame as the user enters the seed
     * @param s Text to be written.
     * @param width Width of the StdDraw window.
     * @param height Height of the StdDraw window.
     */
    public void drawFrame(String s, int width, int height) {
        StdDraw.clear(StdDraw.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 17);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        String instr = "Please enter numerical values for";
        StdDraw.text(width / 2, height / 2 + 6, instr);
        instr =  "desired seed and type 's' or 'S' when done.";
        StdDraw.text(width / 2, height / 2 + 4, instr);
        font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }


    public boolean possibleNextInput() {
        return true;
    }
}

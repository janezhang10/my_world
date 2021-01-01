package byow.Core;

import byow.InputTranslation.InputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Avatar {
    private TETile icon;
    private int x;
    private int y;
    private Random ran;
    private TETile[][] world;
    private TERenderer ter;
    private boolean inputType = false;
    private TETile trailTile;
    private InputSource input;
    private MemoryGame game;

    private static final TETile WALL = Tileset.WALL;
    private static final TETile NOTHING = Tileset.NOTHING;
    private static final TETile GRASS = Tileset.GRASS;
    private static final TETile SAND = Tileset.SAND;
    private static final TETile FLOOR = Tileset.FLOOR;
    private static final TETile FLOOR_TWO = Tileset.FLOOR_TWO;
    private static final TETile FLOWER = Tileset.FLOWER;
    private static final TETile FLOWER_TWO = Tileset.FLOWER_TWO;
    private static final TETile TREE = Tileset.TREE;
    private static final TETile TREE_TWO = Tileset.TREE_TWO;
    private static final TETile ENCOUNTER_TILE = Tileset.SAND;

    public Avatar(TETile a, TETile[][] w, long s, TERenderer t, boolean i, InputSource inp) {
        game = new MemoryGame(40, 40, s);
        icon = a;
        world = w;
        ran = new Random(s);
        ter = t;
        inputType = i;
        trailTile = FLOOR;
        input = inp;
    }

    /**
     * Sets the x and y coordinates of the avatar in the world.
     * @param a Integer of the x-coordinate.
     * @param b Integer of the y-coordinate.
     */
    public void setXY(int a, int b) {
        x = a;
        y = b;
        world[x][y] = icon;
    }

    /**
     * Moves the avatar to the new location and also checks to see if
     * the avatar has landed on a tile to play the game.
     * @return String 'T' if the player won the game and 'F' otherwise.
     */
    private String move() {
        String ret = playGame(x, y);
        world[x][y] = icon;
        return ret;
    }

    /**
     * Moves the avatar to the left unit if the tile on the left is not
     * a wall.
     * @return String of outcome of the player playing the game.
     */
    public String moveLeft() {
        if (world[x - 1][y] == WALL) {
            return null;
        }
        world[x][y] = trailTile;
        this.x--;
        return move();
    }

    /**
     * Moves the avatar to the right unit if the tile on the right
     * is not a wall.
     * @return String of outcome of the player playing the game.
     */
    public String moveRight() {
        if (world[x + 1][y] == WALL) {
            return null;
        }
        world[x][y] = trailTile;
        this.x++;
        return move();
    }

    /**
     * Moves the avatar to the above unit if the tile above is not a
     * wall.
     * @return String of outcome of the player playing the game.
     */
    public String moveUp() {
        if (world[x][y + 1] == WALL) {
            return null;
        }
        world[x][y] = trailTile;
        this.y++;
        return move();
    }

    /**
     * Moves the avatar to the below unit if the tile below is not a
     * wall.
     * @return String of outcome of the player playing the game.
     */
    public String moveDown() {
        if (world[x][y - 1] == WALL) {
            return null;
        }
        world[x][y] = trailTile;
        this.y--;
        return move();
    }

    /**
     * Doesn't move the avatar at all.
     * @return Null because the player did not move.
     */
    public String doNothing() {
        return null;
    }

    /**
     * Gets the current x-coordinate of the avatar.
     * @return Integer of the avatar's x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the current y-coordinate of the avatar.
     * @return Integer of the avatar's y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Checks to see if the player will play a game based on the next
     * tile of the avatar.
     * @param a Integer of the next x-coordinate.
     * @param b Integer of the next y-coordinate.
     * @return String of outcome of the player playing the game.
     */
    private String playGame(int a, int b) {
        String ret;
        if (world[a][b] == ENCOUNTER_TILE) {
            if (!inputType && input.possibleNextInput()) {
                char nextCh = input.getNextKey();
                if (nextCh == 'T') {
                    changeTrail();
                    ret = "T";
                } else {
                    ret = "F";
                }
            } else if (inputType) {
                boolean changeTrail = game.startGame();
                if (changeTrail) {
                    changeTrail();
                    ret = "T";
                } else {
                    ret = "F";
                }
                ter.initialize(world.length, world[0].length);
            } else {
                return null;
            }
            return ret;
        }
        return null;
    }

    /**
     * Sets the tile trailing behind the avatar as a random tile.
     */
    private void changeTrail() {
        trailTile = randomTile();
    }

    /**
     * Randomly chooses a tile.
     * @return A random tile.
     */
    private TETile randomTile() {
        int randNum = ran.nextInt(6);
        switch (randNum) {
            case 0:
                return Tileset.FLOOR;
            case 1:
                return Tileset.FLOOR_TWO;
            case 2:
                return Tileset.FLOWER;
            case 3:
                return Tileset.FLOWER_TWO;
            case 4:
                return Tileset.TREE;
            case 5:
                return Tileset.TREE_TWO;
            default:
                return FLOOR;
        }
    }
}

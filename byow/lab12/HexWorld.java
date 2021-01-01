package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private int size;
    private TERenderer ter;
    private TETile[][] world;
    private Random rand;

    public HexWorld(int sz, int seed) {
        size = sz;
        rand = new Random(seed);
        ter = new TERenderer();

        int width = 13 * size;
        int height = 14 * size;
        ter.initialize(width, height);
        world = new TETile[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        tesslateWorld();
        ter.renderFrame(world);
    }
    public void drawOneHex(int x, int y, TETile tile) {
        int width = size;
        int height = 1;
        while (height <= size) {
            for (int i = x; i < x + width; i++) {
                world[i][y] = tile;
            }
            width += 2;
            x--;
            y++;
            height++;
        }
        width -= 2;
        x++;
        while (height > 1) {
            for (int i = x; i < x + width; i++) {
                world[i][y] = tile;
            }
            width -= 2;
            x++;
            y++;
            height--;
        }
    }

    private TETile randomTile() {
        int randNum = rand.nextInt(6);
        switch(randNum) {
            case 0:
                return Tileset.WALL;
            case 1:
                return Tileset.SAND;
            case 2:
                return Tileset.GRASS;
            case 3:
                return Tileset.FLOWER;
            case 4:
                return Tileset.AVATAR;
            case 5:
                return Tileset.TREE;
            default:
                return null;
        }
    }

    private void addColumnOfHex(int x, int y, int numHex, TETile[][] world) {
        for (int i = 0; i < numHex; i++) {
            TETile tile = randomTile();
            drawOneHex(x, y, tile);
            y -= 2 * size;
        }
    }

    private void tesslateWorld() {

        int width = 10 * size;
        int height = 12 * size;

        TETile[][] world = new TETile[width][height];

        int x = size + 1;
        int y = (height - 1) - 2 * size;
        addColumnOfHex(x, y, 3, world);

        x += 2 * size - 1;
        y += size;
        addColumnOfHex(x, y, 4, world);

        x += 2 * size - 1;
        y += size;
        addColumnOfHex(x, y, 5, world);

        x += 2 * size - 1;
        y -= size;
        addColumnOfHex(x, y, 4, world);

        x += 2 * size - 1;
        y -= size;
        addColumnOfHex(x, y, 3, world);
    }
}

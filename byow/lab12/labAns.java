package byow.lab12;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class labAns {
    private static final int SIZE = 3;
    private static Random rand = new Random(42);

    private static class Hex {
        private int _x;
        private int _y;
        private TETile _tile;

        public Hex(int x, int y, TETile tile) {
            _x = x;
            _y = y;
            _tile = tile;
        }

        public void addHex(TETile[][] tiles) {

        }
    }

    private static void initWorld(TETile[][] tiles) {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private static TETile randomTile() {
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

    private static void addColumnOfHex(int x, int y, int numHex, TETile[][] world) {
        for (int i = 0; i < numHex; i++) {
            TETile tile = randomTile();
            Hex h = new Hex(x, y, tile);
            y -= 2 * SIZE;
        }
    }

    private static void tesslateWorld() {

        int width = 10 * SIZE;
        int height = 12 * SIZE;

        TETile[][] world = new TETile[width][height];

        int x = SIZE + 1;
        int y = (height - 1) - 2 * SIZE;
        addColumnOfHex(x, y, 3, world);

        x += 2 * SIZE - 1;
        y += SIZE;
        addColumnOfHex(x, y, 4, world);

        x += 2 * SIZE - 1;
        y += SIZE;
        addColumnOfHex(x, y, 5, world);

        x += 2 * SIZE - 1;
        y -= SIZE;
        addColumnOfHex(x, y, 4, world);

        x += 2 * SIZE - 1;
        y -= SIZE;
        addColumnOfHex(x, y, 3, world);
    }

    public static void main(String[] args) {

    }
}

package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class WorldGenerator {

    private static final TETile NOTHING = Tileset.NOTHING;
    private static final TETile WALL = Tileset.WALL;
    private static final TETile FLOOR = Tileset.FLOOR;
    private static final TETile ENCOUNTER_TILE = Tileset.SAND;

    TETile[][] world;
    private int worldMaxX;
    private int worldMaxY;

    private int numOfTilesToUse;
    private Random rand;

    Node lastRoom;

    public WorldGenerator(int x, int y, long seed) {
        world = new TETile[x][y];
        worldMaxX = x - 1;
        worldMaxY = y - 3;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                world[i][j] = NOTHING;
            }
        }

        rand = new Random(seed);

        numOfTilesToUse = (7 * x * y) / 8;
        createFirstRoom();
    }

    /**
     * Create the first room to start the world.
     */
    private void createFirstRoom() {
        Node currRoom = roomNode();
        drawRoom(currRoom);
        lastRoom = currRoom;
        numOfTilesToUse -= (currRoom.width + 2) * (currRoom.height + 2);
        createRoom();
    }

    /**
     * Continually create new rooms until numOfTilesToUse reaches 0.
     */
    private void createRoom() {
        Node currRoom = roomNode();
        drawRoom(currRoom);
        numOfTilesToUse -= (currRoom.width + 2) * (currRoom.height + 2);
        connectRooms(currRoom, lastRoom);
        lastRoom = currRoom;
        if (numOfTilesToUse > 0) {
            createRoom();
        }
    }

    /**
     * Creates a node with the properly ordered x and y-coordinates along with the
     * width and height of the room.
     * @return Node of randomly generated room dimension and location.
     */
    private Node roomNode() {
        int x = 1 + rand.nextInt(worldMaxX - 1);
        int y = 1 + rand.nextInt(worldMaxY - 1);
        int w = 2 + rand.nextInt(5);
        int h = 2 + rand.nextInt(5);

        int xLeft, xRight, yTop, yBot;
        if (x + w  - 1 >= worldMaxX - 1) {
            xLeft = x - w + 1;
            xRight = x;
        } else {
            xLeft = x;
            xRight = x + w - 1;
        }

        if (y + h - 1 >= worldMaxY - 1) {
            yTop = y;
            yBot = y - h + 1;
        } else {
            yTop = y + h - 1;
            yBot = y;
        }

        int width = xRight - xLeft;
        int height = yTop - yBot;

        return new Node(xLeft, xRight, yBot, yTop, width, height);
    }

    /**
     * Connects two rooms together with a hallway.
     * @param a A room node to connect to b.
     * @param b A room node to connect to a.
     */
    private void connectRooms(Node a, Node b) {
        int r = rand.nextInt(5);
        int aX = a.xLeft + rand.nextInt(a.width);
        int bX = b.xLeft + rand.nextInt(b.width);
        int aY = a.yBot + rand.nextInt(a.height);
        int bY = b.yBot + rand.nextInt(b.height);


        if (aX < bX) {
            for (int i = aX; i <= bX; i++) {
                world[i][aY] = FLOOR;
                makeWall(i, aY + 1);
                makeWall(i, aY - 1);
                makeWall(i + 1, aY);
                numOfTilesToUse -= 3;
            }
            if (aY < bY) {
                for (int i = aY; i <= bY; i++) {
                    world[bX][i] = FLOOR;
                    makeWall(bX - 1, i);
                    makeWall(bX + 1, i);
                    makeWall(bX, i + 1);
                    numOfTilesToUse -= 3;
                }
            } else {
                for (int i = aY; i >= bY; i--) {
                    world[bX][i] = FLOOR;
                    makeWall(bX - 1, i);
                    makeWall(bX + 1, i);
                    makeWall(bX, i + 1);
                    numOfTilesToUse -= 3;
                }
            }
        } else {
            for (int i = bX; i <= aX; i++) {
                world[i][bY] = FLOOR;
                makeWall(i, bY + 1);
                makeWall(i, bY - 1);
                makeWall(i + 1, bY);
                numOfTilesToUse -= 3;
            }
            if (aY < bY) {
                for (int i = aY; i <= bY; i++) {
                    world[aX][i] = FLOOR;
                    makeWall(aX - 1, i);
                    makeWall(aX + 1, i);
                    makeWall(aX, i + 1);
                    numOfTilesToUse -= 3;
                }
            } else {
                for (int i = aY; i >= bY; i--) {
                    world[aX][i] = FLOOR;
                    makeWall(aX - 1, i);
                    makeWall(aX + 1, i);
                    makeWall(aX, i + 1);
                    numOfTilesToUse -= 3;
                }
            }
        }
    }

    /**
     * Draw out the floor of the space that the room takes up.
     * @param currRoom Node of the room to draw.
     */
    private void drawRoom(Node currRoom) {
        int xLeft = currRoom.xLeft;
        int xRight = currRoom.xRight;
        int yBot = currRoom.yBot;
        int  yTop = currRoom.yTop;

        for (int i = xLeft; i <= xRight; i++) {
            for (int j = yBot; j <= yTop; j++) {
                world[i][j] = FLOOR;
            }
        }

        changeRandTileInRoom(xLeft, xRight, yBot, yTop);

        drawWall(xLeft, xRight, yBot, yTop);
    }

    /**
     * Sets a random tile in the room with the given coordinates to be a tile
     * where if the avatar steps on it then the avatar will be met with an
     * encounter.
     * @param xLeft Integer of the x-coordinate of the left corners.
     * @param xRight Integer of the x-coordinate of the right corners.
     * @param yBot Integer of the y-coordinate of the top corners.
     * @param yTop Integer of the y-coordinate of the bottom corners.
     */
    private void changeRandTileInRoom(int xLeft, int xRight, int yBot, int yTop) {
        int xCoord = xLeft + rand.nextInt(xRight - xLeft);
        int yCoord = yBot + rand.nextInt(yTop - yBot);

        world[xCoord][yCoord] = ENCOUNTER_TILE;
    }

    /**
     * Draw out the walls of a room given properly ordered dimensions.
     * @param xLeft Most left x-coordinate of the room.
     * @param xRight Most right x-coordinate of the room.
     * @param yBot Highest y-coordinate of the room.
     * @param yTop Lowest y-coordinate of the room.
     */
    private void drawWall(int xLeft, int xRight, int yBot, int yTop) {
        for (int i = xLeft - 1; i <= xRight + 1; i++) {
            makeWall(i, yBot - 1);
            makeWall(i, yTop + 1);
        }
        for (int j = yBot; j <= yTop; j++) {
            makeWall(xLeft - 1, j);
            makeWall(xRight + 1, j);
        }
    }

    /**
     * Creates a wall at the given coordinate if the space is Tileset.NOTHING. Otherwise
     * do nothing.
     * @param x x-coordinate of wall location.
     * @param y y-coordinate of wall location.
     */
    private void makeWall(int x, int y) {
        if (x < 0 || x > worldMaxX || y < 0 || y > worldMaxY) {
            return;
        } else if (world[x][y].equals(NOTHING)) {
            world[x][y] = WALL;
        }
    }

    /**
     * @return TITile[][] object of the current world.
     */
    public TETile[][] getWorld() {
        return world;
    }

    class Node {
        private int xLeft;
        private int xRight;
        private int yBot;
        private int yTop;
        private int width;
        private int height;

        Node(int xL, int xR, int yB, int yT, int w, int h) {
            xLeft = xL;
            xRight = xR;
            yBot = yB;
            yTop = yT;
            width = w;
            height = h;
        }

        public int getXRight() {
            return xRight;
        }

        public int getyTop() {
            return yTop;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}

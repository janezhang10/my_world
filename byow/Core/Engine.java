package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import byow.TileEngine.Tileset;
import byow.InputTranslation.InputSource;
import byow.InputTranslation.KeyboardInputSource;
import byow.InputTranslation.StringInputSource;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int MENU_WIDTH = 35;
    private static final int MENU_HEIGHT = 50;
    private static final TETile WALL = Tileset.WALL;
    private static final TETile FLOOR = Tileset.FLOOR;
    private static final TETile NOTHING = Tileset.NOTHING;
    private static final boolean KEYBOARD = true;
    private static final boolean STRING = false;

    private TETile[][] world;
    private boolean continueGame;
    private Avatar avatar;
    private List<String> info;
    private boolean inputType = STRING;
    private TETile avatarTile = Tileset.AVATAR;

    private Random rand;

    /**
     * Allows for users to interact with the world with keyboard inputs in real time.
     * If the first letter is N, then a new world will be generated based on the
     * seed that the user enters.
     * If the first letter is L, then the world will be loaded from the most recent
     * save with the proper positions.
     * If the first letter is C, then a new window is displayed where the user can change
     * what they want their avatar to look like
     * If the first letter is Q, then the program will halt.
     * If the user enters an invalid character that doesn't correspond to any
     * commands on the menu then the user will be asked to enter a valid command.
     */
    public void interactWithKeyboard() {
        if (info == null) {
            info = new ArrayList<>();
        }
        inputType = KEYBOARD;
        continueGame = true;
        KeyboardInputSource input = new KeyboardInputSource(ter, world, true);
        drawMenu();

        char command = input.getNextKey();
        if (command == 'N') {
            StdDraw.clear(StdDraw.BLACK);
            long seed = getSeed(input);
            world = createWorld(seed, input);
            ter.initialize(WIDTH, HEIGHT);
            input = new KeyboardInputSource(ter, world, false);
            runGame(input);
        } else if (command == 'L') {
            String previousCommands = readData();
            world = interactWithInputString(previousCommands);
            inputType = KEYBOARD;
            ter.initialize(WIDTH, HEIGHT);
            input = new KeyboardInputSource(ter, world, false);
            runGame(input);
        } else if (command == 'C') {
            info.add("C");
            drawAvatarMenu();
            char avatarChoice = input.getNextKey();
            info.add(String.valueOf(avatarChoice));
            setAvatarTile(avatarChoice);
            interactWithKeyboard();
        } else if (command == 'Q') {
            System.exit(0);
        } else {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2, "Please enter a valid command");
            for (int j = 0; j < 5000; j++) {
                StdDraw.show();
            }
            interactWithKeyboard();
        }
    }

    /**
     * Method used for autograding and testingMENU_HEIGHTour code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactlMENU_HEIGHTas if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        inputType = STRING;
        if (info == null) {
            info = new ArrayList<>();
        }
        continueGame = true;
        StringInputSource inputString = new StringInputSource(input);

        char command = inputString.getNextKey();
        if (command == 'N') {
            long seed = getSeed(inputString);
            world = createWorld(seed, inputString);
            runGame(inputString);
        } else if (command == 'L') {
            String previousCommands = readData();
            world = interactWithInputString(previousCommands);
            runGame(inputString);
        } else if (command == 'C') {
            info.add("C");
            drawAvatarMenu();
            char avatarChoice = inputString.getNextKey();
            info.add(String.valueOf(avatarChoice));
            setAvatarTile(avatarChoice);
            String rem = inputString.getRemainingString();
            world = interactWithInputString(rem);
        } else {
            inputString = new StringInputSource(input);
            long seed = getSeed(inputString);
            world = createWorld(seed, inputString);
            runGame(inputString);
        }
        return world;
    }

    /**
     * Sets the avatar icon depending on the input of the player. If the player enters
     * any other character then the default avatar tile will be chosen.
     * @param a Character of the letter given by the player.
     */
    private void setAvatarTile(char a) {
        switch (a) {
            case 'D':
                avatarTile = Tileset.AVATAR;
                break;
            case 'M':
                avatarTile = Tileset.MOUNTAIN;
                break;
            case 'S':
                avatarTile = Tileset.LOCKED_DOOR;
                break;
            case 'W':
                avatarTile = Tileset.WATER;
                break;
            case 'B':
                avatarTile = Tileset.UNLOCKED_DOOR;
                break;
            default:
                avatarTile = Tileset.AVATAR;
                break;
        }
    }

    /**
     * Runs the game based on whether there is another character to process, and saves the progress
     * of the world when ':q' is typed in succession.
     * @param input InputSource type to use to check whether there's another character to process.
     * @Source How to create and write txt files:
     * https://stackabuse.com/reading-and-writing-files-in-java.
     */
    private void runGame(InputSource input) {
        while (input.possibleNextInput() && continueGame) {
            char nextCh = input.getNextKey();
            if (nextCh == ':') {
                if (input.possibleNextInput()) {
                    nextCh = input.getNextKey();
                    if (nextCh == 'Q') {
                        continueGame = false;
                        saveGame();
                    }
                }
            }
            info.add(String.valueOf(nextCh));
            moveAvatar(nextCh);
        }
        saveGame();
    }

    /**
     * Calls on the correct avatar method depending on the user's keyboard input. If
     * an invalid key is pressed then the avatar won't do anything.
     * @param input Character of the user's next input.
     */
    private void moveAvatar(char input) {
        String result;

        switch (input) {
            case 'W':
                result = avatar.moveUp();
                break;
            case 'A':
                result = avatar.moveLeft();
                break;
            case 'S':
                result = avatar.moveDown();
                break;
            case 'D':
                result = avatar.moveRight();
                break;
            default:
                result = avatar.doNothing();
                break;
        }

        if (result != null) {
            info.add(result);
        }
    }

    /**
     * Saves the game by creating and writing a text file with the current
     * game's information.
     */
    private void saveGame() {
        try {
            File saveFile = new File("byow/Core/load.txt");
            FileWriter writeFile = new FileWriter("byow/Core/load.txt");
            String commands = "";
            for (int i = 0; i < info.size(); i++) {
                commands += info.get(i);
            }
            writeFile.write(commands);
            writeFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred while creating/writing");
            e.printStackTrace();
        }
        if (inputType == KEYBOARD) {
            System.exit(0);
        }
    }

    /**
     * Gets the value of the seed from the input. If a letter is entered instead of a number
     * while entering the seed in between typing N and S, an IllegalArgumentException error
     * will be thrown.
     * @param input InputSource used to get the seed value.
     * @return Long type of the seed value.
     */
    private long getSeed(InputSource input) {
        String num = "";
        while (input.possibleNextInput()) {
            char nextCh = input.getNextKey();
            info.add(String.valueOf(nextCh));
            if (nextCh == 'N') {
                continue;
            } else if (nextCh == 'S') {
                break;
            } else {
                try {
                    num += nextCh;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Could not convert to integer: " + e);
                }
            }
        }
        long seed = Long.parseLong(num);
        return seed;
    }

    /**
     * Generates the world based on the given seed. Also creates an avatar instance for
     * the world.
     * @param seed Long value of the seed to be used to generate the world.
     * @return TETile[][] of the generated world.
     */
    private TETile[][] createWorld(long seed, InputSource inp) {
        rand = new Random(seed);
        WorldGenerator w = new WorldGenerator(WIDTH, HEIGHT, seed);
        TETile[][] finalWorldFrame = w.getWorld();
        avatar = new Avatar(avatarTile, finalWorldFrame, seed, ter, inputType, inp);
        initializeAvatar(w);
        return finalWorldFrame;
    }

    /**
     * Places the avatar in the last room that was created by WorldGenerator
     * with random x and y coordinates inside of that room.
     * @param w
     */
    private void initializeAvatar(WorldGenerator w) {
        int roomX = w.lastRoom.getXRight();
        int roomY = w.lastRoom.getyTop();
        int roomWidth = w.lastRoom.getWidth();
        int roomHeight = w.lastRoom.getHeight();

        int x = roomX - rand.nextInt(roomWidth);
        int y = roomY - rand.nextInt(roomHeight);
        avatar.setXY(x, y);
    }

    /**
     * Draws the menu if the input type is KEYBOARD so that the user knows what
     * characters corresponds to what commands.
     */
    private void drawMenu() {
        StdDraw.setCanvasSize(MENU_WIDTH * 10, MENU_HEIGHT * 10);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, MENU_WIDTH);
        StdDraw.setYscale(0, MENU_HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);
        String title = "zzZzzZz";
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 4 * 3, title);
        font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 + 2, "New Game (N)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2, "Load Game (L)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 2, "Change Avatar (C)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 4, "Quit (Q)");
        StdDraw.show();
    }

    /**
     * Draws the menu of avatar tile selections.
     */
    private void drawAvatarMenu() {
        StdDraw.setCanvasSize(MENU_WIDTH * 10, MENU_HEIGHT * 10);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, MENU_WIDTH);
        StdDraw.setYscale(0, MENU_HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);
        String title = "Choose Your Avatar";
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 4 * 3, title);
        font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 + 2, "Default: @ (D)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2, "Mountain: ▲ (M)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 2, "Square: █ (S)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 4, "Water: ≈ (W)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 6, "Cube: ▢ (B)");
        font = new Font("Monaco", Font.BOLD, 10);
        StdDraw.setFont(font);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 10, "Press Anything To Go Back");
        StdDraw.show();
    }

    /**
     * Reads the file with the previous load's information.
     * @return String of the moves from the previous game. If no file is found, then no previous
     * game has been playedMENU_HEIGHTet, and an error will be thrown.
     * @Source How to use a Scanner: https://www.w3schools.com/java/java_files_read.asp.
     */
    private String readData() {
        try {
            File loadData = new File("byow/Core/load.txt");
            Scanner myReader = new Scanner(loadData);
            if (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                return data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No loads available");
        }
        return "";
    }
}

package byow.InputTranslation;

/**
 * @Source Created by hug with a few modifications.
 */
public class StringInputSource implements InputSource  {
    private String input;
    private int index;

    public StringInputSource(String s) {
        index = 0;
        input = s;
    }

    /**
     * Gets the next character in the String at the instance variable index.
     * @return Next character in the String.
     */
    public char getNextKey() {
        char returnChar = Character.toUpperCase(input.charAt(index));
        index += 1;
        return returnChar;
    }

    /**
     * Checks to see if there's another character in the String at the current index.
     * @return True if there's another character in the String at the current index,
     * false otherwise.
     */
    public boolean possibleNextInput() {
        return index < input.length();
    }

    public String getRemainingString() {
        String val = "";
        for (int i = index; i < input.length(); i++) {
            val += input.charAt(i);
        }
        return val;
    }
}

public class ReverseInteger {

    public static void main(String[] args) {
        // Your solution goes here
        // Following is a (not so clever) example solution, feel free to remove any or all of the methods provided.
        String[] reversedNumbers = processInputs(args);
        String result = String.join(" ", reversedNumbers);
        System.out.println(result);
    }

    private static String[] processInputs(String[] initialStrings) {
        String[] reversed = new String[initialStrings.length];
        for (int i = 0; i<reversed.length; i++) {
            String reverseString = reverseString(initialStrings[i]);
            String stripped = String.valueOf(Long.parseLong(reverseString));
            reversed[i] = stripped;
        }
        return reversed;
    }

    private static String reverseString(String asString) {
        char[] in = asString.toCharArray();
        int len = asString.length();
        char[] out = new char[len];
        for (int i = 0; i < len; i++) {
            out[i] = in[len - 1 - i];
        }
        String result = String.valueOf(out);
        return result;
    }

}

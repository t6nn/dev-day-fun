import java.util.HashSet;
import java.util.Set;

public class MissingNumber {

  public static void main(String [] args) {
    // Your solution goes here
    // Following is a (not so clever) example solution, feel free to remove any or all of the methods provided.
    int[] array = convertToIntArray(args);
    System.out.println(findMissing(array));
  }

  private static int findMissing(int[] array) {
    Set<Integer> odd = new HashSet<>();
    for (int nr : array) {
      if (!odd.add(nr)) {
        odd.remove(nr);
      }
    }
    return odd.iterator().next();
  }

  private static int[] convertToIntArray(String[] stringArray) {
    int[] intArray = new int[stringArray.length];
    int i = 0;
    for (String nr : stringArray) {
      intArray[i++] = Integer.parseInt(nr);
    }
    return intArray;
  }

}

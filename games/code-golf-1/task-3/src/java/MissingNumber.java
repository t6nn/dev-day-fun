import java.util.*;

public class MissingNumber {

  public static void main(String [] args) {
    // Your solution goes here
    // Following is a (not so clever) example solution, feel free to remove any or all of the methods provided.
    List<Integer> ints = new ArrayList<>();
    Scanner s = new Scanner(System.in);
    while(s.hasNext()) {
      ints.add(s.nextInt());
    }
    System.out.println(findMissing(ints));
  }

  private static int findMissing(List<Integer> array) {
    Set<Integer> odd = new HashSet<>();
    for (int nr : array) {
      if (!odd.add(nr)) {
        odd.remove(nr);
      }
    }
    return odd.iterator().next();
  }

}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FindMatrixSum {

  public static void main(String [] args) throws IOException {
    // Your solution goes here
    // Following is a (not so clever) example solution, feel free to remove any or all of the methods provided.

    String input = new BufferedReader(new InputStreamReader(System.in)).readLine();
    int[][] matrix = constructMatrix(input);
    long sum = largestColRowSum(matrix);
    System.out.println(sum);
  }

  private static int[][] constructMatrix(String in) {
    String[] rows = in.split("\\$");
    int n = rows.length;
    int[][] matrix = new int[n][rows[0].split(",").length];
    for (int i = 0; i < n; i++) {
      int[] row = new int[n];
      int j = 0;
      String[] elements = rows[i].split(",");
      for (String element : elements) {
        row[j] = Integer.parseInt(element);
        j++;
      }
      matrix[i] = row;
    }
    return matrix;
  }

  private static long largestColRowSum(int[][] matrix) {
    long largest = Long.MIN_VALUE;
    for(int row = 0; row < matrix.length; row++) {
      long sum = 0;
      for(int col = 0; col < matrix[0].length; col++) {
        sum += matrix[row][col];
      }
      largest = Math.max(largest, sum);
    }
    for(int col = 0; col < matrix[0].length; col++) {
      long sum = 0;
      for(int row = 0; row < matrix.length; row++) {
        sum += matrix[row][col];
      }
      largest = Math.max(largest, sum);
    }
    return largest % 13092017;
  }

}

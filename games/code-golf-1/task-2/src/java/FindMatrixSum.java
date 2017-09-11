public class FindMatrixSum {

  public static void main(String [] args) {
    // Your solution goes here
    // Following is a (not so clever) example solution, feel free to remove any or all of the methods provided.
    int[][] matrix = constructMatrix(args[0]);
    int sum = matrixSum(matrix);
    System.out.println(sum);
  }

  private static int[][] constructMatrix(String in) {
    String[] rows = in.split("\\$");
    int n = rows.length;
    int[][] matrix = new int[n][n];
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

  private static int matrixSum(int[][] matrix) {
    int n = matrix.length;
    int m = matrix[0].length;
    int sum = 0;
    for (int i = 0; i < n; i++ ) {
      for (int j = 0 ; j < m; j++) {
        sum += matrix[i][j]%13092017;
      }
    }
    return sum%13092017;
  }

}

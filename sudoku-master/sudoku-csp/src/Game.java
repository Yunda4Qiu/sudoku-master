import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Game {
  private Sudoku sudoku;
  public int evaluations = 0;

  Game(Sudoku sudoku) {
    this.sudoku = sudoku;
  }

  public void showSudoku() {
    System.out.println(sudoku);
  }

  public int getEvaluations() {
    return evaluations;
  }

  // public boolean solve() {
  //   // TODO: Implement AC-3 algorithm
  //   // Reset evaluations count for each solve call
  //   //evaluations = 0;
  //   Queue<Field> queue = new ArrayDeque<>();
  //   for (Field[] row : sudoku.getBoard()) {
  //     for (Field field : row) {
  //       if (field.getValue() != 0) {
  //         queue.add(field);
  //       }
  //     }
  //   }

  //   while (!queue.isEmpty()) {
  //     Field current = queue.poll();
  //     for (Field neighbor : current.getNeighbours()) {
  //       if (arcReduce(neighbor, current)) {
  //         if (neighbor.getDomainSize() == 0) {
  //           return false; // Invalid sudoku state, domain becomes empty
  //         }
  //         queue.add(neighbor);
  //       }
  //     }
  //   }

  //   return backtrackSolve();
  // }


  public boolean solve(String heuristic) {
    Queue<Field> queue = new ArrayDeque<>();

    for (Field[] row : sudoku.getBoard()) {
        for (Field field : row) {
            if (field.getValue() != 0) {
                queue.add(field);
                evaluations++;
            }
        }
    }
    
    while (!queue.isEmpty()) {
        Field current = queue.poll();
        for (Field neighbor : current.getNeighbours()) {
            if (arcReduce(neighbor, current)) {
                if (neighbor.getDomainSize() == 0) {
                    return false; // Invalid sudoku state, domain becomes empty
                }
                queue.add(neighbor);
                evaluations++;
            }
        }
        // Apply the selected heuristic on the queue after processing each arc
        if (heuristic != "") {
            Heuristic.applyHeuristic(queue, heuristic);
        }
    }

    return backtrackSolve();
}




  /**
   * Use backtracking to complete the sudoku after applying AC-3 algorithm
   *
   * @return true if the sudoku is successfully completed, else false
   */
  private boolean backtrackSolve() {
    return backtrack();
  }

  private boolean backtrack() {
    // Find the next empty field
    int[] nextEmpty = findNextEmpty();
    int row = nextEmpty[0];
    int col = nextEmpty[1];

    // If there are no empty fields, the sudoku is solved
    if (row == -1) {
      return true;
    }

    // Try different values for the empty field
    for (int value = 1; value <= 9; value++) {
      if (isValidPlacement(row, col, value)) {
        // Assign the value to the field
        sudoku.getBoard()[row][col].setValue(value);
        evaluations++;

        // Recursively check if this assignment leads to a solution
        if (backtrack()) {
          return true;
        }

        // If the current assignment doesn't lead to a solution, reset the field and try the next value
        sudoku.getBoard()[row][col].setValue(0);
      }
    }

    // No value can be assigned to the current empty field, backtrack
    return false;
  }

  private int[] findNextEmpty() {
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (sudoku.getBoard()[i][j].getValue() == 0) {
          return new int[]{i, j};
        }
      }
    }
    return new int[]{-1, -1}; // Return -1 if no empty field is found
  }

  private boolean isValidPlacement(int row, int col, int value) {
    // Check row
    for (int j = 0; j < 9; j++) {
      if (sudoku.getBoard()[row][j].getValue() == value) {
        return false;
      }
    }

    // Check column
    for (int i = 0; i < 9; i++) {
      if (sudoku.getBoard()[i][col].getValue() == value) {
        return false;
      }
    }

    // Check 3x3 box
    int boxStartRow = 3 * (row / 3);
    int boxStartCol = 3 * (col / 3);
    for (int i = boxStartRow; i < boxStartRow + 3; i++) {
      for (int j = boxStartCol; j < boxStartCol + 3; j++) {
        if (sudoku.getBoard()[i][j].getValue() == value) {
          return false;
        }
      }
    }

    evaluations++;

    return true;
  }

  private boolean arcReduce(Field neighbor, Field current) {
    if (neighbor.removeFromDomain(current.getValue())) {
      if (neighbor.getDomainSize() == 0) {
        return true; // Domain reduced to zero, invalid state
      }
      if (neighbor.getDomainSize() == 1 && neighbor.getValue() == 0) {
        // Only one value left in domain, assign it
        neighbor.setValue(neighbor.getDomain().get(0)); 
        evaluations++;
      }
    }
    return false;
  }

  /**
   * Checks the validity of a sudoku solution
   *
   * @return true if the sudoku solution is correct
   */

  public boolean validSolution() {
    // TODO: Implement validity check

    // Check each row
    for (int i = 0; i < 9; i++) {
      if (!isUnique(sudoku.getBoard()[i])) {
        return false;
      }
    }

    // Check each column
    for (int j = 0; j < 9; j++) {
      Field[] column = new Field[9];
      for (int i = 0; i < 9; i++) {
        column[i] = sudoku.getBoard()[i][j];
      }
      if (!isUnique(column)) {
        return false;
      }
    }

    // Check each 3x3 box
    for (int boxRow = 0; boxRow < 9; boxRow += 3) {
      for (int boxCol = 0; boxCol < 9; boxCol += 3) {
        Field[] box = new Field[9];
        int k = 0;
        for (int i = boxRow; i < boxRow + 3; i++) {
          for (int j = boxCol; j < boxCol + 3; j++) {
            box[k++] = sudoku.getBoard()[i][j];
          }
        }
        if (!isUnique(box)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Checks if the given array of fields contains unique values (1-9)
   *
   * @param fields The array of fields to check
   * @return true if the values are unique, else false
   */
  private boolean isUnique(Field[] fields) {
    Set<Integer> values = new HashSet<>();
    for (Field field : fields) {
      int value = field.getValue();
      if (value != 0) {
        if (values.contains(value)) {
          return false;
        }
        values.add(value);
      }
    }
    return true;
  }
}
import java.io.File;

public class App {
    public static void main(String[] args) throws Exception {

        //start("Sudoku1.txt");
        
        String dirPath = "sudoku-master\\sudoku-csp\\";
        File directory = new File(dirPath);
        File[] files = directory.listFiles();

        // Available heuristics: "minimumRemainingValues", "priorityToFinalizedFields".
        
        // String heuristic = "";
        // String heuristic = "minimumRemainingValues";
        String heuristic = "priorityToFinalizedFields";
        

        for (File file : files) {
            if (file.isFile()) { 
                start(dirPath + file.getName(), heuristic);
            }
        }

        // start(dirPath + "Sudoku1.txt");
        // start(dirPath + "Sudoku2.txt");
        // start(dirPath + "Sudoku3.txt");
        // start(dirPath + "Sudoku4.txt");
        // start(dirPath + "Sudoku5.txt");
    }

    /**
     * Start AC-3 using the sudoku from the given filepath, and reports whether the sudoku could be solved or not, and how many steps the algorithm performed
     * 
     * @param filePath
     */
    // TODO: Add a heuristic parameter to the start function
    // Add a heuristic parameter to the start function
    public static void start(String filePath, String heuristic){
        File file = new File(filePath);

        Game game1 = new Game(new Sudoku(filePath));
        game1.showSudoku();

        // TODO: Use a heuristic as a parameter to the solve function
        // Use a heuristic as a parameter to the solve function
        if (game1.solve(heuristic) && game1.validSolution() && heuristic != ""){
            // System.out.println("Solved!");
            System.out.println(file.getName() + " solved using " + heuristic + " heuristic!");
            System.out.println(heuristic + " heuristic evaluations: " + game1.getEvaluations());
        }
        else if (game1.solve(heuristic) && game1.validSolution() && heuristic == ""){
            System.out.println(file.getName() + " solved without heuristic!");
            System.out.println("No heuristic evaluations: " + game1.getEvaluations());
        }
        else{
            System.out.println("Could not solve this sudoku :(");
            System.out.println("No heuristic evaluations: " + game1.getEvaluations());
        }
        game1.showSudoku();
    }
}

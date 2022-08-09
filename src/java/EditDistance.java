import java.util.Comparator;
import java.util.stream.Stream;

public class EditDistance {
    enum Operation {
        MATCH,
        INSERT,
        DELETE,
    }

    static class Cell {
        final int cost;
        final Operation operation;

        Cell(int cost, Operation operation) {
            this.cost = cost;
            this.operation = operation;
        }

        @Override
        public String toString() {
            return "{" +
                    "cost=" + cost +
                    ", operation=" + operation +
                    '}';
        }
    }

    private void editDistance(String text, String pattern, Cell[][] memo) {
        int n = text.length();
        int m = pattern.length();

        memo[0][0] = new Cell(0, Operation.MATCH);
        for (int i = 1; i <= n; i++) memo[i][0] = new Cell(i, Operation.INSERT);
        for (int j = 1; j <= m; j++) memo[0][j] = new Cell(j, Operation.DELETE);

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                Cell match = new Cell(memo[i - 1][j - 1].cost + (text.charAt(i - 1) == pattern.charAt(j - 1) ? 0 : 1), Operation.MATCH);
                Cell delete = new Cell(memo[i][j - 1].cost + 1, Operation.DELETE);
                Cell insert = new Cell(memo[i - 1][j].cost + 1, Operation.INSERT);
                memo[i][j] = Stream.of(match, delete, insert).min(Comparator.comparingInt(c -> c.cost)).get();
            }
        }
    }

    private int editDistance(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        Cell[][] memo = new Cell[n + 1][m + 1];
        editDistance(text, pattern, memo);
        printMatrix(memo);
        return memo[n][m].cost;
    }

    private void printMatrix(Cell[][] matrix) {
        System.out.print("\t");
        for (int j = 0; j < matrix[0].length; j++) {
            System.out.print(j + "\t");
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            System.out.print(i + "\t");
            for (int j = 0; j < matrix[0].length; j++) {
                Cell cell = matrix[i][j];
                System.out.print(cell.operation.toString().charAt(0) + ":" + cell.cost + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        EditDistance ed = new EditDistance();
        String text = "thou shalt";
        String pattern = "you should";
        int editDistance = ed.editDistance(text, pattern);
        System.out.printf("edit distance between '%s' and '%s': %d", text, pattern, editDistance);
    }
}

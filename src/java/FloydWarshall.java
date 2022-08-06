import java.util.Arrays;
import java.util.List;

public class FloydWarshall {

    static class Edge {
        final int x;
        final int y;
        final int weight;

        Edge(int x, int y, int weight) {
            this.x = x;
            this.y = y;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "x=" + x +
                    ", y=" + y +
                    ", weight=" + weight +
                    '}';
        }
    }

    private int[][] floydWarshall(int n, List<Edge> edges) {
        int[][] matrix = toMatrix(n, edges);

        System.out.println("start"); printMatrix(matrix);

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int ik = matrix[i][k];
                    int kj = matrix[k][j];
                    if (ik != Integer.MAX_VALUE && kj != Integer.MAX_VALUE) {
                        matrix[i][j] = Math.min(matrix[i][j], ik + kj);
                    }
                }
            }
            System.out.println("k=" + k); printMatrix(matrix);
        }

        return matrix;
    }

    private int[][] toMatrix(int n, List<Edge> edges) {
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = i == j ? 0 : Integer.MAX_VALUE;
            }
        }
        for (Edge edge : edges) {
            matrix[edge.x][edge.y] = edge.weight;
            matrix[edge.y][edge.x] = edge.weight;
        }
        return matrix;
    }

    private static Edge e(int x, int y, int weight) {
        return new Edge(x, y, weight);
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }

    public static void main(String[] args) {
//        List<Edge> edges = List.of(
//                e(0, 1, 5),
//                e(0, 2, 7),
//                e(0, 3, 12),
//                e(1, 2, 9),
//                e(2, 3, 4),
//                e(1, 6, 7),
//                e(2, 6, 4),
//                e(2, 4, 3),
//                e(3, 4, 7),
//                e(6, 4, 2),
//                e(4, 5, 2),
//                e(6, 5, 5)
//        );
//        int[][] result = new FloydWarshall().floydWarshall(7, edges);

        List<Edge> edges = List.of(
                e(0, 1, 2),
                e(1, 2, 3),
                e(2, 3, 1),
                e(0, 3, 8)
        );
        int[][] result = new FloydWarshall().floydWarshall(4, edges);
    }
}

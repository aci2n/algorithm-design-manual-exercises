import java.util.ArrayList;
import java.util.List;

public class FindCityWithFewestWithThreshold {
    class Solution {
        public int findTheCity(int n, int[][] edges, int distanceThreshold) {
            int[][] matrix = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = i == j ? 0 : Integer.MAX_VALUE;
                }
            }

            for (int[] edge : edges) {
                matrix[edge[0]][edge[1]] = matrix[edge[1]][edge[0]] = edge[2];
            }

            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        int ix = matrix[i][k];
                        int xj = matrix[k][j];

                        if (ix != Integer.MAX_VALUE && xj != Integer.MAX_VALUE) {
                            matrix[i][j] = Math.min(matrix[i][j], ix + xj);
                        }
                    }
                }
            }

            int minReachable = Integer.MAX_VALUE;
            int result = Integer.MIN_VALUE;

            for (int i = 0; i < n; i++) {
                int reachable = 0;

                for (int j = 0; j < n; j++) {
                    if (matrix[i][j] <= distanceThreshold) {
                        reachable++;
                    }
                }

                if (reachable <= minReachable) {
                    minReachable = reachable;
                    result = i;
                }
            }

            return result;
        }
    }
}

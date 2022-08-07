import java.util.*;

public class NetworkDelayTime {
    class Solution {
        public int networkDelayTime(int[][] times, int n, int k) {
            int[][] matrix = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = i == j ? 0 : Integer.MAX_VALUE;
                }
            }

            for (int[] edge : times) {
                matrix[edge[0] - 1][edge[1] - 1] = edge[2];
            }

            for (int x = 0; x < n; x++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        int ix = matrix[i][x];
                        int xj = matrix[x][j];

                        if (ix != Integer.MAX_VALUE && xj != Integer.MAX_VALUE) {
                            matrix[i][j] = Math.min(matrix[i][j], ix + xj);
                        }
                    }
                }
            }

            int maxTravelTime = 0;

            for (int i = 0; i < n; i++) {
                int travelTime = matrix[k - 1][i];

                if (travelTime == Integer.MAX_VALUE) {
                    return -1;
                }

                if (travelTime > maxTravelTime) {
                    maxTravelTime = travelTime;
                }
            }

            return maxTravelTime;
        }
    }
}

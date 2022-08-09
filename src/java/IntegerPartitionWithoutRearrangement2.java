public class IntegerPartitionWithoutRearrangement2 {
    private static int partition(int[] nums, int k) {
        final int n = nums.length;
        int[][] matrix = new int[n][k];
        int[][] path = new int[n][k];

        for (int i = 0; i < n; i++) {
            matrix[i][0] = (i == 0 ? 0 : matrix[i - 1][0]) + nums[i];
        }
        for (int j = 0; j < k; j++) {
            matrix[0][j] = nums[0];
        }

        for (int j = 1; j < k; j++) {
            for (int i = 1; i < n; i++) {
                int sum = 0;
                int min = Integer.MAX_VALUE;
                int parent = -1;

                for (int l = 0; l < i; l++) {
                    sum += nums[i - l];
                    int cost = Math.max(sum, matrix[i - l - 1][j - 1]);
                    if (cost < min) {
                        min = cost;
                        parent = l + 1;
                    }
                }

                matrix[i][j] = min;
                path[i][j] = parent;
            }
        }

        return matrix[n - 1][k - 1];
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int k = 3;
        int res = partition(nums, k);
        System.out.println(res);
    }
}

public class MaximumMonotoneSubsequence {
    private int mms(int[] nums, int i, int prev) {
        if (i >= nums.length || nums[i] <= prev) {
            return 0;
        }
        return Math.max(1 + mms(nums, i + 1, nums[i]), mms(nums, i + 1, Integer.MIN_VALUE));
    }

    private int mms(int[] nums) {
        return mms(nums, 0, Integer.MIN_VALUE);
    }

    private int mmsdp(int[] nums) {
        final int n = nums.length;
        int[] predecessor = new int[n];
        int[] starting = new int[n];

        for (int i = n - 1; i >= 0; i--) {
            int nextPredecessor = i == n - 1 ? 0 : predecessor[i + 1];
            int nextStarting = i == n - 1 ? 0 : starting[i + 1];

            starting[i] = Math.max(1 + nextPredecessor, nextStarting);

            if (i == 0 || nums[i] > nums[i - 1]) {
                predecessor[i] = starting[i];
            } else {
                predecessor[i] = 0;
            }
        }

        return starting[0];
    }

    public static void main(String[] args) {
        MaximumMonotoneSubsequence a = new MaximumMonotoneSubsequence();
        int[] seq = {10, 11, 12, 13, 14, 15, 1, 2, 3, 4};
        int res = a.mms(seq);
        int res2 = a.mmsdp(seq);
        System.out.println(res + "," + res2);
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Subsets {
    class Solution {
        public void computeSubsets(int[] nums, int step, Integer[] partial, int partialSize, List<List<Integer>> subsets) {
            if (step == nums.length) {
                subsets.add(Arrays.asList(Arrays.copyOf(partial, partialSize)));
                return;
            }

            partial[partialSize] = nums[step];
            computeSubsets(nums, step + 1, partial, partialSize + 1, subsets);
            computeSubsets(nums, step + 1, partial, partialSize, subsets);
        }

        public List<List<Integer>> subsets(int[] nums) {
            List<List<Integer>> subsets = new ArrayList<>((int) Math.pow(2, nums.length));
            computeSubsets(nums, 0, new Integer[nums.length], 0, subsets);
            return subsets;
        }
    }
}

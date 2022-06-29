import java.util.*;
import java.util.stream.Collectors;

public class FourSum {
    class Solution {
        private void recurse(int[] nums, int depth, int start, long sum, long target, LinkedList<Integer> trail, List<List<Integer>> solutions) {
            if (depth == 0) {
                if (sum == target) {
                    solutions.add(new ArrayList<>(trail));
                }
                return;
            }

            final long diff = target - sum;

            for (int i = start; i < nums.length; i++) {
                final int num = nums[i];
                final long min = (long) num * depth;

                if (min > diff) {
                    break;
                }

                if (i > start && num == nums[i - 1]) {
                    continue;
                }

                trail.addLast(num);
                recurse(nums, depth - 1, i + 1, sum + num, target, trail, solutions);
                trail.removeLast();
            }
        }

        public List<List<Integer>> fourSum(int[] nums, int target) {
            final int depth = 4;
            if (nums == null || nums.length < depth) {
                return Collections.emptyList();
            }
            List<List<Integer>> solutions = new ArrayList<>();
            Arrays.sort(nums);
            recurse(nums, depth, 0, 0, target, new LinkedList<>(), solutions);
            return solutions;
        }
    }

    public static void main(String[] args) {
        FourSum.Solution sol = new FourSum().new Solution();
        System.out.println(sol.fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0));
        System.out.println(sol.fourSum(new int[]{2, 2, 2, 2, 2}, 8));
        System.out.println(sol.fourSum(new int[]{0, 0, 0, -1000000000, -1000000000, -1000000000, -1000000000}, -1000000000));
        System.out.println(sol.fourSum(new int[]{-1000000000, -1000000000, 1000000000, -1000000000, -1000000000}, 294967296));
    }
}

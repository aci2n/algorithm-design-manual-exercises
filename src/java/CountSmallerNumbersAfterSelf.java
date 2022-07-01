
import java.util.List;
import java.util.PriorityQueue;

public class CountSmallerNumbersAfterSelf {
    class Solution {
        public List<Integer> countSmaller(int[] nums) {
            final PriorityQueue<Integer> heap = new PriorityQueue<>(nums.length);

            for (int i = nums.length - 1; i > 0; i--) {
                final int num = nums[i];
                int count = heap.size();

                for (int e : heap) {
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(1 >>> 1);
        System.out.println(0 >>> 1);
    }
}

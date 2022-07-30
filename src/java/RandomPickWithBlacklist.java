import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPickWithBlacklist {

    class Interval {
        final int start;
        final int end;
        final int offset;

        public Interval(int start, int end, int offset) {
            this.start = start;
            this.end = end;
            this.offset = offset;
        }
    }

    class Solution {
        private final List<Interval> intervals;
        private final int pool;

        public Solution(int n, int[] blacklist) {
            assert blacklist.length < n : "blacklist cannot be greater than n";

            Arrays.sort(blacklist);

            this.intervals = new ArrayList<>(blacklist.length + 1);
            this.pool = n - blacklist.length;

            for (int i = 0; i <= blacklist.length; i++) {
                int start = (i == 0 ? -1 : blacklist[i - 1]) + 1;
                int end = (i == blacklist.length ? n : blacklist[i]) - 1;

                if (end >= start) {
                    intervals.add(new Interval(start - i, end - i, i));
                }
            }
        }

        private int findOffset(int x) {
            int lo = 0;
            int hi = intervals.size() - 1;

            while (lo <= hi) {
                int mid = (lo + hi) / 2;
                Interval i = intervals.get(mid);

                if (x > i.end) {
                    lo = mid + 1;
                } else if (x < i.start) {
                    hi = mid - 1;
                } else {
                    return i.offset;
                }
            }

            return -1;
        }

        public int pick() {
            int random = ThreadLocalRandom.current().nextInt(0, pool);
            int offset = findOffset(random);
            assert offset != -1;
            return random + offset;
        }
    }

    public static void main(String[] args) {
        Solution sol = new RandomPickWithBlacklist().new Solution(7, new int[]{2,3,5});
        for (int i = 0; i < 20; i++) {
            System.out.println(sol.pick());
        }
    }

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(n, blacklist);
 * int param_1 = obj.pick();
 */
}

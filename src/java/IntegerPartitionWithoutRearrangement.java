import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IntegerPartitionWithoutRearrangement {
    private static int sum(int[] nums, int start, int end) {
        int sum = 0;
        for (int i = start; i < end; i++) {
            sum += nums[i];
        }
        return sum;
    }

    static class Partition {
        int start; // inclusive
        int end; // exclusive

        Partition(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private static String key(Partition[] partitions) {
        StringBuilder builder = new StringBuilder();
        for (Partition partition : partitions) {
            builder.append(partition.start).append(partition.end);
        }
        return builder.toString();
    }

    private static int partition(int[] nums, Partition[] partitions, Set<String> seen) {
        String key = key(partitions);
        if (seen.contains(key)) return Integer.MAX_VALUE;
        seen.add(key);

        final int k = partitions.length;
        int maxSum = Integer.MIN_VALUE;
        int maxPartition = -1;

        for (int i = 0; i < k; i++) {
            Partition p = partitions[i];
            int sum = sum(nums, p.start, p.end);
            if (sum > maxSum) {
                maxSum = sum;
                maxPartition = i;
            }
        }

        Partition curr = partitions[maxPartition];
        Partition prev = maxPartition > 0 ? partitions[maxPartition - 1] : null;
        Partition next = maxPartition < k - 1 ? partitions[maxPartition + 1] : null;
        int result = maxSum;

        if (prev != null) {
            prev.end++;
            curr.start++;
            result = Math.min(result, partition(nums, partitions, seen));
            prev.end--;
            curr.start--;
        }

        if (next != null) {
            next.start--;
            curr.end--;
            result = Math.min(result, partition(nums, partitions, seen));
            next.start++;
            curr.end++;
        }

        return result;
    }

    private static int partition(int[] nums, int k) {
        Partition[] partitions = new Partition[k];
        Arrays.setAll(partitions, i -> new Partition(nums.length, nums.length));
        partitions[0] = new Partition(0, nums.length);
        return partition(nums, partitions, new HashSet<>());
    }

    public static void main(String[] args) {
        int[] nums = {100, 200, 300, 400, 500, 600, 700, 800, 900};
        int k = 3;
        int res = partition(nums, k);
        System.out.println(res);
    }
}

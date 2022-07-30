import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class SubsetWithBitSet {
    private static void computeSubSets(int[] arr, int n, BitSet bs, List<int[]> results) {
        if (n == arr.length) {
            int[] sol = new int[bs.cardinality()];
            int solIndex = 0;
            for (int i = 0; i < n; i++) {
                if (bs.get(i)) {
                    sol[solIndex++] = arr[i];
                }
            }
            results.add(sol);
            return;
        }

        bs.set(n);
        computeSubSets(arr, n + 1, bs, results);
        bs.clear(n);
        computeSubSets(arr, n + 1, bs, results);
    }

    private static List<int[]> computeSubSets(int[] arr) {
        List<int[]> subSets = new ArrayList<>((int) Math.pow(2, arr.length));
        computeSubSets(arr, 0, new BitSet(arr.length), subSets);
        return subSets;
    }

    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 3};
        List<int[]> subSets = computeSubSets(arr);

        assert subSets.size() == Math.pow(2, arr.length);

        System.out.println("n: " + subSets.size());

        for (int[] subSet : subSets) {
            System.out.println(Arrays.toString(subSet));
        }
    }
}

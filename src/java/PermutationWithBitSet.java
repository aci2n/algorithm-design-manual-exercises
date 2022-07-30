import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class PermutationWithBitSet {
    private static void computePermutations(int[] arr, int[] sol, int n, BitSet bs, List<int[]> results) {
        if (n == arr.length) {
            results.add(Arrays.copyOf(sol, sol.length));
            return;
        }

        for (int i = 0; i < arr.length; i++) {
            if (bs.get(i)) continue;
            sol[n] = arr[i];
            bs.set(i);
            computePermutations(arr, sol, n + 1, bs, results);
            bs.clear(i);
        }
    }

    private static int factorial(int n) {
        int res = 1;
        while (n > 0) res *= n--;
        return res;
    }

    private static List<int[]> computePermutations(int[] arr) {
        List<int[]> permutations = new ArrayList<>(factorial(arr.length));
        computePermutations(arr, new int[arr.length], 0, new BitSet(arr.length), permutations);
        return permutations;
    }

    public static void main(String[] args) {
        int[] arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        List<int[]> permutations = computePermutations(arr);

        assert permutations.size() == factorial(arr.length);

        System.out.println("n: " + permutations.size());

        for (int[] permutation : permutations) {
            System.out.println(Arrays.toString(permutation));
        }
    }
}

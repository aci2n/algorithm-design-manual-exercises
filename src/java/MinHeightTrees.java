import java.util.*;
import java.lang.*;
import java.io.*;

public class MinHeightTrees {
    class Solution {
        public List<Integer> findMinHeightTrees(int n, int[][] edges) {
            List<Set<Integer>> adjacencyLists = new ArrayList<>(n);

            for (int i = 0; i < n; i++) {
                adjacencyLists.add(new HashSet<>());
            }

            for (int[] edge : edges) {
                final int x = edge[0];
                final int y = edge[1];
                assert x < n;
                assert y < n;
                adjacencyLists.get(x).add(y);
                adjacencyLists.get(y).add(x);
            }

            List<Integer> leaves = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (adjacencyLists.get(i).size() <= 1) {
                    leaves.add(i);
                }
            }

            while (n > 2) {
                List<Integer> newLeaves = new ArrayList<>();

                for (int leaf : leaves) {
                    int parent = adjacencyLists.get(leaf).stream().findFirst().orElseThrow();
                    if (!adjacencyLists.get(parent).remove(leaf)) {
                        throw new AssertionError();
                    }
                    if (adjacencyLists.get(parent).size() == 1) {
                        newLeaves.add(parent);
                    }
                }

                n -= leaves.size();
                leaves = newLeaves;
            }

            return leaves;
        }
    }
}

import java.util.*;

public class RedundantConnection {
    enum VertexStatus {
        UNDISCOVERED,
        DISCOVERED,
        PROCESSED;
    }

    class Solution {
        private int[] findBackEdge(List<Set<Integer>> adjacencyLists, int[] parents, VertexStatus[] statuses, int v) {
            statuses[v] = VertexStatus.DISCOVERED;

            for (int u : adjacencyLists.get(v)) {
                switch (statuses[u]) {
                    case UNDISCOVERED:
                        parents[u] = v;
                        int[] backEdge = findBackEdge(adjacencyLists, parents, statuses, u);
                        if (backEdge != null) {
                            return backEdge;
                        }
                        break;
                    case DISCOVERED:
                        if (parents[v] != u) {
                            return new int[]{v, u};
                        }
                        break;
                }
            }

            statuses[v] = VertexStatus.PROCESSED;
            return null;
        }

        private Set<Integer> findPath(int[] parents, int v, int u) {
            Set<Integer> path = new HashSet<>();
            path.add(u);
            while (v != u) {
                path.add(v);
                v = parents[v];
            }
            return path;
        }

        public int[] findRedundantConnection(int[][] edges) {
            final int n = edges.length;
            List<Set<Integer>> adjacencyLists = new ArrayList<>(n + 1);
            for (int i = 0; i <= n; i++) adjacencyLists.add(new HashSet<>());
            for (int[] edge : edges) {
                adjacencyLists.get(edge[0]).add(edge[1]);
                adjacencyLists.get(edge[1]).add(edge[0]);
            }
            int[] parents = new int[n + 1];
            VertexStatus[] statuses = new VertexStatus[n + 1];
            Arrays.fill(statuses, VertexStatus.UNDISCOVERED);
            int[] backEdge = findBackEdge(adjacencyLists, parents, statuses, 1);
            assert backEdge != null;
            Set<Integer> cyclePath = findPath(parents, backEdge[0], backEdge[1]);
            for (int i = edges.length - 1; i >= 0; i--) {
                int[] edge = edges[i];
                if (cyclePath.contains(edge[0]) && cyclePath.contains(edge[1])) {
                    return edge;
                }
            }
            throw new IllegalArgumentException("no cycles");
        }
    }

    public static void main(String[] args) {
//        int[][] edges = new int[][]{{1, 2}, {1, 3}, {2, 3}};
//        int[][] edges = new int[][]{{1, 3}, {3, 4}, {1, 5}, {3, 5}, {2, 3}};
        int[][] edges = {{2, 7}, {7, 8}, {3, 6}, {2, 5}, {6, 8}, {4, 8}, {2, 8}, {1, 8}, {7, 10}, {3, 9}};

        int[] redundantConnection = new RedundantConnection().new Solution().findRedundantConnection(edges);
        System.out.println(Arrays.toString(redundantConnection));
    }
}

import java.util.*;

public class Kruskal {
    static class UnionFind {
        final int[] parent;
        final int[] size;

        UnionFind(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            while (x != parent[x]) {
                x = parent[x];
            }
            return x;
        }

        void union(int x, int y) {
            int rx = find(x);
            int ry = find(y);

            if (rx == ry) {
                return;
            }

            if (size[rx] < size[ry]) {
                parent[rx] = ry;
                size[ry] += size[rx];
            } else {
                parent[ry] = rx;
                size[rx] += size[ry];
            }
        }
    }

    static class Edge implements Comparable<Edge> {
        final int x;
        final int y;
        final int weight;

        Edge(int x, int y, int weight) {
            this.x = x;
            this.y = y;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(weight, o.weight);
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "x=" + x +
                    ", y=" + y +
                    ", weight=" + weight +
                    '}';
        }
    }

    private List<Edge> kruskal(int n, List<Edge> edges) {
        PriorityQueue<Edge> queue = new PriorityQueue<>(edges);
        UnionFind uf = new UnionFind(n);
        List<Edge> mst = new ArrayList<>();

        while (mst.size() < n - 1 && !queue.isEmpty()) {
            final Edge minEdge = queue.remove();
            final int x = minEdge.x;
            final int y = minEdge.y;

            if (uf.find(x) != uf.find(y)) {
                mst.add(minEdge);
                uf.union(x, y);
            }
        }

        return mst;
    }

    private static Edge e(int x, int y, int weight) {
        return new Edge(x, y, weight);
    }

    public static void main(String[] args) {
        List<Edge> edges = List.of(
                e(0, 1, 5),
                e(0, 2, 7),
                e(0, 3, 12),
                e(1, 2, 9),
                e(2, 3, 4),
                e(1, 6, 7),
                e(2, 6, 4),
                e(2, 4, 3),
                e(3, 4, 7),
                e(6, 4, 2),
                e(4, 5, 2),
                e(6, 5, 5)
        );
        List<Edge> mst = new Kruskal().kruskal(7, edges);
        System.out.println(mst);
    }
}

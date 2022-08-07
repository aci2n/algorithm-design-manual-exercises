import java.util.*;

public class CheapestFlightKStops {
    class Edge {
        final int v;
        final int weight;

        Edge(int v, int weight) {
            this.v = v;
            this.weight = weight;
        }
    }

    class Graph {
        final List<List<Edge>> adjacencyLists;

        Graph(int n, int[][] edges, boolean invert) {
            List<List<Edge>> adjacencyLists = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                adjacencyLists.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                assert edge.length == 3;
                final int x;
                final int y;
                if (!invert) {
                    x = edge[0];
                    y = edge[1];
                } else {
                    x = edge[1];
                    y = edge[0];
                }
                adjacencyLists.get(x).add(new Edge(y, edge[2]));
            }
            this.adjacencyLists = Collections.unmodifiableList(adjacencyLists);
        }

        int vertices() {
            return adjacencyLists.size();
        }
    }

    class Solution {
        private int findCheapestPrice(Graph graph, int src, int dst, int hopsAvailable, int price, boolean[] discovered, int[] minHops, int best) {
            if (src == dst) {
                return price;
            }

            if (discovered[src] || minHops[src] > hopsAvailable || price >= best) {
                return Integer.MAX_VALUE;
            }

            int cheapestPrice = best;

            discovered[src] = true;
            for (Edge edge : graph.adjacencyLists.get(src)) {
                cheapestPrice = Math.min(
                        cheapestPrice,
                        findCheapestPrice(graph, edge.v, dst, hopsAvailable - 1, price + edge.weight, discovered, minHops, cheapestPrice));
            }
            discovered[src] = false;

            return cheapestPrice;
        }

        private int[] getMinHops(Graph graph, int s) {
            int[] minHops = new int[graph.vertices()];
            Queue<Integer> queue = new LinkedList<>();
            boolean[] discovered = new boolean[graph.vertices()];

            Arrays.fill(minHops, Integer.MAX_VALUE);

            queue.add(s);
            discovered[s] = true;
            minHops[s] = 0;

            while (!queue.isEmpty()) {
                int v = queue.remove();

                for (Edge edge : graph.adjacencyLists.get(v)) {
                    if (!discovered[edge.v]) {
                        discovered[edge.v] = true;
                        minHops[edge.v] = minHops[v] + 1;
                        queue.add(edge.v);
                    }
                }
            }

            return minHops;
        }

        public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
            int[] minHops = getMinHops(new Graph(n, flights, true), dst);
            int cheapestPrice = findCheapestPrice(new Graph(n, flights, false), src, dst, k + 1, 0, new boolean[n], minHops, Integer.MAX_VALUE);
            return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
        }
    }
}

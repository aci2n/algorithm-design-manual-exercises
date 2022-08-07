import java.util.*;

public class CheapestFlightKStopsBestFirstSearch {
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
            for (List<Edge> adjacencyList : adjacencyLists) {
                // sorting by weight allows pruning dead-ends faster by increasing chances to find low-weight solutions first
                adjacencyList.sort(Comparator.comparingInt(edge -> edge.weight));
            }
            this.adjacencyLists = Collections.unmodifiableList(adjacencyLists);
        }

        int vertices() {
            return adjacencyLists.size();
        }
    }

    class Solution {
        private int[] getMinPathSize(Graph graph, int s) {
            int[] minPathSize = new int[graph.vertices()];
            Queue<Integer> queue = new LinkedList<>();
            boolean[] discovered = new boolean[graph.vertices()];

            Arrays.fill(minPathSize, Integer.MAX_VALUE);

            queue.add(s);
            discovered[s] = true;
            minPathSize[s] = 1;

            while (!queue.isEmpty()) {
                int v = queue.remove();

                for (Edge edge : graph.adjacencyLists.get(v)) {
                    if (!discovered[edge.v]) {
                        discovered[edge.v] = true;
                        minPathSize[edge.v] = minPathSize[v] + 1;
                        queue.add(edge.v);
                    }
                }
            }

            return minPathSize;
        }

        public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
            int[] minPathSize = getMinPathSize(new Graph(n, flights, true), dst);
            Graph g = new Graph(n, flights, false);
            Queue<State> queue = new PriorityQueue<>(Comparator.comparingInt(res -> res.price));
            int cheapestPrice = Integer.MAX_VALUE;
            int maxPathSize = k + 2;

            queue.add(new State(0, new TreeSet<>(List.of(src))));

            while (!queue.isEmpty() && queue.peek().price < cheapestPrice) {
                State curr = queue.remove();
                SortedSet<Integer> currPath = curr.path;
                int currVertex = currPath.last();

                for (Edge edge : g.adjacencyLists.get(currVertex)) {
                    int nextVertex = edge.v;
                    int nextPrice = curr.price + edge.weight;

                    if (nextVertex == dst) {
                        cheapestPrice = Math.min(cheapestPrice, nextPrice);
                    } else if (!currPath.contains(nextVertex) && nextPrice < cheapestPrice && currPath.size() + minPathSize[nextVertex] <= maxPathSize) {
                        SortedSet<Integer> nextPath = new TreeSet<>(currPath);
                        nextPath.add(nextVertex);
                        queue.add(new State(nextPrice, nextPath));
                    }
                }
            }

            return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
        }
    }

    class State {
        final int price;
        final SortedSet<Integer> path;

        State(int price, SortedSet<Integer> path) {
            this.price = price;
            this.path = path;
        }
    }
}

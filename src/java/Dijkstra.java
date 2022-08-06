import java.util.*;

public class Dijkstra {
    static class Edge {
        final int x;
        final int y;
        final int weight;

        Edge(int x, int y, int weight) {
            this.x = x;
            this.y = y;
            this.weight = weight;
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

    private List<List<Edge>> toAdjacencyLists(int n, List<Edge> edges) {
        List<List<Edge>> adjacencyLists = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adjacencyLists.add(new ArrayList<>());
        }
        for (Edge edge : edges) {
            adjacencyLists.get(edge.x).add(edge);
        }
        return adjacencyLists;
    }

    private int pickNext(int[] array, boolean[] known) {
        int minVal = Integer.MAX_VALUE;
        int minIdx = -1;
        for (int i = 0; i < array.length; i++) {
            if (!known[i] && array[i] < minVal) {
                minVal = array[i];
                minIdx = i;
            }
        }
        return minIdx;
    }

    static class DijkstraResult {
        final int distance;
        final List<Integer> path;

        DijkstraResult(int distance, List<Integer> path) {
            this.distance = distance;
            this.path = path;
        }

        @Override
        public String toString() {
            return "DijkstraResult{" +
                    "distance=" + distance +
                    ", path=" + path +
                    '}';
        }
    }

    private List<Integer> buildPath(int[] parent, int s, int t) {
        LinkedList<Integer> path = new LinkedList<>();
        while (t != parent[s]) {
            path.addFirst(t);
            t = parent[t];
        }
        return path;
    }

    private DijkstraResult dijkstra(int n, List<Edge> edges, int s, int t) {
        List<List<Edge>> adjacencyLists = toAdjacencyLists(n, edges);
        boolean[] known = new boolean[n];
        int[] parent = new int[n];
        int[] distance = new int[n];
        int next = s;

        for (int i = 0; i < n; i++) {
            known[i] = false;
            parent[i] = -1;
            distance[i] = Integer.MAX_VALUE;
        }

        distance[s] = 0;

        while (next != t) {
            int dist = distance[next];

            for (Edge edge : adjacencyLists.get(next)) {
                int candidate = dist + edge.weight;

                if (candidate < distance[edge.y]) {
                    parent[edge.y] = next;
                    distance[edge.y] = candidate;
                }
            }

            known[next] = true;
            next = pickNext(distance, known);
        }

        return new DijkstraResult(distance[t], buildPath(parent, s, t));
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
        DijkstraResult result = new Dijkstra().dijkstra(7, edges, 0, 3);
        System.out.println(result);
    }
}

import java.util.*;

public class NetworkFlow {

    private static Edge e(int x, int y, int cap) {
        return new Edge(x, y, cap);
    }

    public static void main(String[] args) {
        NetworkFlow nf = new NetworkFlow();
        List<Edge> edges = List.of(
                e(0, 1, 5),
                e(0, 2, 12),
                e(1, 3, 9),
                e(1, 4, 7),
                e(2, 3, 4),
                e(2, 5, 7),
                e(3, 4, 3),
                e(3, 5, 3),
                e(4, 6, 5),
                e(5, 6, 2)
        );
        int n = 7;
        var res = nf.networkFlow(n, edges, 0, 6);

        for (int i = 0; i < n; i++) {
            System.out.println("Vertex " + i);
            System.out.println(res.get(i));
        }
    }

    private List<List<FlowEdge>> toAdjacencyLists(int n, List<Edge> edges) {
        List<List<FlowEdge>> adjacencyLists = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adjacencyLists.add(new ArrayList<>());
        }
        for (Edge edge : edges) {
            adjacencyLists.get(edge.x).add(new FlowEdge(edge.y, 0, edge.capacity));
            adjacencyLists.get(edge.y).add(new FlowEdge(edge.x, 0, 0));
        }
        return adjacencyLists;
    }

    private List<Integer> findPath(List<List<FlowEdge>> adjacencyLists, int s, int t) {
        final int n = adjacencyLists.size();
        Queue<Integer> queue = new LinkedList<>();
        int[] parents = new int[n];
        boolean[] discovered = new boolean[n];

        queue.add(s);
        discovered[s] = true;
        Arrays.fill(parents, -1);

        while (!queue.isEmpty()) {
            int v = queue.remove();

            for (FlowEdge edge : adjacencyLists.get(v)) {
                int u = edge.target;

                if (!discovered[u] && edge.residual > 0) {
                    parents[u] = v;
                    if (u == t) {
                        return buildPath(parents, s, u);
                    }
                    discovered[u] = true;
                    queue.add(u);
                }
            }
        }

        return List.of(); // no path from s to t
    }

    private List<Integer> buildPath(int[] parents, int s, int t) {
        LinkedList<Integer> path = new LinkedList<>();
        while (t != parents[s]) {
            path.addFirst(t);
            t = parents[t];
        }
        return path;
    }

    private FlowEdge findEdge(List<List<FlowEdge>> adjacencyLists, int i, int j) {
        return adjacencyLists.get(i).stream().filter(e -> e.target == j).findFirst().orElseThrow();
    }

    private int pathVolume(List<List<FlowEdge>> adjacencyLists, List<Integer> path) {
        assert !path.isEmpty();
        int volume = Integer.MAX_VALUE;
        for (int i = 0; i < path.size() - 1; i++) {
            int curr = path.get(i);
            int next = path.get(i + 1);
            volume = Math.min(findEdge(adjacencyLists, curr, next).residual, volume);
        }
        return volume;
    }

    private List<List<FlowEdge>> networkFlow(int n, List<Edge> edges, int s, int t) {
        List<List<FlowEdge>> adjacencyLists = toAdjacencyLists(n, edges);

        while (true) {
            List<Integer> path = findPath(adjacencyLists, s, t);
            if (path.isEmpty()) break;
            int volume = pathVolume(adjacencyLists, path);
            assert volume > 0;
            augmentPath(adjacencyLists, path, volume);
        }

        return adjacencyLists;
    }

    private void augmentPath(List<List<FlowEdge>> adjacencyLists, List<Integer> path, int volume) {
        for (int i = 0; i < path.size() - 1; i++) {
            int curr = path.get(i);
            int next = path.get(i + 1);
            FlowEdge straight = findEdge(adjacencyLists, curr, next);
            FlowEdge reverse = findEdge(adjacencyLists, next, curr);
            straight.flow += volume;
            straight.residual -= volume;
            reverse.residual += volume;
        }
    }

    static class Edge {
        final int x;
        final int y;
        final int capacity;

        Edge(int x, int y, int capacity) {
            this.x = x;
            this.y = y;
            this.capacity = capacity;
        }
    }

    static class FlowEdge {
        final int target;
        int flow;
        int residual;

        FlowEdge(int target, int flow, int residual) {
            this.target = target;
            this.flow = flow;
            this.residual = residual;
        }

        @Override
        public String toString() {
            return "{" +
                    "target=" + target +
                    ", flow=" + flow +
                    ", residual=" + residual +
                    '}';
        }
    }
}

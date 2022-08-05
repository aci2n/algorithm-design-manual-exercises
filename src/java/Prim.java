import java.util.*;

public class Prim {
    static class Edge implements Comparable<Edge> {
        final int target;
        final int weight;

        public Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return target == edge.target && weight == edge.weight;
        }

        @Override
        public int hashCode() {
            return Objects.hash(target, weight);
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(weight, o.weight);
        }
    }

    int prim(List<Set<Edge>> adjacencyLists) {
        final int n = adjacencyLists.size();
        final boolean[] inTree = new boolean[n];
        final PriorityQueue<Edge> candidates = new PriorityQueue<>();
        int treeSize = 0;
        int treeWeight = 0;

        candidates.add(new Edge(0, 0));

        while (treeSize < n) {
            Edge minEdge;

            do {
                minEdge = candidates.remove();
            } while (inTree[minEdge.target]);

            inTree[minEdge.target] = true;
            treeSize++;
            treeWeight += minEdge.weight;

            for (Edge adjEdge : adjacencyLists.get(minEdge.target)) {
                if (!inTree[adjEdge.target]) {
                    candidates.add(adjEdge);
                }
            }
        }

        return treeWeight;
    }

    private static Edge edge(int target, int weight) {
        return new Edge(target, weight);
    }

    public static void main(String[] args) {
        List<Set<Edge>> edges = List.of(
                Set.of(edge(1, 5), edge(2, 7), edge(3, 12)),
                Set.of(edge(0, 5), edge(2, 9), edge(6, 7)),
                Set.of(edge(1, 9), edge(0, 7), edge(3, 4), edge(6, 4), edge(4, 3)),
                Set.of(edge(2, 4), edge(0, 12), edge(4, 7)),
                Set.of(edge(6, 2), edge(3, 7), edge(2, 3), edge(5, 2)),
                Set.of(edge(4, 2), edge(6, 5)),
                Set.of(edge(1, 7), edge(2, 4), edge(5, 5), edge(4, 2)));

        int mstWeight = new Prim().prim(edges);
        System.out.println(mstWeight);
    }
}

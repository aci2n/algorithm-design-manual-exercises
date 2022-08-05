import java.util.*;

public class CourseSchedule {
    enum VertexStatus {
        UNDISCOVERED,
        DISCOVERED,
        PROCESSED;
    }

    static class DFSState {
        final List<Set<Integer>> adjacencyLists;
        final int[] parent;
        final VertexStatus[] status;
        int time;
        final int[] entryTime;
        final int[] exitTime;

        DFSState(int n, int[][] edges) {
            adjacencyLists = new ArrayList<>(n);
            status = new VertexStatus[n];
            parent = new int[n];
            time = 0;
            entryTime = new int[n];
            exitTime = new int[n];

            for (int i = 0; i < n; i++) {
                adjacencyLists.add(new HashSet<>());
                status[i] = VertexStatus.UNDISCOVERED;
                parent[i] = -1;
                entryTime[i] = -1;
                exitTime[i] = -1;
            }

            for (int[] edge : edges) {
                adjacencyLists.get(edge[1]).add(edge[0]);
            }
        }
    }

    class Solution {
        boolean canFinish(DFSState state, int v) {
            state.status[v] = VertexStatus.DISCOVERED;
            state.entryTime[v] = state.time++;

            for (int u : state.adjacencyLists.get(v)) {
                switch (state.status[u]) {
                    case UNDISCOVERED:
                        // tree-edge
                        state.parent[u] = v;
                        if (!canFinish(state, u)) {
                            return false;
                        }
                        break;
                    case DISCOVERED:
                        // back-edge
                        return false;
                    case PROCESSED:
                        // cross-edge or forward-edge
                        break;
                }
            }

            state.status[v] = VertexStatus.PROCESSED;
            state.exitTime[v] = state.time++;

            return true;
        }

        public boolean canFinish(int numCourses, int[][] prerequisites) {
            DFSState state = new DFSState(numCourses, prerequisites);
            for (int i = 0; i < numCourses; i++) {
                if (state.status[i] == VertexStatus.UNDISCOVERED && !canFinish(state, i)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        Solution sol = new CourseSchedule().new Solution();
        System.out.println(sol.canFinish(20, new int[][]{{10, 0}, {5, 5}}));
    }
}

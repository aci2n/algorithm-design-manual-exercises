import java.util.Comparator;
import java.util.PriorityQueue;

public class Temperatures {
    public int[] dailyTemperatures(int[] temperatures) {
        final int n = temperatures.length;
        final PriorityQueue<Integer> heap = new PriorityQueue<>(n, Comparator.comparingInt(a -> temperatures[a]));
        final int[] answer = new int[n];

        for (int i = 0; i < n; i++) {
            final int temperature = temperatures[i];

            while (!heap.isEmpty() && temperatures[heap.peek()] < temperature) {
                final int index = heap.remove();
                answer[index] = i - index;
            }

            heap.add(i);
        }

        return answer;
    }
}

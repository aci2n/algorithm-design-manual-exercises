import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPointNonOverlappingTriangles {
    class Rectangle {
        final int sizeX;
        final int lowX;
        final int lowY;
        final int start;
        final int end;

        Rectangle(int[] coords, int start) {
            int sizeX = coords[2] - coords[0] + 1;
            int sizeY = coords[3] - coords[1] + 1;
            int size = sizeX * sizeY;

            this.start = start;
            this.end = start + size - 1;
            this.sizeX = sizeX;
            this.lowX = coords[0];
            this.lowY = coords[1];
        }
    }

    class Solution {
        private final List<Rectangle> rectangles;
        private final int pool;

        public Solution(int[][] rects) {
            assert rects.length > 0;

            List<Rectangle> rectangles = new ArrayList<>(rects.length);
            int pool = 0;

            for (int[] coords : rects) {
                Rectangle rectangle = new Rectangle(coords, pool);
                rectangles.add(rectangle);
                pool = rectangle.end + 1;
            }

            this.rectangles = rectangles;
            this.pool = pool;
        }

        private Rectangle findRectangle(int x) {
            int lo = 0;
            int hi = rectangles.size() - 1;

            while (lo <= hi) {
                int mid = (lo + hi) / 2;
                Rectangle rectangle = rectangles.get(mid);

                if (rectangle.start > x) {
                    hi = mid - 1;
                } else if (rectangle.end < x) {
                    lo = mid + 1;
                } else {
                    return rectangle;
                }
            }

            throw new AssertionError();
        }

        public int[] pick() {
            final int rand = ThreadLocalRandom.current().nextInt(0, pool);
            final Rectangle rectangle = findRectangle(rand);
            final int n = rand - rectangle.start;
            final int x = n % rectangle.sizeX;
            final int y = n / rectangle.sizeX;

            return new int[]{rectangle.lowX + x, rectangle.lowY + y};
        }
    }

    public static void main(String[] args) {
        int[][] rects = {{-2, -1, 0, 1}, {1, 0, 2, 3}};
        Solution sol = new RandomPointNonOverlappingTriangles().new Solution(rects);

        for (int i = 0; i < 20; i++) {
            int[] point = sol.pick();
            System.out.printf("[%d, %d]%n", point[0], point[1]);
        }
    }
}

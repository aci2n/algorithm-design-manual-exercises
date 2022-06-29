import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class MatrixOperations {
    private static int[][] multiply(int[][] a, int[][] b) {
        // a: x rows, y columns
        // b: y rows, z columns
        // c: x rows, z columns
        final int x = a.length;
        final int y = a[0].length;
        final int z = b[0].length;

        if (y != b.length) {
            throw new IllegalArgumentException("matrices must be a[x][y], b[y][z]");
        }

        final int[][] c = new int[x][z];

        for (int i = 0; i < x; i++) { // for each row in a
            for (int j = 0; j < z; j++) { // row each column in b
                c[i][j] = 0;
                for (int k = 0; k < y; k++) { // for each column in a==row in b
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return c;
    }

    private static int minor(int[][] matrix, int skipRow, int skipCol) {
        final int size = matrix.length - 1;

        if (size != matrix[0].length - 1) {
            throw new IllegalArgumentException("m must be square");
        }

        int minor = 0;

        for (int i = 0; i < size; i++) {
            int plus = 1;
            int minus = 1;

            for (int j = 0; j < size; j++) {
                final int colPlus = (i + j) % size;
                final int colMinus = (i + size - j) % size;

                plus *= matrix[j >= skipRow ? j + 1 : j][colPlus >= skipCol ? colPlus + 1 : colPlus];
                minus *= matrix[j >= skipRow ? j + 1 : j][colMinus >= skipCol ? colMinus + 1 : colMinus];
            }

            minor = minor + plus - minus;
        }

        return minor;
    }

    private static int[][] minors(int[][] matrix) {
        final int size = matrix.length;

        if (size != matrix[0].length) {
            throw new IllegalArgumentException("matrix must be square");
        }

        final int[][] minors = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                minors[i][j] = minor(matrix, i, j);
            }
        }

        return minors;
    }

    private int[][] invert(int[][] matrix) {
        final int size = matrix.length;

        if (size != matrix[0].length) {
            throw new IllegalArgumentException("matrix must be square");
        }

        final int[][] minors = minors(matrix);

        return matrix;
    }

    private static void write(int[][] m, Appendable a) throws IOException {
        final int x = m.length;
        final int y = m[0].length;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                a.append(String.valueOf(m[i][j])).append('\t');
            }
            a.append('\n');
        }
    }

    public static void main(String[] args) throws IOException {
        int[][] a = {
                {1, 2, 13, 19},
                {3, 4, 14, 20},
                {5, 6, 15, 21}
        };
        int[][] b = {
                {7, 8, 9},
                {10, 11, 12},
                {16, 17, 18},
                {22, 23, 24}
        };
        int[][] c = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        System.out.println("axb:");
        write(MatrixOperations.multiply(a, b), System.out);
        System.out.println("bxa:");
        write(MatrixOperations.multiply(b, a), System.out);

        System.out.println("minors(c):");
        write(MatrixOperations.minors(c), System.out);
    }
}

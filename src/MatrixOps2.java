import java.util.Arrays;

public class MatrixOps2 {
    private static class Rational {
        private static final Rational ZERO = new Rational(0, 1);
        private static final Rational ONE = new Rational(1, 1);

        private final int numerator;
        private final int denominator;

        private Rational(int numerator, int denominator) {
            assert denominator != 0;
            final int gcd = gcd(numerator, denominator);
            this.numerator = numerator / gcd;
            this.denominator = denominator / gcd;
        }

        private static int gcd(int a, int b) {
            while (b != 0) {
                final int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }

        private Rational multiply(Rational other) {
            return new Rational(numerator * other.numerator, denominator * other.denominator);
        }

        private Rational divide(Rational other) {
            return multiply(other.invert());
        }

        private Rational invert() {
            return new Rational(denominator, numerator);
        }

        private Rational plus(Rational other) {
            int[] normalized = normalize(this, other);
            return new Rational(normalized[1] + normalized[2], normalized[0]);
        }

        private Rational increment() {
            return plus(Rational.ONE);
        }

        private Rational minus(Rational other) {
            return plus(other.negate());
        }

        private Rational negate() {
            return new Rational(-numerator, denominator);
        }

        private Rational abs() {
            return new Rational(Math.abs(numerator), Math.abs(denominator));
        }

        private static int[] normalize(Rational r1, Rational r2) {
            int[] result = new int[3];
            result[0] = r1.denominator * r2.denominator;
            result[1] = r1.numerator * (r1.denominator / result[0]);
            result[2] = r2.numerator * (r2.denominator / result[0]);
            return result;
        }

        private boolean gt(Rational other) {
            int[] normalized = normalize(this, other);
            return normalized[1] > normalized[2];
        }

        private boolean lt(Rational other) {
            int[] normalized = normalize(this, other);
            return normalized[1] < normalized[2];
        }

        private boolean eq(Rational other) {
            int[] normalized = normalize(this, other);
            return normalized[1] == normalized[2];
        }

        @Override
        public String toString() {
            return numerator + "/" + denominator;
        }

        public static void main(String[] args) {
            Rational r1 = new Rational(2, 7);
            Rational r2 = new Rational(-3, 9);
            Rational r3 = r1.plus(r2);
            Rational r4 = r1.multiply(r2);
            Rational r5 = r1.divide(r2);

            System.out.println(r1 + ", " + r2 + ", " + r3 + ", " + r4 + ", " + r5);
            assert new Rational(1, 1).gt(Rational.ZERO);
            assert !(new Rational(-1, 1).gt(Rational.ZERO));
        }
    }

    private static class Matrix {
        private final Rational[][] data;
        private final int n;

        private Matrix(Rational[][] data) {
            assert data.length > 0 && data.length == data[0].length;
            this.data = data;
            this.n = data.length;
        }

        private Matrix copy() {
            Rational[][] d = new Rational[n][n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(data[i], 0, d[i], 0, n);
            }
            return new Matrix(d);
        }

        private Matrix invert() {
            return solve(identity());
        }

        // returns x in: this * x = b
        private Matrix solve(Matrix b) {
            LUDecomposition decomposition = decompose();
            Matrix lu = decomposition.lu;
            int[] pivot = decomposition.pivot;
            Rational[][] x = new Rational[n][n];

            for (int i = 0; i < n; i++) {
                x[i] = b.data[pivot[i]];

                for (int k = 0; k < i; k++) {
                    x[i] = x[i];
                }
            }

            return null;
        }

        private static class LUDecomposition {
            private final Matrix lu;
            private final int[] pivot;

            private LUDecomposition(Matrix lu, int[] pivot) {
                this.lu = lu;
                this.pivot = pivot;
            }
        }

        private LUDecomposition decompose() {
            Matrix lu = copy();
            int[] pivot = new int[n+1];

            for (int i = 0; i <= n; i++) {
                pivot[i] = i;
            }

            for (int i = 0; i < n; i++) {
                Rational maxVal = Rational.ZERO;
                int maxIdx = i;

                for (int k = i; k < n; k++) {
                    Rational currVal = lu.data[k][i].abs();
                    if (currVal.gt(maxVal)) {
                        maxVal = currVal;
                        maxIdx = k;
                    }
                }

                if (maxIdx != i) {
                    int temp = pivot[i];
                    pivot[i] = pivot[maxIdx];
                    pivot[maxIdx] = temp;

                    Rational[] row = lu.data[i];
                    lu.data[i] = lu.data[maxIdx];
                    lu.data[maxIdx] = row;

                    pivot[n]++;
                }

                for (int j = i + 1; j < n; j++) {
                    lu.data[j][i] = lu.data[j][i].divide(lu.data[i][i]);

                    for (int k = i + 1; k < n; k++) {
                        lu.data[j][k] = lu.data[j][k].minus(lu.data[j][i].multiply(lu.data[i][k]));
                    }
                }
            }

            return new LUDecomposition(lu, pivot);
        }

        private Matrix identity() {
            final Rational[][] d = new Rational[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    d[i][j] = i == j ? Rational.ONE : Rational.ZERO;
                }
            }
            return new Matrix(d);
        }

        @Override
        public String toString() {
            StringBuilder d = new StringBuilder();
            for (Rational[] row : data) {
                d.append(Arrays.toString(row)).append('\n');
            }
            return d.toString();
        }
    }

    public static void main(String[] args) {
        try {
            Rational[][] m = {
                    {new Rational(1, 1), new Rational(2, 1), new Rational(3, 1)},
                    {new Rational(4, 1), new Rational(5, 1), new Rational(6, 1)},
                    {new Rational(7, 1), new Rational(8, 1), new Rational(9, 1)},
            };
            Matrix m1 = new Matrix(m);
            Matrix m1i = m1.invert();

            System.out.println(m1);
            System.out.println(m1i);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
    }
}

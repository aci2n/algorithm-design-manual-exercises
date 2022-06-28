import java.util.Arrays;

public class MatrixOps {
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

        private Rational minus(Rational other) {
            return plus(other.negate());
        }

        private Rational negate() {
            return new Rational(-numerator, denominator);
        }

        private int sign() {
            return (int) (Math.signum(numerator) * Math.signum(denominator));
        }

        private Rational abs() {
            return sign() == 1 ? this : new Rational(-numerator, denominator);
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
        private final int rows;
        private final int columns;

        private Matrix(Rational[][] data) {
            this.data = data;
            this.rows = data.length;
            this.columns = data[0].length;
        }

        private Matrix invert() {
            return new LUDecomposition(this).solve(identity());
        }

        private Matrix identity() {
            final Rational[][] d = new Rational[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    d[i][j] = i == j ? Rational.ONE : Rational.ZERO;
                }
            }
            return new Matrix(d);
        }

        private Rational[][] copyData() {
            Rational[][] copy = new Rational[rows][columns];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(data[i], 0, copy[i], 0, columns);
            }
            return copy;
        }

        public Matrix getMatrix(int[] r, int j0, int j1) {
            Rational[][] d = new Rational[r.length][j1 - j0 + 1];
            for (int i = 0; i < r.length; i++) {
                for (int j = j0; j <= j1; j++) {
                    d[i][j - j0] = data[r[i]][j];
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

    private static class LUDecomposition {
        final private Rational[][] LU;
        final private int rows;
        final private int columns;
        final private int[] piv;
        private int pivsign;

        public LUDecomposition(Matrix matrix) {

            // Use a "left-looking", dot-product, Crout/Doolittle algorithm.

            LU = matrix.copyData();
            rows = matrix.rows;
            columns = matrix.columns;
            piv = new int[rows];
            for (int i = 0; i < rows; i++) {
                piv[i] = i;
            }
            pivsign = 1;
            Rational[] LUrowi;
            Rational[] LUcolj = new Rational[rows];

            // Outer loop.

            for (int j = 0; j < columns; j++) {

                // Make a copy of the j-th column to localize references.

                for (int i = 0; i < rows; i++) {
                    LUcolj[i] = LU[i][j];
                }

                // Apply previous transformations.

                for (int i = 0; i < rows; i++) {
                    LUrowi = LU[i];

                    // Most of the time is spent in the following dot product.

                    int kmax = Math.min(i, j);
                    Rational s = Rational.ZERO;

                    for (int k = 0; k < kmax; k++) {
                        s = s.plus(LUrowi[j].multiply(LUcolj[k]));
                    }

                    LUcolj[i] = LUcolj[i].minus(s);
                    LUrowi[j] = LUcolj[i];
                }

                // Find pivot and exchange if necessary.

                int p = j;
                for (int i = j + 1; i < rows; i++) {
                    if (LUcolj[i].abs().gt(LUcolj[p].abs())) {
                        p = i;
                    }
                }
                if (p != j) {
                    for (int k = 0; k < columns; k++) {
                        Rational t = LU[p][k];
                        LU[p][k] = LU[j][k];
                        LU[j][k] = t;
                    }
                    int k = piv[p];
                    piv[p] = piv[j];
                    piv[j] = k;
                    pivsign = -pivsign;
                }

                // Compute multipliers.

                if (j < rows & !LU[j][j].eq(Rational.ZERO)) {
                    for (int i = j + 1; i < rows; i++) {
                        LU[i][j] = LU[i][j].divide(LU[j][j]);
                    }
                }
            }
        }

        public boolean isNonsingular () {
            for (int j = 0; j < rows; j++) {
                if (LU[j][j].eq(Rational.ZERO))
                    return false;
            }
            return true;
        }

        private Matrix solve(Matrix other) {
            if (other.rows != rows) {
                throw new IllegalArgumentException("Matrix row dimensions must agree.");
            }
            if (!isNonsingular()) {
                throw new RuntimeException("Matrix is singular.");
            }

            // Copy right hand side with pivoting
            int otherColumns = other.columns;
            Matrix Xmat = other.getMatrix(piv, 0, otherColumns - 1);
            Rational[][] X = Xmat.data;

            // Solve L*Y = B(piv,:)
            for (int k = 0; k < columns; k++) {
                for (int i = k + 1; i < columns; i++) {
                    for (int j = 0; j < otherColumns; j++) {
                        X[i][j] = X[i][j].minus(X[k][j].multiply(LU[i][k]));
                    }
                }
            }
            // Solve U*X = Y;
            for (int k = columns - 1; k >= 0; k--) {
                for (int j = 0; j < otherColumns; j++) {
                    X[k][j] = X[k][j].divide(LU[k][k]);
                }
                for (int i = 0; i < k; i++) {
                    for (int j = 0; j < otherColumns; j++) {
                        X[i][j] = X[i][j].minus(X[k][j].multiply(LU[i][k]));
                    }
                }
            }
            return Xmat;
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

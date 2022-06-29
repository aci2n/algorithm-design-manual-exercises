/*
Doomsday Fuel
=============

Making fuel for the LAMBCHOP's reactor core is a tricky process because of the exotic matter involved. It starts as raw ore, then during processing, begins randomly changing between
forms, eventually reaching a stable form. There may be multiple stable forms that a sample could ultimately reach, not all of which are useful as fuel.

Commander Lambda has tasked you to help the scientists increase fuel creation efficiency by predicting the end state of a given ore sample. You have carefully studied the different structures
that the ore can take and which transitions it undergoes. It appears that, while random, the probability of each structure transforming is fixed. That is, each time the ore is in 1 state, it
has the same probabilities of entering the next state (which might be the same state).  You have recorded the observed transitions in a matrix. The others in the lab have hypothesized more
exotic forms that the ore can become, but you haven't seen all of them.

Write a function solution(m) that takes an array of array of nonnegative ints representing how many times that state has gone to the next state and return an array of ints for each terminal
state giving the exact probabilities of each terminal state, represented as the numerator for each state, then the denominator for all of them at the end and in simplest form. The matrix is
at most 10 by 10. It is guaranteed that no matter which state the ore is in, there is a path from that state to a terminal state. That is, the processing will always eventually end in a
stable state. The ore starts in state 0. The denominator will fit within a signed 32-bit integer during the calculation, as long as the fraction is simplified regularly.

For example, consider the matrix m:
[
  [0,1,0,0,0,1],  # s0, the initial state, goes to s1 and s5 with equal probability
  [4,0,0,3,2,0],  # s1 can become s0, s3, or s4, but with different probabilities
  [0,0,0,0,0,0],  # s2 is terminal, and unreachable (never observed in practice)
  [0,0,0,0,0,0],  # s3 is terminal
  [0,0,0,0,0,0],  # s4 is terminal
  [0,0,0,0,0,0],  # s5 is terminal
]
So, we can consider different paths to terminal states, such as:
s0 -> s1 -> s3
s0 -> s1 -> s0 -> s1 -> s0 -> s1 -> s4
s0 -> s1 -> s0 -> s5
Tracing the probabilities of each, we find that
s2 has probability 0
s3 has probability 3/14
s4 has probability 1/7
s5 has probability 9/14
So, putting that together, and making a common denominator, gives an answer in the form of
[s2.numerator, s3.numerator, s4.numerator, s5.numerator, denominator] which is
[0, 3, 2, 9, 14]
 */
public class Four {
    private static class Rational {
        private static final Rational ZERO = new Rational(0, 0);
        private static final Rational ONE = new Rational(1, 1);

        private final int numerator;
        private final int denominator;

        private Rational(int numerator, int denominator) {
            final int gcd = gcd(numerator, denominator);
            this.numerator = numerator / gcd;
            this.denominator = denominator / gcd;
        }

        private static int gcd(int a, int b) {
            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }

        private static int lcm(int a, int b) {
            return (a * b) / gcd(a, b);
        }

        private Rational multiply(Rational other) {
            return new Rational(numerator * other.numerator, denominator * other.denominator);
        }

        private Rational sum(Rational other) {
            return new Rational(numerator + other.numerator, denominator + other.denominator);
        }

        private boolean equals(Rational other) {
            return numerator == other.numerator && denominator == other.denominator;
        }
    }

    private static Rational probFromAtoB(int[][] m, int[] denominators, int a, int b) {
        if (denominators[b] != 0) {
            throw new IllegalArgumentException("expected b to be terminal");
        }

        if (a == b) {
            return Rational.ONE;
        }

        if (denominators[a] == 0) { // a is terminal
            return Rational.ZERO;
        }

        final int[] state = m[a];
        Rational result = Rational.ZERO;

        for (int i = 0; i < state.length; i++) {
            final Rational probability = new Rational(state[i], denominators[a]);

            if (!probability.equals(Rational.ZERO)) {
                // RECURSES ENDLESSLY IN CYCLIC GRAPHS
                // HAHAHHAHAHAHAHAHAHAH
                result = result.sum(probability.multiply(probFromAtoB(m, denominators, i, b)));
            }
        }

        return result;
    }

    public static int[] solution(int[][] m) {
        final int[] denominators = new int[m.length];
        int terminal = 0;

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                denominators[i] += m[i][j];
            }
            if (denominators[i] == 0) {
                terminal++;
            }
        }

        final Rational[] result = new Rational[terminal + 1]; // +1 for denominator

        for (int i = 0, pos = 0; i < m.length; i++) {
            if (denominators[i] > 0) {
                result[pos++] = probFromAtoB(m, denominators, 0, i);
            }
        }

        return new int[]{};
    }

    public static void main(String[] args) {

    }
}
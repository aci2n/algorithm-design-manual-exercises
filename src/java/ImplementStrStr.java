public class ImplementStrStr {
    class Solution {

        private int pow(int base, int exp, int mod) {
            if (exp == 0) return 1;
            int x = pow(base, exp / 2, mod);
            int res = (x * x) % mod;
            if (exp % 2 == 1) res = (res * base) % mod;
            return res;
        }

        private int hash(String s, int alpha, int j, int m, int mod) {
            int h = 0;
            for (int i = 0; i < m; i++) {
                h += (pow(alpha, m - (i + 1), mod) * s.charAt(j + i)) % mod;
            }
            return h % mod;
        }

        private int hashNext(String s, int alpha, int j, int m, int hj, int alpham1, int mod) {
            return (alpha * Math.floorMod(hj - (alpham1 * s.charAt(j)), mod) + s.charAt(j + m)) % mod;
        }

        public int strStr(String haystack, String needle) {
            final int hlen = haystack.length();
            final int nlen = needle.length();
            final int alpha = 256;
            final int mod = 1451;
            final int alpham1 = pow(alpha, nlen - 1, mod);
            final int limit = hlen - nlen;

            if (nlen > hlen) return -1;
            if (nlen == 0) return 0;

            int hh = hash(haystack, alpha, 0, nlen, mod);
            int hn = hash(needle, alpha, 0, nlen, mod);

            for (int i = 0; i <= limit; i++) {
                if (hh == hn) {
                    int j = 0;
                    for (; j < nlen; j++) {
                        if (haystack.charAt(i + j) != needle.charAt(j)) {
                            break;
                        }
                    }
                    if (j == nlen) {
                        return i;
                    }
                }
                if (i < limit) {
                    hh = hashNext(haystack, alpha, i, nlen, hh, alpham1, mod);
                }
            }

            return -1;
        }

    }

    public static void main(String[] args) {
        Solution sol = new ImplementStrStr().new Solution();
        String s = "hola como te va a mi todo trank";
        int alpha = 256;
        int mod = 1451;
        int m = 10;
        int alpham1 = sol.pow(alpha, m - 1, mod);

        assert sol.pow(2, 3, mod) == 8;
        assert sol.pow(5, 3, mod) == 125 % mod;
        assert sol.pow(5, 0, mod) == 1;
        assert sol.pow(5, 1, mod) == 5;
        assert sol.pow(5, 10, mod) == sol.pow(5, 10, Integer.MAX_VALUE) % mod;

//        System.out.println("[0,4]: " + sol.hash(s, alpha, 0, 4, mod));
        System.out.println("[0,m]: " + sol.hash(s, alpha, 0, m, mod));
        System.out.println("[1,m]: " + sol.hash(s, alpha, 1, m, mod));
        System.out.println("[2,m]: " + sol.hash(s, alpha, 2, m, mod));

        System.out.println("[1,m](alt): " + sol.hashNext(s, alpha, 0, m, sol.hash(s, alpha, 0, m, mod), alpham1, mod));
        System.out.println("[2,m](alt): " + sol.hashNext(s, alpha, 1, m, sol.hash(s, alpha, 1, m, mod), alpham1, mod));

        System.out.println(sol.strStr("hola", "ola"));
        System.out.println(sol.strStr("hola", "l"));

        System.out.println(sol.strStr("mississippi", "issip"));
    }
}

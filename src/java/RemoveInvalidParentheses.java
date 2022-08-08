import java.util.*;

class RemoveInvalidParentheses {

    class Candidate {
        final String s;
        final int ub;

        Candidate(String s) {
            int unmatchedOpening = 0;
            int unmatchedClosing = 0;

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '(':
                        unmatchedOpening++;
                        break;
                    case ')':
                        if (unmatchedOpening == 0) {
                            unmatchedClosing++;
                        } else {
                            unmatchedOpening--;
                        }
                        break;
                }
            }

            this.s = s;
            this.ub = s.length() - unmatchedOpening - unmatchedClosing;
        }
    }

    class Solution {
        public List<String> removeInvalidParentheses(String s) {
            Set<String> known = new HashSet<>();
            Queue<Candidate> queue = new PriorityQueue<>(Comparator.<Candidate>comparingInt(c -> c.ub).reversed());
            int maxLength = 0;
            List<String> solutions = new ArrayList<>();

            queue.add(new Candidate(s));

            while (!queue.isEmpty() && queue.peek().ub >= maxLength) {
                Candidate curr = queue.remove();

                if (curr.s.length() == curr.ub) {
                    if (curr.s.length() > maxLength) {
                        maxLength = curr.s.length();
                        solutions = new ArrayList<>();
                    }
                    solutions.add(curr.s);
                    continue;
                }

                for (int i = 0; i < curr.s.length(); i++) {
                    char c = curr.s.charAt(i);
                    if (c != '(' && c != ')') continue;
                    String next = curr.s.substring(0, i) + curr.s.substring(i + 1);
                    if (!known.contains(next)) {
                        known.add(next);
                        queue.add(new Candidate(next));
                    }
                }
            }

            return solutions;
        }
    }

    public static void main(String[] args) {
        String s = ")()))())))";
        List<String> solutions = new RemoveInvalidParentheses().new Solution().removeInvalidParentheses(s);
        System.out.println(solutions);
    }
}
import java.util.*;

class RemoveInvalidParentheses {

    class Candidate {
        final String s;
        final int ub;
        final boolean balanced;

        Candidate(String s) {
            boolean balanced = true;
            int balance = 0;

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '(':
                        balance++;
                        break;
                    case ')':
                        if (--balance < 0) {
                            balanced = false;
                        }
                        break;
                }
            }

            this.s = s;
            this.ub = s.length() - Math.abs(balance);
            this.balanced = balanced && balance == 0;
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

                if (curr.balanced) {
                    if (curr.s.length() > maxLength) {
                        maxLength = curr.s.length();
                        solutions = new ArrayList<>();
                    }
                    solutions.add(curr.s);
                    continue;
                }

                for (int i = 0; i < curr.s.length(); i++) {
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
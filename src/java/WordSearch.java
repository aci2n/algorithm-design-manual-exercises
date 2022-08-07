public class WordSearch {
    class Solution {
        public boolean exist(char[][] board, String word, boolean[][] used, int k, int x, int y) {
            if (k == word.length()) {
                return true;
            }

            final int n = board.length;
            final int m = board[0].length;

            if (k == 0) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (advance(board, word, used, k, i, j)) {
                            return true;
                        }
                    }
                }
            } else {
                for (int[] point : new int[][]{{x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}}) {
                    if (advance(board, word, used, k, point[0], point[1])) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean advance(char[][] board, String word, boolean[][] used, int k, int x, int y) {
            final int n = board.length;
            final int m = board[0].length;
            boolean exists = false;

            if (x >= 0 && x < n && y >= 0 && y < m && !used[x][y] && board[x][y] == word.charAt(k)) {
                used[x][y] = true;
                exists = exist(board, word, used, k + 1, x, y);
                used[x][y] = false;
            }

            return exists;
        }

        public boolean exist(char[][] board, String word) {
            final int n = board.length;
            final int m = board[0].length;
            return exist(board, word, new boolean[n][m], 0, 0, 0);
        }
    }
}

import java.util.Arrays;

public class MaximumProductOfSplittedBinaryTree {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "{" + val + "}";
        }
    }

    public class TreeNodeWithSum {
        final TreeNodeWithSum left;
        final TreeNodeWithSum right;

        final int val;
        final int sum;

        public TreeNodeWithSum(TreeNodeWithSum left, TreeNodeWithSum right, int val, int sum) {
            this.left = left;
            this.right = right;
            this.val = val;
            this.sum = sum;
        }
    }

    class Solution {
        private TreeNodeWithSum buildTreeWithSums(TreeNode root) {
            if (root == null) {
                return null;
            }
            int sum = root.val;
            TreeNodeWithSum left = buildTreeWithSums(root.left);
            TreeNodeWithSum right = buildTreeWithSums(root.right);
            if (left != null) sum += left.sum;
            if (right != null) sum += right.sum;
            return new TreeNodeWithSum(left, right, root.val, sum);
        }

        private long findHighestProduct(TreeNodeWithSum root, long parentSum) {
            if (root == null) {
                return 0;
            }

            long result = 0;

            if (root.right != null) {
                long sumLeftSide = parentSum + root.sum - root.right.sum;
                result = Math.max(result, Math.max(sumLeftSide * root.right.sum, findHighestProduct(root.right, sumLeftSide)));
            }

            if (root.left != null) {
                long sumRightSide = parentSum + root.sum - root.left.sum;
                result = Math.max(result, Math.max(sumRightSide * root.left.sum, findHighestProduct(root.left, sumRightSide)));
            }

            return result;
        }

        public int maxProduct(TreeNode root) {
            long product = findHighestProduct(buildTreeWithSums(root), 0);
            return (int) (product % (Math.pow(10, 9) + 7));
        }
    }

    public static void main(String[] args) {
        Solution sol = new MaximumProductOfSplittedBinaryTree().new Solution();
        TreeNode[] nodes = new TreeNode[6];
        Arrays.setAll(nodes, i -> new TreeNode(i + 1));
        nodes[0].left = nodes[1];
        nodes[0].right = nodes[2];
        nodes[1].left = nodes[3];
        nodes[1].right = nodes[4];
        nodes[2].left = nodes[5];
        int max = sol.maxProduct(nodes[0]);
        System.out.println(max);
    }
}

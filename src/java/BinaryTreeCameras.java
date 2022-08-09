import com.sun.source.tree.Tree;

public class BinaryTreeCameras {
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
    }

    class Solution {

        class Step {

            final int cost;
            final boolean marked;
            final boolean watched;

            Step(int cost, boolean marked, boolean watched) {
                this.cost = cost;
                this.marked = marked;
                this.watched = watched;
            }

            Step merge(Step o) {
                return new Step(cost + o.cost, marked || !o.watched, watched || o.marked);
            }
        }

        public Step minCameraCoverStep(TreeNode root) {
            Step step = new Step(0, false, false);

            if (root.left == null && root.right == null) {
                return step;
            }

            if (root.left != null) {
                step = step.merge(minCameraCoverStep(root.left));
            }

            if (root.right != null) {
                step = step.merge(minCameraCoverStep(root.right));
            }

            if (!step.watched || step.marked) {
                step = new Step(step.cost + 1, true, true);
            }

            return step;
        }

        public int minCameraCover(TreeNode root) {
            if (root.left == null && root.right == null) return 1;
            return minCameraCoverStep(root).cost;
        }
    }

    private static void printTree(TreeNode root, int depth) {
        System.out.print("\t".repeat(depth));
        if (root == null) {
            System.out.println("X");
        } else {
            System.out.println(root.val);
            printTree(root.left, depth + 1);
            printTree(root.right, depth + 1);
        }
    }

    public static void main(String[] args) {
        TreeNode f = new TreeNode(5, null, null);
        TreeNode g = new TreeNode(6, null, null);
        TreeNode j = new TreeNode(9, null, null);
        TreeNode i = new TreeNode(8, null, null);
        TreeNode h = new TreeNode(7, null, j);
        TreeNode e = new TreeNode(4, i, null);
        TreeNode d = new TreeNode(3, h, null);
        TreeNode b = new TreeNode(1, d, e);
        TreeNode c = new TreeNode(2, f, g);
        TreeNode a = new TreeNode(0, b, c);
//        printTree(a, 0);
        int minCover = new BinaryTreeCameras().new Solution().minCameraCover(a);
        System.out.println(minCover);
    }
}

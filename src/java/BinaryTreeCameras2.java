import java.util.*;

public class BinaryTreeCameras2 {
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

    class Solution {
        private List<TreeNode> collectChildren(TreeNode root) {
            List<TreeNode> children = new ArrayList<>(2);
            if (root != null) {
                if (root.left != null) {
                    children.add(root.left);
                }
                if (root.right != null) {
                    children.add(root.right);
                }
            }
            return children;
        }

        private List<TreeNode> collectGrandchildren(TreeNode root) {
            List<TreeNode> grandChildren = new ArrayList<>(4);
            if (root != null) {
                grandChildren.addAll(collectChildren(root.left));
                grandChildren.addAll(collectChildren(root.right));
            }
            return grandChildren;
        }

        private List<TreeNode> append(List<TreeNode> list, TreeNode node) {
            if (node != null) {
                list.add(node);
            }
            return list;
        }

        private int sumMinCovers(List<TreeNode> nodes, Map<TreeNode, Integer> cache) {
            int sum = 0;
            for (TreeNode node : nodes) {
                sum += minCameraCover(node, cache);
            }
            return sum;
        }

        private int minCameraCover(TreeNode root, Map<TreeNode, Integer> cache) {
            assert root != null;

            if (cache.containsKey(root)) {
                return cache.get(root);
            }

            int costRoot = sumMinCovers(collectGrandchildren(root), cache);
            int costLeft = sumMinCovers(append(collectGrandchildren(root.left), root.right), cache);
            int costRight = sumMinCovers(append(collectGrandchildren(root.right), root.left), cache);

            int cost = 1 + Math.min(costRoot, Math.min(costLeft, costRight));
            cache.put(root, cost);

            return cost;
        }

        public int minCameraCover(TreeNode root) {
            return minCameraCover(root, new IdentityHashMap<>());
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
        TreeNode[] nodes = new TreeNode[9];
        Arrays.setAll(nodes, i -> new TreeNode(i, null, null));
        nodes[0].right = nodes[1];
        nodes[1].right = nodes[2];
        nodes[2].right = nodes[3];
        nodes[3].right = nodes[4];
        nodes[4].left = nodes[5];
        nodes[4].right = nodes[6];
        nodes[6].left = nodes[7];
        nodes[6].right = nodes[8];
        int minCover = new BinaryTreeCameras2().new Solution().minCameraCover(nodes[0]);
        System.out.println(minCover);
    }

    public static void main2(String[] args) {
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
        int minCover = new BinaryTreeCameras2().new Solution().minCameraCover(a);
        System.out.println(minCover);
    }
}

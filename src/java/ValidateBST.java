public class ValidateBST {
    class Node {
        int data;
        Node left;
        Node right;
    }

    boolean check(Node root, Integer[] prev) {
        if (root == null) return true;
        if (!check(root.left, prev)) return false;
        if (prev[0] != null && prev[0] >= root.data) return false;
        prev[0] = root.data;
        return check(root.right, prev);
    }

    boolean checkBST(Node root) {
        return check(root, new Integer[1]);
    }
}

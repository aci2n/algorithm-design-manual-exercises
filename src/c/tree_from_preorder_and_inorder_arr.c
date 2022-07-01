/**
 * Definition for a binary tree node.
 * struct TreeNode {
 *     int val;
 *     struct TreeNode *left;
 *     struct TreeNode *right;
 * };
 */

#define MIN -3000
#define MAX 3000

typedef struct TreeNode tree;

void tree_insert(tree** t, int val) {
    tree* root = *t;
    
    if (!root) {
        *t = root = malloc(sizeof(tree));
        *root = (tree) { .val = val };
        return;
    }
    
    if (val < root->val) {
        tree_insert(&root->left, val);
    } else if (val > root->val) {
        tree_insert(&root->right, val);
    }
}

void tree_replace(tree* root, int inorder[static 1]) {
    if (root) {
        tree_replace(root->left, inorder);
        root->val = inorder[root->val];
        tree_replace(root->right, inorder);
    }
}

size_t val_to_idx(int);
inline size_t val_to_idx(int val) {
    return val - MIN;
}

struct TreeNode* buildTree(int* preorder, int preorderSize, int* inorder, int inorderSize){
    size_t map[MAX-MIN+1];
    
    for (size_t i = 0; i < inorderSize; i++) {
        map[val_to_idx(inorder[i])] = i;
    }
    
    tree* t = 0;
    
    for (size_t i = 0; i < preorderSize; i++) {
        tree_insert(&t, map[val_to_idx(preorder[i])]);    
    }
    
    tree_replace(t, inorder);
    
    return t;
}
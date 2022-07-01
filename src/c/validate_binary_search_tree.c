/**
 * Definition for a binary tree node.
 * struct TreeNode {
 *     int val;
 *     struct TreeNode *left;
 *     struct TreeNode *right;
 * };
 */

typedef struct TreeNode tree;   

bool tree_check(tree* root, tree** min, tree** max) {
    tree* left_min = root;
    tree* left_max = root;
    tree* right_min = root;
    tree* right_max = root;
    
    if (root->left && !tree_check(root->left, &left_min, &left_max)) {
        return false;
    }
    
    if (left_max != root && left_max->val >= root->val) {
        return false;
    }
    
    if (!tree_check(root->right, &right_min, &right_max)) {
        return false;
    }
    
    if (right_min != root && right_min->val <= root->val) {
        return false;
    }
    
    if (min) *min = left_min;
    if (max) *max = right_max;
    
    return true;
}

bool isValidBST(tree* root){
    return tree_check(root, 0, 0);
}

// OR

/**
 * Definition for a binary tree node.
 * struct TreeNode {
 *     int val;
 *     struct TreeNode *left;
 *     struct TreeNode *right;
 * };
 */

typedef struct TreeNode tree;

size_t tree_length(tree* root) {
    if (!root) return 0;
    return 1 + tree_length(root->left) + tree_length(root->right);
}

void tree_to_array(tree* root, size_t idx[static 1], int* arr) {
    if (root) {
        tree_to_array(root->left, idx, arr);
        arr[(*idx)++] = root->val;
        tree_to_array(root->right, idx, arr);
    }
}

bool isValidBST(tree* root) {
    size_t const n = tree_length(root);
    int arr[n];
    size_t idx = 0;
    
    tree_to_array(root, &idx, arr);
    
    for (size_t i = 0; i < n - 1; i++) {
        if (arr[i] >= arr[i+1]) {
            return false;
        }
    }
    
    return true;
}

// even better


typedef struct TreeNode tree;

bool tree_check(tree* root, tree** prev) {
    if (!root) return true;
    if (!tree_check(root->left, prev)) return false;
    if (*prev && (*prev)->val >= root->val) return false;
    *prev = root;
    if (!tree_check(root->right, prev)) return false;
    return true;
}

bool isValidBST(tree* root) {
    tree* prev = 0; 
    return tree_check(root, 0);
}
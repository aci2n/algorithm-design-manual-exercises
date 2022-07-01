#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>

typedef struct tree tree;
struct tree {
	int val;
	tree* left;
	tree* right;
};

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
  return tree_check(root, &prev);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));
  return EXIT_SUCCESS;
}

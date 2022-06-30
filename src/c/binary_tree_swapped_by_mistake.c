#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>

/*
3-13. [5] Two elements of a binary search tree have been swapped by mistake. Give an O(n) algorithm to identify these two elements so they can be swapped back.

I hate this exercise >:(
*/

typedef struct tree tree;
struct tree {
  int val;
  tree* parent;
  tree* left;
  tree* right;
};

void tree_add(tree** t, int val) {
  tree* curr = *t;

  if (!curr) {
    *t = curr = malloc(sizeof(tree));
    *curr = (tree) { .val = val };
    return;
  }

  if (val < curr->val) {
    tree_add(&curr->left, val);
  } else if (val > curr->val) {
    tree_add(&curr->right, val);
  }
}

size_t tree_length(tree* t) {
  if (!t) return 0;
  return 1 + tree_length(t->left) + tree_length(t->right);
}

void tree_to_array_ref(tree* t, tree* arr[static 1], size_t idx[static 1]) {
  if (!t) return;
  tree_to_array_ref(t->left, arr, idx);
  arr[(*idx)++] = t;
  tree_to_array_ref(t->right, arr, idx);
}

void tree_to_array(tree* t, int arr[static 1], size_t idx[static 1]) {
  if (!t) return;
  tree_to_array(t->left, arr, idx);
  arr[(*idx)++] = t->val;
  tree_to_array(t->right, arr, idx);
}

void tree_copy(tree* src, tree** dst) {
  if (!src) return;
  *dst = malloc(sizeof(tree));
  *(*dst) = (tree) { .val = src->val };
  tree_copy(src->left, &((*dst)->left));
  tree_copy(src->right, &((*dst)->right));
}

void tree_fix(tree* t, int arr[static 1], size_t idx[static 1]) {
  if (!t) return;
  tree_fix(t->left, arr, idx);
  printf("fix: %d -> %d\n", t->val, arr[*idx]);
  t->val = arr[(*idx)++];
  tree_fix(t->right, arr, idx);
}

void print_array(size_t n, int arr[static n]) {
  printf("arr[%lu]: [ ", n);
  for (size_t i = 0; i < n; i++) {
    printf("%d ", arr[i]);
  }
  printf("]\n");
}

void fix_swapped(tree* t) {
  size_t len = tree_length(t);
  int arr[len];
  size_t idx = 0;

  tree_to_array(t, arr, &idx);
  print_array(len, arr);

  for (size_t i = 0; i < len - 1; i++) {
    if (arr[i] > arr[i+1]) {
      int t = arr[i];
      arr[i] = arr[i+1];
      arr[i+1] = t;
    }
  }

  for (size_t i = 0; i < len - 1; i++ ) {
    if (arr[len-i-1] < arr[len-i-2]) {
      int t = arr[len-i-1];
      arr[len-i-1] = arr[len-i-2];
      arr[len-i-2] = t;
    }
  }
  

  print_array(len, arr);

  idx = 0;
  tree_fix(t, arr, &idx);
}

void tree_print(tree* t) {
  if (t) {
    tree_print(t->left);
    printf("%d ", t->val);
    tree_print(t->right);
  }
}

tree* tree_max(tree* t) {
  if (!t) return 0;
  return t->right ? tree_max(t->right) : t;
}

tree* tree_min(tree* t) {
  if (!t) return 0;
  return t->left ? tree_min(t->left) : t;
}

bool tree_is_correct(tree* t) {
  if (!t) {
    return true;
  }
  if (t->left && t->val < t->left->val) {
    return false;
  }
  if (t->right && t->val > t->right->val) {
    return false;
  }
  return tree_is_correct(t->left) && tree_is_correct(t->right);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));
  size_t const n = 20;
  tree* t = 0;
  
  for (size_t i = 0; i < n; i++) {
    tree_add(&t, rand() % n);
  }

  tree_print(t); printf("\n");

  size_t len = tree_length(t);

  if (len > 2) {
    size_t a = rand() % len;
    size_t b = rand() % len;
    tree* nodes[len];
    size_t idx = 0;

    tree_to_array_ref(t, nodes, &idx);

    assert(tree_is_correct(t));

    printf("swapping: %d -> %d\n", nodes[a]->val, nodes[b]->val);

    int v = nodes[a]->val;
    nodes[a]->val = nodes[b]->val;
    nodes[b]->val = v;

    assert(!tree_is_correct(t));

    fix_swapped(t);

    fflush(0);
    assert(tree_is_correct(t));
  } else {
    printf("tree too small\n");
  }

  return EXIT_SUCCESS;
}

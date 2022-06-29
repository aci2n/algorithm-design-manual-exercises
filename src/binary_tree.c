#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <assert.h>

typedef struct tree tree;
struct tree {
  int val;
  tree* parent;
  tree* left;
  tree* right;
};

void tree_add(tree** t, int val, tree* parent) {
  tree* p = *t;

  if (!p) {
    *t = p = malloc(sizeof(tree));
    *p = (tree) {
      .val = val,
      .parent = parent,
    };
    return;
  }

  if (val < p->val) {
    tree_add(&p->left, val, p);
  } else if (val > p->val) {
    tree_add(&p->right, val, p);
  }
}

tree* tree_search(tree* t, int val) {
  if (!t) {
    return 0;
  }
  if (val < t->val) {
    return tree_search(t->left, val);
  }
  if (val > t->val) {
    return tree_search(t->right, val);
  }
  return t;
}

int tree_delete_min(tree**);

void tree_delete_node(tree** t) {
  tree* p = *t;

  if (!p->left && !p->right) {
    *t = 0;
    free(p);
    return;
  }

  if (!p->left || !p->right) {
    tree* c = p->left ? p->left : p->right;
    c->parent = p->parent;
    *t = c;
    free(p);
    return;
  }
  
  p->val = tree_delete_min(&p->right);
}

int tree_delete_min(tree** t) {
  tree* p = *t;

  if (!p->left) {
    int val = p->val;
    tree_delete_node(t);
    return val;
  }

  return tree_delete_min(&p->left);
}

void tree_delete(tree** t, int val) {
  tree* p = *t;

  if (!p) {
    return;
  }

  if (val < p->val) {
    tree_delete(&p->left, val);
    return;
  }

  if (val > p-> val) {
    tree_delete(&p->right, val);
    return;
  }

  tree_delete_node(t);
}

typedef void tree_op(int val);

void tree_visit(tree* t, tree_op* op) {
  if (t) {
    tree_visit(t->left, op);
    op(t->val);
    tree_visit(t->right, op);
  }
}

void print_val(int val) {
  printf("%d ", val);
}

int main() {
  int const n = 20;
  srand(time(0));

  tree* t = {0};
  int last = 0;

  for (size_t i = 0; i < n; i++) {
    last = rand() % n;
    tree_add(&t, last, 0);
  }

  assert(tree_search(t, last)->val == last);

  tree_visit(t, print_val);
  printf("\n");

  for (size_t i = 0; i < n; i++) {
    int const r = rand() % n;
    printf("deleting %d\n", r);
    tree_delete(&t, r);
    tree_visit(t, print_val);
    printf("\n");
  }

  return EXIT_SUCCESS;
}

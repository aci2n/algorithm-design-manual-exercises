#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>

typedef struct tree tree;
struct tree {
  int val;
  tree* parent;
  tree* left;
  tree* right;
};

typedef struct list list;
struct list {
  int val;
  list* prev;
  list* next;
};

void tree_add(tree** t, int val, tree* parent) {
  tree* curr = *t;

  if (!curr) {
    *t = curr = malloc(sizeof(tree));
    *curr = (tree) { .val = val, .parent = parent };
    return;
  }

  if (val < curr->val) {
    tree_add(&curr->left, val, curr);
  } else if (val > curr->val) {
    tree_add(&curr->right, val, curr);
  }
}

void list_add(list** l, int val) {
  list* next = *l;
  list* curr = malloc(sizeof(list));

  *curr = (list) { .val = val, .next = next };
  if (next) next->prev = curr;
  *l = curr;
}

tree* tree_max(tree* t) {
  if (!t) return 0;
  return t->right ? tree_max(t->right) : t;
}

tree* tree_min(tree* t) {
  if (!t) return 0;
  return t->left ? tree_min(t->left) : t;
}

tree* tree_predecessor(tree* t) {
  if (!t) return 0;
  if (t->left) return tree_max(t->left);
  tree* p = t->parent;
  for (; p && p->val > t->val; p = p->parent);
  return p;
}

tree* tree_successor(tree* t) {
  if (!t) return 0;
  if (t->right) return tree_min(t->right);
  tree* p = t->parent;
  for (; p && p->val < t->val; p = p->parent);
  return p;
}

list* tree_combine(tree* a, tree* b) {
  list* l = 0;
  tree* walker_a = tree_max(a);
  tree* walker_b = tree_max(b);

  while (walker_a || walker_b) {
    tree** walker_max = 0;

    if (walker_a && walker_b) {
      walker_max = walker_a->val > walker_b->val ? &walker_a : &walker_b;
    } else if (walker_a) {
      walker_max = &walker_a;
    } else {
      walker_max = &walker_b;
    }

    list_add(&l, (*walker_max)->val);
    *walker_max = tree_predecessor(*walker_max);
  }

  return l;
}

void list_print(list* l) {
  for (list* c = l; c; c = c->next) {
    printf("%d -> ", c->val);
  }
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

  tree* t1 = 0;
  tree* t2 = 0;
  size_t n1 = 20;
  size_t n2 = 25;

  for (size_t i = 0; i < n1; i++) {
    tree_add(&t1, rand() % n1, 0);
  }

  for (size_t i = 0; i < n2; i++) {
    tree_add(&t2, rand() % n2, 0);
  }

  list* l = tree_combine(t1, t2);
  list_print(l);

  return EXIT_SUCCESS;
}

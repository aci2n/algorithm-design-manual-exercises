#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <assert.h>

/*
3-4. [5] Design a stack S that supports S.push(x), S.pop(), and S.findmin(), which
returns the minimum element of S. All operations should run in constant time.
*/

typedef struct node node;
struct node {
  int value;
  node* next;
};

typedef struct min_stack min_stack;
struct min_stack {
  node* stack;
  node* min;
};

void min_stack_push(min_stack* ms, int value) {
  node* n = malloc(sizeof(node));
  *n = (node) {.value = value, .next = ms->stack};
  ms->stack = n;

  if (!ms->min || value <= ms->min->value) {
    node* nm = malloc(sizeof(node));
    *nm = (node) {.value = value, .next = ms->min};
    ms->min = nm;
  }
}

int min_stack_pop(min_stack* ms) {
  node* n = ms->stack; // assume valid pointer
  ms->stack = n->next;

  if (n->value == ms->min->value) {
    ms->min = ms->min->next;
  }
}

int min_stack_min(min_stack* ms) {
  return ms->min->value; // assume valid pointer
}

int main(int const argc, char const*const argv[static argc + 1]) {
  min_stack* ms = malloc(sizeof(min_stack));

  for (int i = 0; i < 20; i++) {
    min_stack_push(ms, i % 10);
  }

  for (int i = 0; i < 19; i++) {
    min_stack_pop(ms);
    assert(min_stack_min(ms) == 0);
  }

  free(ms);

  return EXIT_SUCCESS;
}

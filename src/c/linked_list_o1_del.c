#include <stdlib.h>
#include <stdbool.h>
#include <assert.h>
#include <stdio.h>

typedef struct list list;
struct list {
  int val;
  bool tombstone;
  list* next;
};

list* list_new() {
  list* l = malloc(sizeof(list));

  *l = (list){ .tombstone = true };

  return l;
}

void list_add(list** l, int val) {
  if (!l) {
    return;
  }

  list* n = malloc(sizeof(list));

  *n = (list){ .val = val, .next = *l };
  *l = n;
}

void list_remove(list* l) {
  if (!l || l->tombstone) {
    return;
  }

  list* n = l->next;

  l->val = n->val;
  l->next = n->next;
  l->tombstone = n->tombstone;

  free(n);

  return;
}

void list_print(list* l) {
  while (l) {
    if (l->tombstone) {
      printf("X\n");
    } else {
      printf("%d -> ", l->val);
    }
    l = l->next;
  }
}

int main() {
  list* l = list_new();

  list_add(&l, 0);
  list_print(l);
  list_add(&l, 1);
  list_print(l);
  list_add(&l, 2);
  list_print(l);

  list_remove(l->next);
  list_print(l);
  list_remove(l->next);
  list_print(l);
  list_remove(l);
  list_print(l);
  list_remove(l);
  list_print(l);

  return EXIT_SUCCESS;
}

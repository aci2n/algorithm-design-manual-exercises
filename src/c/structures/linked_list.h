#include <stdlib.h>

typedef struct linked_list linked_list;
struct linked_list {
  int value;
  linked_list* next;
};

void linked_list_add(linked_list** l, int value) {
  linked_list* node = malloc(sizeof(linked_list));

  *node = (linked_list){ .value = value, .next = *l };
  *l = node;
}

void linked_list_reverse(linked_list** l) {
  linked_list* prev = 0;
  linked_list* curr = *l;

  while (curr) {
    linked_list* next = curr->next;
    curr->next = prev;
    prev = curr;
    curr = next;
  }

  *l = curr;
}

#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>

struct ListNode {
    int val;
    struct ListNode *next;
};

typedef struct ListNode list;

int list_compare(list* a, list* b) {
    if (a->val < b->val) return -1;
    if (a->val > b->val) return 1;
    return 0;
}

int list_compare0(void const* a, void const* b) {
    return list_compare(*((list**)a), *((list**)b));
}

size_t list_length(list* list) {
    size_t len = 0;
    while (list) {
        len++;
        list = list->next;
    }
    return len;
}

struct ListNode* sortList(struct ListNode* head){
    size_t const len = list_length(head);
    list** arr = malloc(len * sizeof(*arr));
    size_t pos = 0;
    
    for (list* walker = head; walker; walker = walker->next) {
        arr[pos++] = walker;
    }
    
    qsort(arr, len, sizeof(*arr), list_compare0);
    
    list* res = 0;
    list** tail = &res;
    
    for (size_t i = 0; i < len; i++) {
        *tail = arr[i];
        (*tail)->next = 0;
        tail = &(*tail)->next;
    }
    
    return res;
}

list* create_list(size_t n, int arr[static n]) {
  list* res = 0;
  list** l = &res;

  for (size_t i = 0; i < n; i++) {
    *l = malloc(sizeof(list));
    (*l)->val = arr[i];
    (*l)->next = 0;
    l = &(*l)->next;
  }

  return res;
}

void list_print(list* l) {
  printf("* -> ");
  while (l) {
    printf("%d -> ", l->val);
    l = l->next;
  }
  printf("\n");
}

int main() {
  int arr[] = {4, 2, 1, 3};
  size_t n = sizeof(arr) / sizeof(arr[0]);

  list* l = create_list(n, arr);
  list_print(l);

  list* sorted = sortList(l);
  list_print(sorted);

  return EXIT_SUCCESS;
}


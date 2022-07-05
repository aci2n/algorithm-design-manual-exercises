#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>

struct ListNode {
    int val;
    struct ListNode *next;
};

// -----

#define DEBUG 1

typedef struct ListNode list;

typedef struct heap_entry heap_entry;
struct heap_entry {
  int val;
  size_t list_index;
};

typedef struct heap heap;
struct heap {
  heap_entry** q;
  size_t len;
};

size_t next_pow2(size_t const x) {
  size_t p = 1;
  while (p < x) p *= 2;
  return p;
}

heap* heap_new(size_t const c) {
  size_t const cap = next_pow2(c);
  heap_entry** q = malloc(sizeof(*q) * cap);
  heap* h = malloc(sizeof(*h));
  *h = (heap) { .q = q };
  return h;
}

void heap_swap(heap_entry** a, heap_entry** b) {
  heap_entry* temp = *a;
  *a = *b;
  *b = temp;
}

void heap_insert(heap* heap, int val, size_t list_index) {
  size_t i = heap->len++;
  heap_entry* x = malloc(sizeof(heap_entry));

  *x = (heap_entry) { .val = val, .list_index = list_index};
  heap->q[i] = x;

  while (i > 0) {
    size_t p = (i - 1) / 2;
    if (x->val >= heap->q[p]->val) break;
    heap_swap(&heap->q[i], &heap->q[p]);
    i = p;
  }
}

heap_entry* heap_remove(heap* heap) {
  if (heap->len == 0) {
    return 0;
  }

  heap_entry* first = heap->q[0];

  if (--heap->len > 0) {
    size_t i = 0;
	
    heap->q[0] = heap->q[heap->len];

    while (true) {
      size_t min = i;

      for (size_t j = 0; j < 2; j++) {
        size_t const c = (i * 2) + 1 + j;
        if (c < heap->len && heap->q[c]->val < heap->q[min]->val) {
          min = c;
        }
      }

      if (min == i) break;
      heap_swap(&heap->q[i], &heap->q[min]);
      i = min;
    }
  }

  return first;
}

void heap_print(heap* h) {
  if (!DEBUG) return;
  printf("heap(%lu): ", h->len);
  for (size_t i = 0; i < h->len; i++) {
    printf("%d (list: %lu) ", h->q[i]->val, h->q[i]->list_index);
  }
  printf("\n");
}

void consume_list(heap* heap, list** lists, size_t i) {
  list* const l = lists[i];

  if (l) {
    heap_insert(heap, l->val, i);
    heap_print(heap);
    lists[i] = l->next;
    free(l);
  }
}

struct ListNode* mergeKLists(struct ListNode** lists, int listsSize){
    if (!lists || listsSize <= 0) {
        return 0;
    }
    
    list* result = 0;
    list** next = &result;
    heap* h = heap_new(listsSize);

    for (size_t i = 0; i < listsSize; i++) {
      consume_list(h, lists, i);
    }

    while (h->len > 0) {
      heap_entry* first = heap_remove(h);
      heap_print(h);

      *next = malloc(sizeof(list));
      (*next)->val = first->val;
      (*next)->next = 0;

      next = &((*next)->next);

      consume_list(h, lists, first->list_index);
      free(first);
    }

    free(h);

    return result;
}

void list_insert(list** l, int val) {
  list* node = malloc(sizeof(list));
  *node = (list) { .val = val, .next = *l };
  *l = node;
}

list* list_create(size_t n, int arr[restrict static n]) {
  list* l = 0;
  for (size_t i = n; i > 0; i--) {
    list_insert(&l, arr[i-1]);
  }
  return l;
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
  int a0[] = {-8,-7,-6,-3,-2,-2,0,3};
  int a1[] = {-10,-6,-4,-4,-4,-2,-1,4};
  int a2[] = {-10,-9,-8,-8,-6};
  int a3[] = {-10,0,4};

#define LIST_CREATE(ARR) list_create(sizeof((ARR)) / sizeof((ARR)[0]), (ARR))

  list* lists[] = {
    LIST_CREATE(a0),
    LIST_CREATE(a1),
    LIST_CREATE(a2),
    LIST_CREATE(a3),
  };

  size_t const n = sizeof(lists) / sizeof(lists[0]);

  printf("merging %lu lists:\n", n);
  for (size_t i = 0; i < n; i++) list_print(lists[i]);
  printf("\n");
  
  list* sorted = mergeKLists(lists, n);
  printf("sorted list:\n");
  list_print(sorted);

  printf("\n");

  return EXIT_SUCCESS;
}

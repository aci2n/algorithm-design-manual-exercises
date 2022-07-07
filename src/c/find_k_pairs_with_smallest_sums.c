#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>

typedef int pq_comparator(void const*, void const*);
typedef struct priority_queue priority_queue;
struct priority_queue {
  void** queue;
  pq_comparator* comparator;
  size_t len;
  size_t cap;
  size_t sz;
};

static size_t next_pow2(size_t const n) {
  size_t pow = 1;
  while (pow < n) {
    pow *= 2;
  }
  return pow;
}

priority_queue* pq_init(size_t const initial_cap, size_t const sz, pq_comparator* comparator) {
  size_t cap = next_pow2(initial_cap);
  priority_queue* pq = malloc(sizeof(priority_queue));

  *pq = (priority_queue) {
    .queue = malloc(cap * sz),
    .cap = cap,
    .comparator = comparator,
    .sz = sz,
  };

  return pq;
}

static void pq_resize(priority_queue pq[restrict static 1]) {
  pq->cap *= 2;
  pq->queue = realloc(pq->queue, pq->cap * pq->sz);
}

static void pq_bubble_up(priority_queue pq[restrict static 1]) {
  assert(pq->len > 0);
  size_t i = pq->len - 1;
  void* const x = pq->queue[i];

  while (i > 0) {
    size_t const p = (i - 1) / 2;

    if (pq->comparator(x, pq->queue[p]) >= 0) {
      break;
    }

    pq->queue[i] = pq->queue[p];
    pq->queue[p] = x;
    i = p;
  }
}

static void pq_bubble_down(priority_queue pq[restrict static 1]) {
  assert(pq->len > 0);
  size_t i = 0;
  void* const x = pq->queue[i];

  while (i < pq->len) {
    size_t min = i;

    for (size_t j = 0; j <= 1; j++) {
      size_t const child_index = (2 * i) + 1 + j;
      if (child_index < pq->len && pq->comparator(pq->queue[min], pq->queue[child_index]) > 0) {
        min = child_index ;
      }
    }

    if (min == i) {
      break;
    }

    pq->queue[i] = pq->queue[min];
    pq->queue[min] = x;
    i = min;
  }
}

void pq_insert(priority_queue pq[restrict static 1], void* restrict x) {
  if (pq->len == pq->cap) {
    pq_resize(pq);
  }
  pq->queue[pq->len++] = x;
  pq_bubble_up(pq);
}

void* pq_remove(priority_queue pq[restrict static 1]) {
  if (pq->len == 0) {
    return 0;
  }
  void* x = pq->queue[0];
  if (--pq->len > 0) {
    pq->queue[0] = pq->queue[pq->len];
    pq_bubble_down(pq);
  }
  return x;
}

void* pq_first(priority_queue pq[restrict static 1]) {
  return pq->queue[0];
}

bool pq_empty(priority_queue pq[restrict static 1]) {
  return pq->len == 0;
}

typedef struct pair pair;
struct pair {
  int val[2];
  size_t i;
  size_t j;
};

typedef struct list list;
struct list {
  size_t val;
  list* next;
};

int int_compare(int a, int b) {
  return a > b ? 1 : a < b ? -1 : 0;
}

int pair_compare(pair a[static 1], pair b[static 1]) {
  return int_compare(a->val[0] + a->val[1], b->val[0] + b->val[1]);
}

int pair_compare0(void const* a, void const* b) {
  return pair_compare((pair*)a, (pair*)b);
}

void list_add(list** l, size_t val) {
  list* n = malloc(sizeof(*n));
  *n = (list) { .val = val, .next = *l };
  *l = n;
}

void list_remove(list** l, size_t val) {
  while (*l) {
    if ((*l)->val == val) {
      *l = (*l)->next;
      return;
    }
    l = &(*l)->next;
  }
}

bool list_contains(list* l, size_t val) {
  while (l) {
    if (l->val == val) {
      return true;
    }
    l = l->next;
  }
  return false;
}

pair* pair_new(size_t i, int u[static i+1], size_t j, int v[static j+1]) {
  pair* p = malloc(sizeof(*p));
  *p = (pair) {
    .val = { u[i], v[j] },
    .i = i,
    .j = j,
  };
  return p;
}

void register_pair(
    size_t const ulen, int u[static ulen],
    size_t const vlen, int v[static vlen],
    size_t const i, size_t const j, 
    priority_queue* pq,
    list* dict[static ulen]) {
  if (i < ulen && j < vlen && !list_contains(dict[i], j)) {
    list_add(&dict[i], j);
    pq_insert(pq, pair_new(i, u, j, v));
  }
}

void find_pairs(
    size_t const ulen, int u[static ulen],
    size_t const vlen, int v[static vlen],
    size_t k, priority_queue* pq,
    int rlen[static 1], int* r[static k],
    list* dict[static ulen]) {
  if ((*rlen) == k || pq_empty(pq)) {
    return;
  }
  
  pair* first = pq_remove(pq);
  int* pair = malloc(sizeof(int) * 2);

  pair[0] = first->val[0];
  pair[1] = first->val[1];
  r[(*rlen)++] = pair;

  register_pair(ulen, u, vlen, v, first->i+1, first->j, pq, dict);
  register_pair(ulen, u, vlen, v, first->i, first->j+1, pq, dict);

  find_pairs(ulen, u, vlen, v, k, pq, rlen, r, dict);
}

int** smallest_pairs(
    size_t const ulen, int u[static ulen],
    size_t const vlen, int v[static vlen],
    size_t const k, int rlen[static 1]) {
  int** const pairs = malloc(sizeof(*pairs) * k);
  priority_queue* pq = pq_init(
      (size_t)(((double)k / ulen) * ulen),
      sizeof(pair*),
      pair_compare0);
  list** dict = calloc(sizeof(*dict), ulen);

  register_pair(ulen, u, vlen, v, 0, 0, pq, dict);
  find_pairs(ulen, u, vlen, v, k, pq, rlen, pairs, dict);

  return pairs;
}

void print_pairs(size_t const n, int* pairs[static n]) {
  for (size_t i = 0; i < n; i++) {
    printf("[%d, %d] ", pairs[i][0], pairs[i][1]);
  }
  printf("\n");
}

int** kSmallestPairs(int* nums1, int nums1Size, int* nums2, int nums2Size, int k, int* returnSize, int** returnColumnSizes){  
  *returnSize = 0;
  int** pairs = smallest_pairs(nums1Size, nums1, nums2Size, nums2, k, returnSize);

  *returnColumnSizes = malloc(sizeof(**returnColumnSizes) * (*returnSize));
  for (size_t i = 0; i < (*returnSize); i++) (*returnColumnSizes)[i] = 2;

  return pairs;
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

  int u[] = {1, 1, 2};
  int v[] = {1, 2, 3};
  size_t k = 10;
  int r1 = 0;
  int* r2 = 0;

  int** pairs = kSmallestPairs(u, sizeof(u) / sizeof(u[0]), v, sizeof(v) / sizeof(v[0]), k, &r1, &r2);
  print_pairs(r1, pairs);

  return EXIT_SUCCESS;
}

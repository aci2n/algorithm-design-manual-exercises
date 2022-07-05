#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>

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

typedef struct person person;
struct person {
  char* name;
  size_t age;
  size_t weight;
};

int size_t_compare(size_t a, size_t b) {
  if (a < b) return -1;
  if (a > b) return 1;
  return 0;
}

int person_compare(person* a, person* b) {
  if (a->age != b->age) return size_t_compare(a->age, b->age);
  if (a->weight != b->weight) return size_t_compare(a->weight, b->weight);
  return strcmp(a->name, b->name);
}

int person_compare_v(void const* a, void const* b) {
  return person_compare((person*)a, (person*)b);
}

void person_print(person p[restrict static 1]) {
  printf("[name=%s,age=%lu,weight=%lu]\n", p->name, p->age, p->weight);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));
  priority_queue* pq = pq_init(20, sizeof(person), person_compare_v);
  
  for (size_t i = 0; i < 1000; i++) {
    person* p = malloc(sizeof(person));
    char* name = malloc(sizeof(char) * 50);
    snprintf(name, 50, "person %lu", i);
    *p = (person) { .age = rand() % 50, .weight = rand() % 80, .name = name };
    pq_insert(pq, p);
    printf("%lu: (len: %lu, cap: %lu) ", i, pq->len, pq->cap);
    person_print(pq_first(pq));
  }

  // will print in order
  while (pq->len > 0) {
    person* p = pq_remove(pq);
    person_print(p);
  }

  return EXIT_SUCCESS;
}

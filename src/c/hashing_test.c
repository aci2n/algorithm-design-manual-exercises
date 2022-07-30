#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>

typedef struct list_node list_node;
struct list_node {
  long value;
  list_node* next;
};

typedef struct list list;
struct list {
  size_t size;
  list_node* root;
};

typedef struct map map;
struct map {
  size_t size;
  size_t buckets_size;
  list* buckets;
};

void build_bins(size_t const n) {
  map m = {
    .buckets_size = n,
    .buckets = malloc(sizeof(*m.buckets) * n),
  };

  for (size_t i = 0; i < n; i++) {
    list* l = &m.buckets[rand() % n];
    list_node* n = malloc(sizeof(*n));  
    *n = (list_node) { .value = i, .next = l->root };
    l->root = n;
    l->size++;
    m.size++;
  }

  size_t* counts = calloc(sizeof(*counts), n);

  for (size_t i = 0; i < n; i++) {
    counts[m.buckets[i].size]++;
  }

  for (size_t i = 0; i < n; i++) {
    if (counts[i] > 0) {
      printf("size %lu: %lu buckets\n", i, counts[i]);
    }
  }

  for (size_t i = 0; i < n; i++) {
    for (list_node* curr = m.buckets[i].root; curr;) {
      list_node* next = curr->next;
      free(curr);
      curr = next;
    }
  }
  free(m.buckets);
  free(counts);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));
  build_bins(strtol(argv[1], 0, 0));
  return EXIT_SUCCESS;
}

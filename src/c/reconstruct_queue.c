#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>

int int_compare(int a, int b) {
  return a < b ? -1 : a > b ? 1 : 0;
}

int entry_compare(int a[static 2], int b[static 2]) {
  if (a[0] != b[0]) return int_compare(a[0], b[0]);
  if (a[1] != b[1]) return int_compare(a[1], b[1]);
  return 0;
}

int entry_compare0(void const* a, void const* b) {
  return entry_compare(*((int**)a), *((int**)b));
}

int** reconstruct_queue(size_t n, int* queue[static n]) {
  int** result = calloc(sizeof(queue[0]), n);

  qsort(queue, n, sizeof(queue[0]), entry_compare0);

  for (size_t i = 0; i < n; i++) {
    size_t ahead = queue[i][1];
    size_t pos = 0;

    while (ahead > 0) {
      if (!result[pos] || result[pos][0] >= queue[i][0]) {
        ahead--;
      }
      pos++;
    }

    while (result[pos]) {
      pos++;
    }

    result[pos] = queue[i];
  }

  return result;
}

int** reconstructQueue(int** people, int peopleSize, int* peopleColSize, int* returnSize, int** returnColumnSizes){
  int** queue = reconstruct_queue(peopleSize, people);

  *returnSize = peopleSize;
  *returnColumnSizes = malloc(sizeof(**returnColumnSizes) * peopleSize);
  for (size_t i = 0; i < peopleSize; i++) (*returnColumnSizes)[i] = 2;

  return queue;
}

void print_queue(size_t const n, int* queue[static n]) {
  for (size_t i = 0; i < n; i++) {
    printf("[%d, %d] ", queue[i][0], queue[i][1]);
  }
  printf("\n");
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

  int people[][2] = {
    //{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}
    {6, 0}, {5, 0}, {4, 0}, {3, 2}, {2, 2}, {1, 4}
  };
  size_t n = sizeof(people) / sizeof(people[0]);
  
  int** p = malloc(sizeof(*p) * n);
  int peopleColSize[n];
  int returnSize = 0;
  int* returnColumnSizes = 0;

  for (size_t i = 0; i < n; i++) {
    p[i] = malloc(sizeof(*p[i]));
    p[i][0] = people[i][0];
    p[i][1] = people[i][1];
    peopleColSize[i] = 2;
  }

  print_queue(n, p);

  int** queue = reconstructQueue(p, n, peopleColSize, &returnSize, &returnColumnSizes);
  print_queue(n, queue);

  return EXIT_SUCCESS;
}

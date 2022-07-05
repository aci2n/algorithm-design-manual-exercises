#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>

typedef int cmpr(void const*, void const*);

static void swap(void* a[static 1], void* b[static 1]) {
  void* temp = *a;
  *a = *b;
  *b = temp;
}

void print_int_arr(size_t const n, void* arr[restrict static n]) {
  for (size_t i = 0; i < n; i++) {
    printf("%d ", *((int*)arr[i]));
  }
  printf("\n");
}

void quicksort(size_t const n, void* arr[const restrict static n], cmpr* comparator) { 
  if (n <= 1) {
    return;
  }

  register size_t pivot_i = rand() % n;
  void* const pivot = arr[pivot_i];
  register size_t partition = 0;

  for (register size_t i = 0; i < n; i++) {
    if (comparator(arr[i], pivot) < 0) {
      swap(&arr[i], &arr[partition++]);

      if (arr[partition] == pivot) {
        pivot_i = partition;
      }
    }
  }

  swap(&arr[partition], &arr[pivot_i]);

  quicksort(partition, arr, comparator);
  quicksort(n - partition - 1, arr + partition + 1, comparator);
}


int int_compare0(int const a[restrict static 1], int const b[restrict static 1]) {
  if (*a > *b) return 1;
  if (*a < *b) return -1;
  return 0;
}

int int_compare(void const* a, void const* b) {
  return int_compare0((int*)a, (int*)b);
}

void shufflesort(size_t const n, int a[restrict static n]) {
  while (true) {
    for (size_t i = 0; i < n; i++) {
      size_t r = rand() % n;
      swap(&arr[i], &arr[r]);
    }
  }
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

  size_t const n = 100;
  int* arr = malloc(sizeof(*arr) * n);
  int** arr2 = malloc(sizeof(*arr2) * n);

  for (size_t i = 0; i < n; i++) {
    arr[i] = rand() % n;
    arr2[i] = &arr[i];
  }

  //qsort(arr, n, sizeof(arr[0]), int_compare);
  quicksort(n, (void*)arr2, int_compare);
  fflush(stdout);

  for (size_t i = 1; i < n; i++) {
    assert(*(arr2[i]) >= *(arr2[i-1]));
  }

  free(arr);
  free(arr2);

  return EXIT_SUCCESS;
}

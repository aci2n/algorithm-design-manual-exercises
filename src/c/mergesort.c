#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>

typedef int cmpr(void const*, void const*);

static void mergesort(size_t const n, void* arr[restrict static n], cmpr* comparator) { 
  if (n <= 1) {
    return;
  }

  size_t const n1 = n / 2;
  size_t const n2 = n - n1;
  void** const a2 = arr + n1;

  mergesort(n1, arr, comparator);
  mergesort(n2, a2, comparator);

  void** const a1 = malloc(sizeof(arr[0]) * n1);
  memcpy(a1, arr, sizeof(arr[0]) * n1);

  register size_t i = 0;
  register size_t i1 = 0;
  register size_t i2 = 0;

  while (i1 < n1) {
    if (i2 == n2 || comparator(a1[i1], a2[i2]) <= 0) {
      arr[i++] = a1[i1++];
    } else {
      arr[i++] = a2[i2++];
    }
  }

  free(a1);
}

int int_compare0(int const a[restrict static 1], int const b[restrict static 1]) {
  if (*a > *b) return 1;
  if (*a < *b) return -1;
  return 0;
}

int int_compare(void const* a, void const* b) {
  return int_compare0((int*)a, (int*)b);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

  size_t const n = 10000000;
  int* arr = malloc(sizeof(*arr) * n);
  int** arr2 = malloc(sizeof(*arr2) * n);

  for (size_t i = 0; i < n; i++) {
    arr[i] = rand() % n;
    arr2[i] = &arr[i];
  }

  mergesort(n, (void*)arr2, int_compare);

  for (size_t i = 1; i < n; i++) {
    assert(*(arr2[i]) >= *(arr2[i-1]));
  }

  free(arr);
  free(arr2);

  return EXIT_SUCCESS;
}

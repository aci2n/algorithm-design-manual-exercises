#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>

int binary_search(size_t const n, int const arr[restrict static n], int const val) {
  size_t lo = 0;
  size_t hi = n - 1;

  while (lo <= hi) {
    size_t const mid = (lo + hi) / 2;
    int const c = arr[mid];

    if (c < val) {
      lo = mid + 1;
    } else if (c > val) {
      hi = mid - 1;
    } else {
      return mid;
    }
  }

  return -1;
}

int compare_int(void const* a, void const* b) {
  int* A = (int*) a;
  int* B = (int*) b;
  if (*A < *B) return -1;
  if (*A > *B) return 1;
  return 0;
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

  int const arr[] = { -10, -1, 0, 1, 2, 2, 4, 6, 8, 8, 8, 1000 };
  size_t const n = sizeof(arr) / sizeof(arr[0]);

  qsort((void*)arr, n, sizeof(arr[0]), compare_int);
  for (size_t i = 0; i < n; i++) printf("%d ", arr[i]); printf("\n"); fflush(stdout);
  
#define CHECK(VAL, IDX) \
  do { \
    assert(binary_search(n, arr, (VAL)+0) == (IDX)+0); \
  } while (false);

#define CHECK_RANGE(VAL, IDX_MIN, IDX_MAX) \
  do { \
    int pos = binary_search(n, arr, (VAL)+0); \
    assert(pos >= (IDX_MIN)+0 && pos <= (IDX_MAX)+0); \
  } while (false);

  CHECK(-10, 0);
  CHECK(-1, 1);
  CHECK(0, 2);
  CHECK(1, 3);
  CHECK_RANGE(2, 4, 5);
  CHECK(4, 6);
  CHECK(6, 7);
  CHECK_RANGE(8, 8, 10);
  CHECK(1000, 11);

  CHECK(10000, -1);
  CHECK(-1000, -1);
  CHECK(50, -1);

  return EXIT_SUCCESS;
}

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>

int find_missing_int(size_t n, int arr[static n]) {
  size_t l = 0;
  size_t h = n - 1;

  while (l < h) {
    size_t m = (l + h) / 2;
    if (arr[m] - 1 > m) {
      h = m;
    } else {
      l = m + 1;
    }
  }

  return arr[l] - 1;
}

int main(int const argc, char const*const argv[static argc + 1]) {
#define TEST(EXPECTED, ...) \
  do { \
    int arr[] = { __VA_ARGS__ }; \
    size_t n = sizeof(arr) / sizeof(arr[0]); \
    assert(find_missing_int(n, arr) == +(EXPECTED)); \
  } while (false);

  TEST(4, 1, 2, 3, 5);
  TEST(2, 1, 3, 4, 5);
  TEST(1, 2, 3, 4, 5);
  TEST(7, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12);
  
  srand(time(0));
  return EXIT_SUCCESS;
}

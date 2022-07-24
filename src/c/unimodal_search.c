#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>

int compare_int(int a, int b) {
  return a < b ? -1 : b < a ? 1 : 0;
}

int compute_slope(size_t n, int arr[static n], size_t i) {
  int l = 0;
  int r = 0;

  if (i > 0) {
    l = compare_int(arr[i], arr[i-1]);
  }

  if (i < n - 1) {
    r = compare_int(arr[i+1], arr[i]);
  }

  int s = l + r;
  return s > 0 ? 1 : s < 0 ? -1 : 0;
}

int find_unimodal_max(size_t n, int arr[static n]) {
  size_t l = 0;
  size_t h = n - 1;

  while (l < h) {
    size_t m = (l + h) / 2;
    int slope = compute_slope(n, arr, m);

    if (slope > 0) {
      l = m + 1;
    } else if (slope < 0) {
      h = m - 1;
    } else {
      return arr[m];
    }
  }

  return arr[l] - 1;
}

int main(int const argc, char const*const argv[static argc + 1]) {
#define TEST(EXPECTED, ...) \
  do { \
    int arr[] = { __VA_ARGS__ }; \
    size_t n = sizeof(arr) / sizeof(arr[0]); \
    assert(find_unimodal_max(n, arr) == +(EXPECTED)); \
  } while (false);
  
  srand(time(0));
  return EXIT_SUCCESS;
}

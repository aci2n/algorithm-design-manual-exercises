#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>
#include <limits.h>

int max(int a, int b) {
  return a > b ? a : b;
}

int min(int a, int b) {
  return a < b ? a : b;
}

int get_i(size_t n, int arr[static n], int i) {
  if (i < 0) return INT_MIN;
  if (i >= n) return INT_MAX;
  return arr[i];
}

double median_sorted(size_t a_len, int a[static a_len], size_t b_len, int b[static b_len]) {
  if (a_len < b_len) {
    return median_sorted(b_len, b, a_len, a);
  }

  if (a_len <= 0) {
    return 0;
  }

  int const median = (a_len + b_len) / 2;
  int b_lo = 0;
  int b_hi = b_len - 1;
  int b_mid = 0;
  int a_mid = 0;

  while (b_lo <= b_hi) {
    b_mid = (b_lo + b_hi) / 2;
    a_mid = median - b_mid - 2;

    if (get_i(a_len, a, a_mid) > get_i(b_len, b, b_mid + 1)) {
      b_lo = b_mid + 1;
    } else if (get_i(b_len, b, b_mid) > get_i(a_len, a, a_mid + 1)) {
      b_hi = b_mid - 1;
    } else {
      break;
    }
  }

  if (b_hi == -1) {
    a_mid = median - 1;
    b_mid = -1;
  }

  if ((a_len + b_len) % 2 == 0) {
    int first = max(get_i(a_len, a, a_mid), get_i(b_len, b, b_mid));
    int second = min(get_i(a_len, a, a_mid + 1), get_i(b_len, b, b_mid + 1));
    return (first + second) / 2.0;
  }

  return min(get_i(a_len, a, a_mid + 1), get_i(b_len, b, b_mid + 1));
}

double findMedianSortedArrays(int* nums1, int nums1Size, int* nums2, int nums2Size) {
  return median_sorted(nums1Size, nums1, nums2Size, nums2);
}

void test(size_t a_len, int a[static a_len], size_t b_len, int b[static b_len], double expected) {
  assert(median_sorted(a_len, a, b_len, b) == expected);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

#define ARR(...) __VA_ARGS__
#define TEST(A, B, EXPECTED) \
  do { \
    int a[] = A; \
    int b[] = B; \
    size_t a_len = sizeof(a) / sizeof(a[0]); \
    size_t b_len = sizeof(b) / sizeof(b[0]); \
    test(a_len, a, b_len, b, +(EXPECTED)); \
  } while (false); \

  TEST(ARR({1, 3}), ARR({2}), 2.0);
  TEST(ARR({1, 2}), ARR({3, 4}), 2.5);
  TEST(ARR({}), ARR({}), 0);
  TEST(ARR({}), ARR({1}), 1);

  return EXIT_SUCCESS;
}

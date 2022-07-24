#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>

int compare_long(long a, long b) {
  if (a < b) return -1;
  if (a > b) return 1;
  return 0;
}

int compare_long0(void const* a, void const* b) {
  return compare_long(*((long*)a), *((long*)b));
}

int find_index(int n, long nums[static n], double x) {
  int lo = 0;
  int hi = n - 1;

  while (lo <= hi) {
    int mid = (lo + hi) / 2;

    if (nums[mid] < x) {
      lo = mid + 1;
    } else if (nums[mid] > x) {
      hi = mid - 1;
    }
  }

  return lo;
}

int count_range_sum(int lo, int hi, int nums[static hi], int lower, int upper) {
  if (lo > hi) {
    return 0;
  }

  int mid = (lo + hi) / 2;
  int prefix_len = hi - mid + 1;
  long* prefix = malloc(prefix_len * sizeof(prefix[0]));
  long sum = 0;
  int count = 0;

  prefix[0] = 0;

  for (int i = 1; i < prefix_len; i++) {
    sum += nums[mid + i];
    prefix[i] = sum;
  }

  qsort(prefix, prefix_len, sizeof(prefix[0]), compare_long0);

  sum = 0;

  for (int i = mid; i >= lo; i--) {
    sum += nums[i];
    int left = find_index(prefix_len, prefix, lower - sum - 0.5);
    int right = find_index(prefix_len, prefix, upper - sum + 0.5);
    count += right - left;
  }

  free(prefix);

  return count + count_range_sum(lo, mid - 1, nums, lower, upper) + count_range_sum(mid + 1, hi, nums, lower, upper);
}

int countRangeSum(int* nums, int numsSize, int lower, int upper){
  return count_range_sum(0, numsSize - 1, nums, lower, upper);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

#define ARR(...) { __VA_ARGS__ }
#define TEST(ARRAY, LOWER, UPPER, EXPECTED) \
  do { \
    int arr[] = ARRAY; \
    int n = sizeof(arr) / sizeof(arr[0]); \
    int sum = countRangeSum(arr, n, +(LOWER), +(UPPER)); \
    assert(sum == +(EXPECTED)); \
  } while (false);

  TEST(ARR(-2, 5, -1), -2, 2, 3);
  TEST(ARR(0), 0, 0, 1);

  return EXIT_SUCCESS;
}

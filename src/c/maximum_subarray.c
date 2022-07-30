#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>

int max(int a, int b, int c) {
  if (a > b && a > c) {
    return a;
  }
  if (b > c) {
    return b;
  }
  return c;
}

int max_sub_array(int const lo, int const hi, int const nums[restrict const static hi]) {
  if (hi < lo) {
    return 0;
  }

  int const mid = (lo + hi) / 2;

  int max_sum_left = 0;
  int sum_left = 0;
  for (int i = mid - 1; i >= lo; i--) {
    sum_left += nums[i];
    if (sum_left > max_sum_left) {
      max_sum_left = sum_left;
    }
  }

  int max_sum_right = 0;
  int sum_right = 0;
  for (int i = mid + 1; i <= hi; i++) {
    sum_right += nums[i];
    if (sum_right > max_sum_right) {
      max_sum_right = sum_right;
    }
  }

  int max_sum = nums[mid] + max_sum_left + max_sum_right;
  return max(max_sum, max_sub_array(mid + 1, hi, nums), max_sub_array(lo, mid - 1, nums));
}

int maxSubArray(int* nums, int numsSize) {
  return max_sub_array(0, numsSize - 1, nums);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));
  return EXIT_SUCCESS;
}

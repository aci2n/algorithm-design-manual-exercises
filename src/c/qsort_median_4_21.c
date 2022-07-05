#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>
#include <float.h>

void swap(double a[static 1], double b[static 1]) {
  double temp = *a;
  *a = *b;
  *b = temp;
}

size_t qsort_partition(size_t const lo, size_t const hi, double arr[restrict static hi - lo]) {
  size_t const piv = hi - 1;
  size_t p = lo;

  for (size_t i = lo; i < hi; i++) {
    if (arr[i] < arr[piv]) {
      swap(&arr[i], &arr[p++]);
    }
  }

  swap(&arr[piv], &arr[p]);

  return p;
}

double qsort_median(size_t const n, double arr[restrict static n]) {
  size_t const m0 = (n - 1) / 2;
  size_t const m1 = n / 2;
  size_t lo = 0;
  size_t hi = n;
  
  while (true) {
    size_t p = qsort_partition(lo, hi, arr);

    if (p > m0) {
      hi = p;
    } else if (p < m0) {
      lo = p + 1;
    } else {
      break;
    }
  }

  if (m0 == m1) {
    return arr[m0];
  }

  double min = DBL_MAX;

  for (size_t i = m1; i < n; i++) {
    if (arr[i] < min) {
      min = arr[i];
    }
  }

  return (arr[m0] + min) / 2;
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

  double arr[] = { 6, 5, 1, 3, 2, 4 };
  double median = qsort_median(sizeof(arr) / sizeof(arr[0]), arr);

  assert(median == 3.5);

  return EXIT_SUCCESS;
}

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>
#include <stdint.h>

double p_k_heads0(size_t n, double p[static n], size_t k, size_t i, double product) {
  if (i >= n) {
    return product;
  }

  double sum = 0;

  if (k < n - i) {
    sum += p_k_heads0(n, p, k, i + 1, product * (1 - p[i]));
  }

  if (k > 0) {
    sum += p_k_heads0(n, p, k - 1, i + 1, product * p[i]);
  }

  return sum;
}

double p_k_heads(size_t n, double p[static n], size_t k) {
  return p_k_heads0(n, p, k, 0, 1);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));

  double p[] = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.55, 0.2, 0.1, 0.11, 0.00001 };
  size_t k = 1;
  double res = p_k_heads(sizeof(p) / sizeof(p[0]), p, k);

  printf("%f\n", res);

  return EXIT_SUCCESS;
}

#include <stdlib.h>
#include <stdio.h>

int fastpow(int b, int e) {
  if (e == 0) return 1;
  int const x = fastpow(b, e/2);
  return (e % 2 ? b : 1) * x * x;
}

int powerSumInner(int x, int n, int b) {
  if (x == 0) {
    return 1;
  }

  int count = 0; 

  for (;;) {
    int const p = fastpow(b++, n);
    if (p > x) break;
    count += powerSumInner(x - p, n, b);
  }

  return count;
}

int powerSum(int x, int n) {
  return powerSumInner(x, n, 1);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  printf("powerSum: %d\n", powerSum(atoi(argv[1]), atoi(argv[2])));
  return EXIT_SUCCESS;
}

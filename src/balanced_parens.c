#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

bool balanced_parens(char const s[static 1], size_t* pos) {
  size_t const n = strlen(s);
  int balance = 0;
  size_t i = 0;
  
  for (; i < n; i++) {
    switch (s[i]) {
      case '(': balance++; break;
      case ')': balance--; break;
    }
    if (balance < 0) break;
  }

  if (balance < 0) {
    if (pos) *pos = i;
    return false;
  }

  if (balance > 0) {
    if (pos) *pos = n - balance;
    return false;
  }

  if (pos) *pos = n;
  return true;
}

int main(int const argc, char const*const argv[static argc + 1]) {
  if (argc < 2) return EXIT_FAILURE;
  size_t pos = 0;
  bool balanced = balanced_parens(argv[1], &pos);
  printf("%s (%lu)\n", balanced ? "balanced" : "not balanced", pos);
  return EXIT_SUCCESS;
}

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

bool balanced_parens(char const s[static 1], size_t* pos) {
  size_t const n = strlen(s);
  size_t stack[n];
  size_t stack_size = 0;
  
  for (size_t i = 0; i < n; i++) {
    char const c = s[i];

    if (c == '(') {
      stack[stack_size++] = i;
    } else if (c == ')') {
      if (stack_size == 0) {
        if (pos) *pos = i;
        return false;
      }
      stack_size--;
    }
  }

  if (stack_size > 0) {
    if (pos) *pos = stack[0];
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

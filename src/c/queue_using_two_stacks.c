#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>

typedef struct stack stack;
struct stack {
  int val;
  stack* prev;
};

void stack_add(stack** s, int val) {
  stack* curr = malloc(sizeof(stack));
  *curr = (stack) { .val = val, .prev = *s };
  *s = curr;
}

int stack_pop(stack** s) {
  int val = (*s)->val;
  *s = (*s)->prev;
  return val;
}

int stack_peek(stack* s) {
  return s->val;
}

void queue_enqueue(stack** a, int val) {
  stack_add(a, val);
}

void queue_flush(stack** a, stack** b) {
  if (!*b) {
    while (*a) {
      stack_add(b, stack_pop(a));
    }
  }
}

int queue_dequeue(stack** a, stack** b) {
  queue_flush(a, b);
  return stack_pop(b);
}

int queue_peek(stack** a, stack** b) {
  queue_flush(a, b);
  return stack_peek(*b);
}

int main(int const argc, char const*const argv[static argc + 1]) {
  size_t cmd = 0;
  int val = 0;
  size_t n = 0;
  stack* a = 0;
  stack* b = 0;

  if (!scanf("%lu", &n)) {
    return EXIT_FAILURE;
  }

  while (n-- > 0) {
    if (!scanf("%lu", &cmd)) {
      return EXIT_FAILURE;
    }

    switch (cmd) {
      case 1:
        if (!scanf("%d", &val)) {
          return EXIT_FAILURE;
        }
        queue_enqueue(&a, val);
        break;
      case 2:
        queue_dequeue(&a, &b);
        break;
      case 3:
        printf("%d\n", queue_peek(&a, &b));
        fflush(stdout);
        break;
    }
  }

  return EXIT_SUCCESS;
}

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <time.h>

struct SinglyLinkedListNode {
  int data;
  SinglyLinkedListNode* next;
};

typedef SinglyLinkedListNode list;

bool has_cycle(list* head) {
  if (!head) {
    return false;
  }

  list* slow = head;
  list* fast = head->next;

  while (slow && fast) {
    if (slow == fast) {
      return true;
    }
    slow = slow->next;
    fast = fast->next ? fast->next->next : 0;
  }

  return false;
}

int main(int const argc, char const*const argv[static argc + 1]) {
  srand(time(0));
  return EXIT_SUCCESS;
}

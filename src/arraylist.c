#include <stdlib.h>
#include <stdio.h>

typedef struct arrlist arrlist;
struct arrlist {
  size_t len;
  size_t cap;
  size_t sz;
  void** arr;
};

arrlist* arrlist_add(arrlist* a, void* el) {
  if (!a) {
    return 0;
  }
  if (a->len == a->cap) {
    printf("before realloc: [len=%lu,cap=%lu]\n", a->len, a->cap);
    a->cap <<= 2;
    a->arr = realloc(a->arr, a->cap * a->sz);
    printf("after realloc: [len=%lu,cap=%lu]\n", a->len, a->cap);
  }
  a->arr[a->len++] = el;
  return a;
}

void* arrlist_get(arrlist* a, size_t i) {
  if (!a || i >= a->len) {
    return 0;
  }
  return a->arr[i];
}

arrlist* arrlist_new(size_t cap, size_t sz) {
  arrlist* a = malloc(sizeof(arrlist));
  size_t adj_cap = 1;
  while (adj_cap < cap) {
    adj_cap <<= 2;
  }
  *a = (arrlist) {
    .len = 0,
    .cap = adj_cap,
    .sz = sz,
    .arr = malloc(adj_cap * sz)
  };
  return a;
}

void arrlist_destroy(arrlist* a) {
  if (!a) {
    return;
  }
  free(a->arr);
  free(a);
}

typedef struct person person;
struct person {
  char const* name;
  size_t age;
  size_t height;
};

int main() {
  arrlist* a = arrlist_new(12, sizeof(person*));

  person* p = malloc(sizeof(person));
  *p = (person){ .name = "Alvaro", .age = 28, .height = 181 };
  arrlist_add(a, p);
  person* p1 = arrlist_get(a, 0);
  printf("%s (%lu, %lucm)\n", p->name, p->age, p->height);

  for (size_t i = 0; i < 100000; i++) {
    person* pp = malloc(sizeof(person));
    *pp = (person){ .name = "dong", .age = i, .height = i };
    arrlist_add(a, pp);
  }

  for (size_t i = 0; i < a->len; i++) {
    person* pp = arrlist_get(a, i);
    // printf("%s (%lu, %lucm)\n", pp->name, pp->age, pp->height);
  }

  arrlist_destroy(a);

  return EXIT_SUCCESS;
}

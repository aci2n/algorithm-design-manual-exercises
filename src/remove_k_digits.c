#include <stdlib.h>
#include <string.h>
#include <stdio.h>

typedef struct node node;
typedef struct node {
  char val;
  size_t idx;
  node* left;
  node* right;
}

void minheap_add(node** root, char val, size_t idx) {
  node* n = *root;

  if (!n) {
    n = *root = malloc(sizeof(node));
    *n = (node){ .val = val, .idx = idx };
    return;
  }

  char tval = n->val;
  size_t tidx = n->idx;

  if (val < n->val) {
    n->val = val;
    n->idx = idx;
    minheap_add(&n->left, tval, tidx);
  } else if (n->left && n->left.val 
}

char * removeKdigits(char * num, int k) {
    if (!num || k <= 0) return 0;

    int const num_len = strlen(num);
    
    if (k > num_len) return 0;
    if (k == num_len) return "0";
    
    size_t const res_len = num_len - k;
    char* res = malloc(sizeof(res[0]) * res_len + 1);
    res[res_len] = 0;
    
    for (size_t i = 0, j = 0; i < res_len; i++) {   
        size_t min_val = '9' + 1;
        size_t min_idx = 0;
        
        for (; j <= k + i; j++) {
            if (num[j] < min_val) {
                min_val = num[j];
                min_idx = j;
            }
        }
        
        res[i] = min_val;
        j = min_idx + 1;
    }
    
    size_t z = 0;
    for (; z < res_len && res[z] == '0'; z++);
    
    if (z == res_len) return "0";
        
    return res + z;
}

int main(int argc, char const*const argv[static argc + 1]) {
  #define TEST(NUM, K) printf("%s: %s\n", "" NUM "", removeKdigits("" NUM "", (K)+0));
  char* num = argv[1];
  int k = atoi(argv[2]);
  printf("%s, %d: %s\n", num, k, removeKdigits(num, k));
  return EXIT_SUCCESS;
}

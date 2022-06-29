#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>

typedef struct node node;
struct node {
  int* trail;
  node* prev;
};

/**
 * Return an array of arrays of size *returnSize.
 * The sizes of the arrays are returned as *returnColumnSizes array.
 * Note: Both returned array and *columnSizes array must be malloced, assume caller calls free().
 */
int** fourSum(int* nums, int numsSize, int target, int* returnSize, int** returnColumnSizes){
  size_t const n = numsSize;
  int** cache[n][n];
  node* curr = 0;

  for (size_t i = 0; i < n; i++) {
    int a = nums[i];

    for (size_t j = i + 1; j < n; j++) {
      int b = nums[j];

      for (size_t k = j + 1; k < n; k++) {
        int c = nums[k];

        for (size_t l = k + 1; l < n; l++) {
          int d = nums[l];

          if (a + b + c + d == target) {
            int* trail = malloc(sizeof(int) * 4);
            trail[0] = a; trail[1] = b; trail[2] = c; trail[3] = d;
            node* prev = curr;

            curr = malloc(sizeof(node));
            *curr = (node){.trail = trail, .prev = prev};
            *returnSize += 1;
          }
        }
      }
    }
  }
  
  *returnColumnSizes = malloc(sizeof(int) * (*returnSize));
  int** ans = malloc(sizeof(int*) * (*returnSize));

  for (size_t i = 0; i < *returnSize; i++) {
    (*returnColumnSizes)[i] = 4;
    ans[i] = curr->trail;

    node* temp = curr;
    curr = curr->prev;
    free(temp);
  }

  return ans;
}

int main() {
  //int nums[] = {1, 0, -1, 0, -2, 2};
  //int numsSize = sizeof(nums) / sizeof(nums[0]);
  //int target = 0;

  int nums[] = {2, 2, 2, 2, 2};
  int numsSize = sizeof(nums) / sizeof(nums[0]);
  int target = 8;

  int returnSize = 0;
  int* returnColumnSizes = 0;
  int** ans = fourSum(nums, numsSize, target, &returnSize, &returnColumnSizes);

  for (size_t i = 0; i < returnSize; i++) {
    int columnSize = returnColumnSizes[i];
    for (size_t j = 0; j < columnSize; j++) {
      printf("%d ", ans[i][j]);
    }
    printf("\n");
  }

  return EXIT_SUCCESS;
}

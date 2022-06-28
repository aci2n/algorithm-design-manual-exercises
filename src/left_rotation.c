#include <stdlib.h>

int* rotateLeft(int d, int arr_count, int* arr, int* result_count) {
  if (arr_count <= 0 || !arr) {
    *result_count = 0;
    return 0;
  }

  int* result = malloc(sizeof(arr[0]) * arr_count);
  
  for (size_t i = 0; i < arr_count; i++) {
    result[(arr_count-d+i)%arr_count] = arr[i];
  }

  *result_count = arr_count;
  return result;
}

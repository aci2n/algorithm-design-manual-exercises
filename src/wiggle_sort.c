#include <stdlib.h>
#include <math.h>
#include <string.h>

int compare(const void* a, const void* b) {
    int* A = (int*) a;
    int* B = (int*) b;
    if (a < b) return -1;
    if (a > b) return 1;
    return 0;
}

void wiggleSort(int* nums, int numsSize){
    int sorted[numsSize];
    
    memcpy(sorted, nums, sizeof(nums[0]) * numsSize);
    qsort(sorted, numsSize, sizeof(nums[0]), compare);
    
    const size_t n = ceil(numsSize / 2.0);
    
    for (size_t i = 0; i < n; i++) {
        const size_t i0 = i * 2;
        const size_t i1 = i0 + 1;

        nums[i0] = sorted[n-i];

        if (i1 < numsSize) {
            nums[i1] = sorted[numsSize-i-1];
        }
    }
}

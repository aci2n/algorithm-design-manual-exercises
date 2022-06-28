#include <stdio.h>
#include <stdlib.h>

/**
 * Note: The returned array must be malloced, assume caller calls free().
 */
int* countBits(int n, int* returnSize){
    if (n < 0) {
        *returnSize = 0;
        return 0;
    }
    
    *returnSize = n + 1;
    
    int* ans = malloc(sizeof(int) * (*returnSize));
    
    ans[0] = 0;
    
    for (size_t i = 1, offset = 1, limit = 2; i <= n; i++) {
        ans[i] = 1 + ans[i - offset];
        
        if (i == limit - 1) {
            offset = limit;
            limit *= 2;
        }
    }
    
    return ans;
}
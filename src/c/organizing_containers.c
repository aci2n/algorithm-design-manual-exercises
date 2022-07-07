 int size_compare(void const* a, void const* b) {
     size_t A = *(size_t*)a;
     size_t B = *(size_t*)b;
     return A > B ? 1 : A < B ? -1 : 0;
 }
 
char* organizingContainers(int container_rows, int container_columns, int** container) {
    if (!container || container_rows <= 0 || container_columns <= 0 || container_rows != container_columns) {
        return "Impossible";
    }
    
    char* ret = 0;
    size_t const n = container_rows;
    size_t* const row_sums = calloc(sizeof(row_sums[0]), n);
    size_t* const col_sums = calloc(sizeof(col_sums[0]), n);
    
    for (size_t i = 0; i < n; i++) {
        for (size_t j = 0; j < n; j++) {
            row_sums[i] += container[i][j];
            col_sums[j] += container[i][j];
        }
    }
    
    qsort(row_sums, n, sizeof(row_sums[0]), size_compare);
    qsort(col_sums, n, sizeof(col_sums[0]), size_compare);
    
    for (size_t i = 0; i < n; i++) {
        if (row_sums[i] != col_sums[i]) {
            ret = "Impossible";
            goto done;
        }
    }
    
    ret = "Possible";
    
done:
    free(row_sums);
    free(col_sums);
    return ret;
}
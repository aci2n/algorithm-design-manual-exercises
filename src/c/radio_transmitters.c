int compare_int(void const* a, void const* b) {
	int* A = (int*) a;
	int* B = (int*) b;
	if (*A < *B) return -1;
	if (*A > *B) return 1;
	return 0;
}

int hackerlandRadioTransmitters(int x_count, int* x, int k) {
    if (x_count <= 0 || !x || k <= 0) {
        return 0;
    }
    
    qsort(x, x_count, sizeof(x[0]), compare_int);
    
    int transmitters = 0;
    
    for (size_t i = 0, j = 0; i < x_count; transmitters++) {
        j = i + 1;
        while (j < x_count && x[j] - x[i] <= k) {
            j++;
        }
        
        i = j--;
        while (i < x_count && x[i] - x[j] <= k) {
            i++;
        }
    }
    
    return transmitters;
}
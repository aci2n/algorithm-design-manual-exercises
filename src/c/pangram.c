#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>

#define NUM_CHARS 'Z' - 'A' + 1
 
char* pangrams(char* s) {
    if (!s) goto not_pangram;
    size_t const len = strlen(s);
    if (len == 0) goto not_pangram;
    
    // 0 to 25
    bool seen[NUM_CHARS] = {0};
    size_t found = 0;
    
    for (size_t i = 0; i < len; i++) {
        char c = s[i];
        
        if (c >= 'a' && c <= 'z') {
            c -= ('a' - 'A');
        }
        
        if (c >= 'A' && c <= 'Z' && !seen[c - 'A']) {
            seen[c - 'A'] = true;
        
            if (++found == NUM_CHARS) {
                return "pangram";
            } 
        }
    }

not_pangram:
    return "not pangram";
}

int main() {
  printf("%s\n", pangrams("We promptly judged antique ivory buckles for the next prize"));
  return EXIT_SUCCESS;
}

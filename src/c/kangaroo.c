#include <stdlib.c>

char* kangaroo(int x1, int v1, int x2, int v2) {
  if (x1 == x2) return "YES";
  if (v1 == v2) return "NO";
  
  const int x_diff = x1 - x2;
  const int v_diff = v2 - v1;
  const int t = x_diff / v_diff;
  const int mod = x_diff % v_diff;
  
  if (mod == 0 && t > 0) {
    return "YES";
  }
  
  return "NO";
}
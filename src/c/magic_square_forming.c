#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>

void rotate_matrix(int n, int src[n][n], int dst[n][n]) {
  size_t cycles = n / 2 + n % 2;
  size_t m = n - 1;

  for (size_t i = 0; i < cycles; i++) {
    for (int j = i; j < m; j++) {
      dst[i][j] = src[m-j][i];
      dst[m-j][i] = src[m-i][m-j];
      dst[m-i][m-j] = src[j][m-i];
      dst[j][m-i] = src[i][j];
    }
  }
}

void transpose_matrix(int rows, int cols, int src[rows][cols], int dst[cols][rows]) {
  for (size_t i = 0; i < rows; i++) {
    for (size_t j = 0; j < cols; j++) {
      dst[j][i] = src[i][j];
    }
  }
}

void init_magic_squares(int magic_squares[8][3][3]) {
  magic_squares[0][0][0] = 4;
  magic_squares[0][0][1] = 9;
  magic_squares[0][0][2] = 2;
  magic_squares[0][1][0] = 3;
  magic_squares[0][1][1] = 5;
  magic_squares[0][1][2] = 7;
  magic_squares[0][2][0] = 8;
  magic_squares[0][2][1] = 1;
  magic_squares[0][2][2] = 6;
  for (size_t i = 1; i < 4; i++) {
    rotate_matrix(3, magic_squares[i-1], magic_squares[i]);
  }
  for (size_t i = 0; i < 4; i++) {
    transpose_matrix(3, 3, magic_squares[i], magic_squares[i+4]);
  }
}

size_t magic_square_diff(int** s, int magic_square[3][3]) {
  size_t diff = 0;
  for (size_t i = 0; i < 3; i++) {
    for (size_t j = 0; j < 3; j++) {
      diff += abs(s[i][j] - magic_square[i][j]);
    }
  }
  return diff;
}

int formingMagicSquare(int s_rows, int s_columns, int** s) {
  static int magic_squares[8][3][3] = {0};
  static bool magic_squares_initialized = false;

  if (!magic_squares_initialized) {
    init_magic_squares(magic_squares);   
  }

  size_t min_diff = 1 << 20;

  for (size_t i = 0; i < 8; i++) {
    size_t diff = magic_square_diff(s, magic_squares[i]);
    if (diff < min_diff) {
      min_diff = diff;
    }
  }

  return min_diff;
}

int main() {
  int** m = malloc(sizeof(int*) * 3);

  m[0] = malloc(sizeof(int) * 3);
  m[1] = malloc(sizeof(int) * 3);
  m[2] = malloc(sizeof(int) * 3);

  m[0][0] = 2;
  m[0][1] = 2;
  m[0][2] = 7;
  m[1][0] = 8;
  m[1][1] = 6;
  m[1][2] = 7;
  m[2][0] = 1;
  m[2][1] = 2;
  m[2][2] = 9;

  printf("%d\n", formingMagicSquare(3, 3, m));

  return EXIT_SUCCESS;
}

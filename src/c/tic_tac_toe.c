#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <stdbool.h>

/*
3-8. [5] Tic-tac-toe is a game played on an n × n board (typically n = 3) where two
players take consecutive turns placing “O” and “X” marks onto the board cells.
The game is won if n consecutive “O” or ‘X” marks are placed in a row, column,
or diagonal. Create a data structure with O(n) space that accepts a sequence
of moves, and reports in constant time whether the last move won the game.
*/

// space: O(n + n + 2) = O(n)
// holds counters initialized at n for each row and the two diagonals
typedef struct ttt_state ttt_state;
struct ttt_state {
  size_t* rows;
  size_t* cols;
  size_t diaglr;
  size_t diagrl;
};

ttt_state* ttt_state_new(size_t ttt_size) {
  ttt_state* s = malloc(sizeof(ttt_state));
  *s = (ttt_state){
    .rows = malloc(sizeof(size_t) * ttt_size),
    .cols = malloc(sizeof(size_t) * ttt_size),
    .diaglr = ttt_size,
    .diagrl = ttt_size,
  };
  for (size_t i = 0; i < ttt_size; i++) {
    s->rows[i] = s->cols[i] = ttt_size;
  }
  return s;
}

void ttt_state_destroy(ttt_state* s) {
  if (s) {
    free(s->rows);
    free(s->cols);
    free(s);
  }
}

bool ttt_winner(size_t ttt_size, size_t moves_size, size_t moves[moves_size][2]) {
  ttt_state* players[] = {ttt_state_new(ttt_size), ttt_state_new(ttt_size)};
  bool winner = false;

  for (size_t i = 0; i < moves_size; i++) {
    size_t x = moves[i][0];
    size_t y = moves[i][1];
    ttt_state* s = players[i % 2];

    // the last move will be a winner if it gets any row/col/diag counter to 0
    winner = false;
    winner |= !--s->rows[x];
    winner |= !--s->cols[y];
    if (x == y) winner |= !--s->diaglr;
    if (x + y == ttt_size - 1) winner |= !--s->diagrl;
  }

  ttt_state_destroy(players[0]);
  ttt_state_destroy(players[1]);

  return winner;
}

int main() {
  size_t moves[][2] = {
    {1, 1},
    {2, 2},
    {2, 1},
    {0, 0},
    {2, 0},
    {0, 2},
    {0, 1}
  };
  bool winner = ttt_winner(3, sizeof(moves) / sizeof(moves[0]), moves);
  printf("last move is winner? %s\n", winner ? "yes" : "no");
  return EXIT_SUCCESS;
}

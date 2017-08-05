import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private final int[] board;
    private final int N;
    private int manhattan;

    public Board(int[][] blocks) {
        N = blocks.length;
        board = new int[N * N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i * N + j] = blocks[i][j];
            }
        }
        manhattan = -1;
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int distance = 0;
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] != i + 1) {
                distance++;
            }
        }

        return distance;
    }

    public int manhattan() {
        // Cache computation
        if (manhattan > -1) {
            return manhattan;
        }
        int distance = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // For all cell values except for 0 we simply compare its value-1 (due to array numbering)
                // to its coordinates, since this two value must be equal. We must handle value of 0
                // as it does not fall into logic
                if (!(board[i * N + j] == 0 && i * N + j != board.length)) {
                    distance += manhattanDistance(board[i * N + j] - 1, i * N + j);
                }
            }
        }
        manhattan = distance;
        return manhattan;
    }

    public boolean isGoal() {
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    private int manhattanDistance(int currentPosition, int correctPosition) {
        // Calculate coordinates in two dimensional plane
        int jCurrent = currentPosition % N;
        int jCorrect = correctPosition % N;
        int iCurrent = currentPosition / N;
        int iCorrect = correctPosition / N;
        return Math.abs(iCurrent - iCorrect) + Math.abs(jCorrect - jCurrent);
    }

    private void swap(int i1, int j1, int i2, int j2, int[][] board) {
        int temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
    }


    private int[][] oneDtoTwoD() {
        int[][] newBoard = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                newBoard[i] = Arrays.copyOfRange(board, i * N, (i + 1) * N);
            }
        }
        return newBoard;
    }

    public Board twin() {
        int[][] twinBoard = oneDtoTwoD();
        // We can assume that N is at least 2 otherwise there would be no board.
        // We will swap first two cells, otherwise, if one of the cells contains
        // zero we will swap last two
        if (twinBoard[0][0] != 0 && twinBoard[0][1] != 0) {
            swap(0, 0, 0, 1, twinBoard);
        } else {
            swap(N - 1, N - 1, N - 1, N - 2, twinBoard);
        }

        return new Board(twinBoard);
    }

    public boolean equals(Object y) {
        if (y == null) return false;

        if (!(y instanceof Board)) return false;

        Board b = (Board) y;

        if (b.N != N) return false;

        for (int i = 0; i < N * N; i++) {
            if (b.board[i] != board[i]) {
                return false;
            }
        }
        return true;
    }

    private class BoardIterable implements Iterable<Board> {

        @Override
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }

        private class BoardIterator implements Iterator<Board> {
            private Board[] boards;
            private int numBoards;

            BoardIterator() {
                // There are at most 4 possible neighbours for each board
                boards = new Board[4];
                numBoards = 0;
                // First find where empty cell is located
                int zeroPosition = 0;
                for (int i = 0; i < N * N; i++) {
                    if (board[i] == 0) {
                        zeroPosition = i;
                        break;
                    }
                }


                int j = zeroPosition % N;
                int i = zeroPosition / N;


                // Can move empty space one cell top
                if (i > 0) {
                    addNewBoard(i, j, i - 1, j);
                }

                // Can move empty space one cell bottom
                if (i < N - 1) {
                    addNewBoard(i, j, i + 1, j);
                }

                // Can move empty space one cell left
                if (j > 0) {
                    addNewBoard(i, j, i, j - 1);
                }

                // Can move empty space one cell right
                if (j < N - 1) {
                    addNewBoard(i, j, i, j + 1);
                }
            }

            private void addNewBoard(int i, int j, int inew, int jnew) {
                int[][] newBoard = oneDtoTwoD();
                swap(i, j, inew, jnew, newBoard);
                boards[numBoards] = new Board(newBoard);
                numBoards++;
            }

            @Override
            public boolean hasNext() {
                return numBoards > 0;
            }

            @Override
            public Board next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return boards[--numBoards];
            }
        }
    }

    public Iterable<Board> neighbors() {
        return new BoardIterable();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i * N + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private WeightedQuickUnionUF percolation;
	private WeightedQuickUnionUF union;
	// Holds information about whether cell has been opened
	private boolean[] grid;
	// Holds matrix's dimension. Used to transform matrix coordinates
	// to array index.
	private int dim;

	// By agreement fist row is top row
	private final int virtualTop = 0;
	private int virtualBottom;

	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("n should be positive number");
		}
		// This UF contains two additional cells: virtual top and virtual bottom
		// which will be used to tell if system percolates
		percolation = new WeightedQuickUnionUF(n * n + 2);
		// This UF contains one additional cell: virtual top
		// which will be use to tell if specific cell is full.
		// We cannot use percolation variable because in this cause once system
		// percolates
		// every cell connected to the bottom row will be full (because bottom
		// row
		// connected to virtual bottom
		// and virtual bottom is connected to virtual top)
		union = new WeightedQuickUnionUF(n * n + 1);
		dim = n;
		grid = new boolean[dim * dim];
		// By placing virtual bottom at position dim*dim + 1 we preserve
		// natural numbering of matrix elements that is from 1 to dim*dim
		virtualBottom = dim * dim + 1;
	}

	private void updateQU(int coord1, int coord2) {
		percolation.union(coord1, coord2);
		union.union(coord1, coord2);
	}

	public void open(int row, int col) {
		int coord = getCoord(row, col);
		if (!isOpen(row, col)) {
			// getCoord returns coordinate in [1, n*n] range,
			// therefore we must compensate for that
			grid[coord - 1] = true;
		} else {
			return;
		}

		// Connect open site in first row to virtual top
		if (row == 1) {
			updateQU(virtualTop, coord);
		}

		// Connect open site in nth row to virtual bottom
		if (row == dim) {
			percolation.union(virtualBottom, coord);
		}

		// Connect to open bottom site if there is one
		if (row > 1) {
			int bottomrow = row - 1;
			if (isOpen(bottomrow, col)) {
				updateQU(getCoord(bottomrow, col), coord);
			}
		}

		// Connect to open top site if there is one
		if (row < dim) {
			int toprow = row + 1;
			if (isOpen(toprow, col)) {
				updateQU(getCoord(toprow, col), coord);
			}
		}

		// Connect to open left site is there is one
		if (col > 1) {
			int leftcolumn = col - 1;
			if (isOpen(row, leftcolumn)) {
				updateQU(getCoord(row, leftcolumn), coord);
			}
		}

		// Connect to open right site if there is one
		if (col < dim) {
			int rightcolumn = col + 1;
			if (isOpen(row, rightcolumn)) {
				updateQU(getCoord(row, rightcolumn), coord);
			}
		}

	}

	public boolean isOpen(int row, int col) {
		// getCoord will return coordinates in [1, n*n] range.
		// To compensate for that we shall subtract one from coordinate.
		return grid[getCoord(row, col) - 1];
	}

	public boolean isFull(int row, int col) {
		// Cell is full if its connected to virtual top
		return union.connected(getCoord(row, col), virtualTop);
	}

	public int numberOfOpenSites() {
		int sites = 0;
		for (boolean b : grid) {
			if (b) {
				sites++;
			}
		}
		return sites;
	}

	public boolean percolates() {
		// Check that at least one socket in bottom row
		// is connected to virtual bottom
		return percolation.connected(virtualBottom, virtualTop);
	}

	public static void main(String[] args) {
		Percolation p = new Percolation(2);
		p.open(1, 1);
		p.open(2, 1);
		System.out.println("Percolates: " + p.numberOfOpenSites());
	}

	private int getCoord(int row, int col) {
		if (row <= 0 || row > dim || col <= 0 || col > dim)
			throw new IndexOutOfBoundsException("row and column must be in [1,n] range");
		// Returns coordinates in [1, n*n] range
		return (row - 1) * dim + col;
	}
}

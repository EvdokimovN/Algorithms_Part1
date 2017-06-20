import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	// Holds experiment results
	private double[] stats;
	// Number of experiment
	private int numTrials;
	// Caches mean and standard deviation calculations
	private double mean;
	private double std;

	public PercolationStats(int n, int trials) {
		if (n <= 0 || trials <= 0) {
			throw new IllegalArgumentException();
		}
		mean = Double.NaN;
		std = Double.NaN;
		numTrials = trials;
		stats = new double[trials];
		for (int i = 0; i < trials; i++) {
			Percolation p = new Percolation(n);
			while (!p.percolates()) {
				int row = StdRandom.uniform(1, n + 1);
				int col = StdRandom.uniform(1, n + 1);
				p.open(row, col);
			}
			stats[i] = (double) p.numberOfOpenSites() / (double) (n * n);
		}
	}

	public double mean() {
		if (Double.isNaN(mean)) {
			mean = StdStats.mean(stats);
		}
		return mean;
	}

	public double stddev() {
		if (Double.isNaN(std)) {
			std = StdStats.stddevp(stats);
		}
		return std;
	}

	public double confidenceLo() {
		return mean() - 1.96 * stddev() / Math.sqrt((double) numTrials);
	}

	public double confidenceHi() {
		return mean() + 1.96 * stddev() / Math.sqrt((double) numTrials);
	}

	public static void main(String[] args) {
	}
}

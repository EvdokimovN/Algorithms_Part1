import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {
    private final ArrayList<Board> path;
    private Node solutionNode;
    private boolean solvable;


    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial board must not be null");
        }

        // Create object that will hold solution sequence
        path = new ArrayList<>();


        // Only boards with even number of permutations are solvable
        // (https://www.math.utah.edu/mathcircle/notes/permutations.pdf)
        // Instead of checking that we will try to solve twin board which
        // achieve by swapping two random cells
        MinPQ<Node> frontier = new MinPQ<>(boardComparator);
        Node node = new Node(initial, null, 0);
        frontier.insert(node);

        MinPQ<Node> twinFrontier = new MinPQ<>(boardComparator);
        Node twinNode = new Node(initial.twin(), null, 0);
        twinFrontier.insert(twinNode);


        boolean twinSolvable = false;
        solvable = false;


        // We will run A* algorithm until one of the boards
        // is solvable
        while (!twinSolvable && !solvable) {
            twinSolvable = aStar(twinFrontier);
            solvable = aStar(frontier);
        }

        // Entangle end node to solution sequence
        while (solutionNode != null) {
            path.add(solutionNode.board);
            solutionNode = solutionNode.parent;
        }
    }


    private boolean aStar(MinPQ<Node> frontier) {

        Node nextBoard = frontier.delMin();
        nextBoard.moves++;

        if (nextBoard.board.isGoal()) {
            solutionNode = nextBoard;
            return true;
        }

        for (Board neighbor : nextBoard.board.neighbors()) {
            if (nextBoard.parent == null || !neighbor.equals(nextBoard.parent.board)) {
                Node newNode = new Node(neighbor, nextBoard, nextBoard.moves);
                frontier.insert(newNode);
            }
        }

        return false;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return path.size() - 1;
    }

    private class Node {
        Board board;
        Node parent;
        int moves;

        Node(Board board, Node parent, int moves) {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
        }


        int priority() {
            return moves + board.manhattan();
        }
    }

    private static Comparator<Node> boardComparator = new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.priority() - o2.priority();
        }
    };

    private class BoardIterable implements Iterable<Board> {

        @Override
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }


        private class BoardIterator implements Iterator<Board> {
            private int i;


            BoardIterator() {
                i = path.size();
            }

            @Override
            public boolean hasNext() {
                return i > 0;
            }

            @Override
            public Board next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return path.get(--i);
            }
        }
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return new BoardIterable();
    }
}
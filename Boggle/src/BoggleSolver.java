import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {

    private final BoggleTrie<Integer> dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        validateNonNull(dictionary);
        this.dictionary = new BoggleTrie<>();
        for (String word : dictionary) {
            validateNonNull(word);
            int length = word.length();
            int score = 0;
            switch (length) {
                case 0:
                case 1:
                case 2:
                    break;
                case 3:
                case 4:
                    score = 1;
                    break;
                case 5:
                    score = 2;
                    break;
                case 6:
                    score = 3;
                    break;
                case 7:
                    score = 5;
                    break;
                default:
                    score = 11;
            }

            this.dictionary.put(word, score);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validateNonNull(board);
        int rows = board.rows();
        int cols = board.cols();
        TreeSet<String> words = new TreeSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                bfs(board, i, j, new boolean[rows * cols], "", words);
            }
        }
        return words;
    }


    private void bfs(BoggleBoard board, int row, int col, boolean[] visited, String key, Set<String> acc) {
        int rows = board.rows();
        int cols = board.cols();
        int index = cols * row + col;

        if (visited[index]) return;
        char letter = board.getLetter(row, col);
        key = (letter != 'Q') ? key + letter : key + "QU";


        if (!dictionary.hasKeysWithPrefix(key)) return;
        if (key.length() > 2 && dictionary.contains(key)) acc.add(key);

        visited[index] = true;

        if (row < rows - 1) bfs(board, row + 1, col, visited, key, acc);
        if (row > 0) bfs(board, row - 1, col, visited, key, acc);
        if (col > 0) bfs(board, row, col - 1, visited, key, acc);
        if (col < cols - 1) bfs(board, row, col + 1, visited, key, acc);
        if (row > 0 && col > 0) bfs(board, row - 1, col - 1, visited, key, acc);
        if (row > 0 && col < cols - 1) bfs(board, row - 1, col + 1, visited, key, acc);
        if (row < rows - 1 && col > 0) bfs(board, row + 1, col - 1, visited, key, acc);
        if (row < rows - 1 && col < cols - 1) bfs(board, row + 1, col + 1, visited, key, acc);

        visited[index] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        validateNonNull(word);
        if (!this.dictionary.contains(word)) return 0;
        return this.dictionary.get(word);
    }


    private void validateNonNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }


    private static class BoggleTrie<Value> {
        private static final int R = 26;
        // A character ASCII code
        private static final int OFFSET = 65;
        // Last prefix hasKeysWithPrefix method was called
        private String lastPrefix;
        // Last node resulting from lastPrefix
        private Node lastPrefixNode;
        private Node root = new Node();


        private static class Node {
            private Object value;
            private Node[] next = new Node[R];
        }


        Value get(String key) {
            Node x = get(root, key, 0);
            if (x == null) return null;
            return (Value) x.value;
        }


        boolean contains(String key) {
            return get(key) != null;
        }

        private Node get(Node x, String key, int d) {
            if (d == key.length() || x == null) return x;
            return get(x.next[key.charAt(d) - OFFSET], key, d + 1);
        }

        void put(String key, Value v) {
            root = put(root, key, v, 0);
        }

        private Node put(Node x, String key, Value v, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                x.value = v;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - OFFSET] = put(x.next[c - OFFSET], key, v, d + 1);
            return x;
        }

        public boolean hasKeysWithPrefix(String prefix) {
            Node x;
            if (lastPrefix == null || prefix.indexOf(lastPrefix) != 0) {
                x = get(root, prefix, 0);
            } else {
                x = get(lastPrefixNode, prefix, prefix.length() - 1);
            }

            // Since most calls to this method will be with prefixes that differ
            // by one additional letter we want to update instance variables only
            // when length difference is greater than two
            if (lastPrefix == null || lastPrefix.length() < prefix.length() - 2) {
                lastPrefix = prefix;
                lastPrefixNode = x;
            }

            return (x != null && (x.value != null ||
                    collect(x)));
        }

        private boolean collect(Node x) {
            if (x == null) return false;
            if (x.value != null) return true;
            for (char c = OFFSET; c < OFFSET + R; c++) {
                boolean success = collect(x.next[c - OFFSET]);
                if (success) return true;
            }
            return false;
        }

    }
}
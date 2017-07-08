import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        int k = StdIn.readInt();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }

        Iterator<String> iterator = queue.iterator();
        while (k > 0 && iterator.hasNext()) {
            StdOut.println(iterator.next());
            k--;
        }
    }
}

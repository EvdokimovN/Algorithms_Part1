import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    /*
     * Pointers to first and last nodes of deque. Schematic implementation of
     * deque may be seen below
     * 
     * ----> next |first|oldFirst|node|oldLast|last| <---- previous
     */
    private Node first = null;
    private Node last = null;

    /*
     * Number of items on the queue. Here we trade space for performance as
     * single integer occupies 4 bytes, while traversing linked list takes O(N)
     * time
     */

    private int N = 0;

    public Deque() {
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        if (isEmpty()) {
            last = first;
        } else {
            // Now we need to update next pointer for first node
            // and previous pointer for old first node. Other pointers
            // will correctly point to null
            first.next = oldFirst;
            oldFirst.previous = first;
        }
        N++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        if (isEmpty()) {
            first = last;
        } else {
            // Now we need to update previous pointer for last node
            // and next pointer for old last node. Other pointers
            // will correctly point to null
            last.previous = oldLast;
            oldLast.next = last;
        }
        N++;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (isEmpty())
            first = last = null;
        N--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = last.item;
        last = last.previous;
        if (isEmpty())
            last = first = null;
        N--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }

    // from front to end
    public static void main(String[] args) {

    }

    private class DequeIterator implements Iterator<Item> {
        private Node node;

        DequeIterator(Node node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            // Current node's item is what we are going to
            // sample by call to next
            return node != null;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = node.item;
            node = node.next;
            return item;
        }

    }

    private class Node {
        Item item;
        Node next;
        Node previous;
    }
}

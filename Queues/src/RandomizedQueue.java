import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int N;

    @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        // This is unsafe from Java type system perspective. However in
        // our case consumer will interact with the array through predefined API
        // which uses generic parameters, so from their perspective this
        // implementation
        // Completely type safe. It's the same way java.util.ArrayList is
        // implemented
        Item[] items = (Item[]) new Object[2];
        this.items = items;
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    private int randomIndex() {
        return StdRandom.uniform(N);
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException("attemp to enqueue null item");
        // Make sure there is space to insert new item
        if (N == items.length)
            resize(2 * items.length);
        items[N++] = item;
    }

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("attempt to sample empty queue");
        return items[randomIndex()];
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("attemp to dequeue from empty queue");
        int index = randomIndex();
        Item item = items[index];
        // Shift elements to empty space
        if (index != items.length - 1) {
            System.arraycopy(items, index + 1, items, index, N - index - 1);
        }
        items[--N] = null;
        // Shrink the array if necessary
        if (N > 0 && N == items.length / 4)
            resize(items.length / 2);
        return item;
    }

    public Iterator<Item> iterator() {
        return new RandomIterator(items, N);
    }

    @SuppressWarnings("unchecked")
    private void resize(int size) {
        Item[] newItems = (Item[]) new Object[size];
        System.arraycopy(items, 0, newItems, 0, N);
        items = newItems;
    }

    private class RandomIterator implements Iterator<Item> {
        Item[] items;
        int i;

        @SuppressWarnings("unchecked")
        RandomIterator(Item[] items, int N) {
            Item[] newItems = (Item[]) new Object[N];
            System.arraycopy(items, 0, newItems, 0, N);
            this.items = newItems;
            shuffleArray(this.items);
        }

        /*
         * Fisher-Yates shuffle implementation courtesy of
         * 
         * https://stackoverflow.com/questions/1519736/random-shuffling-of-an-
         * array#answer-1520212
         */
        private void shuffleArray(Item[] ar) {
            Random r = new Random();
            for (int i = 0; i < ar.length; i++) {
                int index = r.nextInt(i + 1);
                Item a = ar[index];
                ar[index] = ar[i];
                ar[i] = a;
            }
        }

        @Override
        public boolean hasNext() {
            return i != items.length;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return items[i++];
        }

    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.dequeue();
        q.dequeue();
        q.dequeue();
    }

}

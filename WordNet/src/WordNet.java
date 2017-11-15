import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Stack;


public class WordNet {
    private final Digraph wordNet;
    private final ST<String, SET<Integer>> nouns;
    private final ST<Integer, String> codes;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        In nounsReader = new In(synsets);
        In hypernymReader = new In(hypernyms);
        nouns = new ST<>();
        codes = new ST<>();
        int V = 0;

        while (nounsReader.hasNextLine()) {
            V++;
            String[] line = nounsReader.readLine().split(",");
            Integer code = Integer.parseInt(line[0]);
            String[] words = line[1].split(" ");
            for (String word : words) {
                SET<Integer> codes = nouns.get(word);
                if (codes == null) codes = new SET<>();
                codes.add(code);
                nouns.put(word, codes);
            }
            codes.put(code, line[1]);
        }

        wordNet = new Digraph(V);
        while (hypernymReader.hasNextLine()) {
            String[] line = hypernymReader.readLine().split(",");
            Integer e = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                wordNet.addEdge(e, Integer.parseInt(line[i]));
            }
        }
        validateGraph();
        sap = new SAP(wordNet);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nouns.get(word) != null;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        Iterable<Integer> v = nouns.get(nounA);
        Iterable<Integer> w = nouns.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        Iterable<Integer> v = nouns.get(nounA);
        Iterable<Integer> w = nouns.get(nounB);
        int ancestor = sap.ancestor(v, w);
        return codes.get(ancestor);
    }

    private void validateGraph() {
        DirectedCycle directedCycle = new DirectedCycle(wordNet);
        if (directedCycle.hasCycle()) throw new IllegalArgumentException("graph contains cycle");

        // Tree must contain exactly one node which has not in edges
        int potentialRootsNumber = 0;
        int rootVertex = -1;

        for (int v = 0; v < wordNet.V(); v++) {
            int in = wordNet.outdegree(v);
            if (in == 0) {
                potentialRootsNumber++;
                rootVertex = v;
            }
        }

        if (potentialRootsNumber != 1) {
            throw new IllegalArgumentException("graph is not a tree");
        }

        // In rooted tree all nodes must be reachable from root
        // and there should be single path from root to node
        boolean[] marked = new boolean[wordNet.V()];
        Digraph reverseGraph = wordNet.reverse();
        Queue<Integer> bstQueue = new Queue<>();
        bstQueue.enqueue(rootVertex);
        while (!bstQueue.isEmpty()) {
            int vertex = bstQueue.dequeue();
            if (marked[vertex]) {
                continue;
            }
            marked[vertex] = true;
            for (int adjVertex : reverseGraph.adj(vertex)) {
                bstQueue.enqueue(adjVertex);
            }
        }

        for (boolean mark : marked) {
            if (!mark) {
                throw new IllegalArgumentException("graph is not a rooted tree");
            }
        }

    }


    private void validateNoun(String noun) {
        if (noun == null || !isNoun(noun)) throw new IllegalArgumentException();
    }
}

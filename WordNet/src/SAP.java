import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


import java.util.Stack;


public class SAP {
    private final Digraph g;

    private static class Node {
        int length;
        int vertex;

        Node() {
            length = Integer.MAX_VALUE;
            vertex = -1;
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("graph must not be null");
        g = new Digraph(G.V());
        for (int vertex = 0; vertex < G.V(); vertex++) {
            for (int edgeVertex : G.adj(vertex)) {
                g.addEdge(vertex, edgeVertex);
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();
        stack1.add(v);
        stack2.add(w);
        return genericAncestor(stack1, stack2).length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();
        stack1.add(v);
        stack2.add(w);
        return genericAncestor(stack1, stack2).vertex;
    }


    private Node genericAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        verifyRange(v);
        verifyRange(w);
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(g, w);
        return findAncestor(bfs1, bfs2);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return genericAncestor(v, w).length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return genericAncestor(v, w).vertex;
    }

    private void verifyRange(int v) {
        if (v < 0 && v  >= g.V()) throw new IllegalArgumentException();
    }

    private void verifyRange(Iterable<Integer> v) {
        if (v == null) throw new IllegalArgumentException();
        for (int vertex : v) {
            verifyRange(vertex);
        }
    }

    private Node findAncestor(BreadthFirstDirectedPaths bfs1, BreadthFirstDirectedPaths bfs2) {
        Node ancestor = new Node();
        for (int i = 0; i < g.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                int dist1 = bfs1.distTo(i);
                int dist2 = bfs2.distTo(i);
                int path = dist1 + dist2;
                if (path < ancestor.length) {
                    ancestor.length = path;
                    ancestor.vertex = i;
                }
            }
        }
        if (ancestor.vertex == -1) ancestor.length = -1;
        return ancestor;
    }
}

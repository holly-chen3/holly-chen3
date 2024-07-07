package main.java.unsw.graph;

import java.util.ArrayList;

import unsw.set.Set;

public class BreadthFirstGraphIterator<N extends Comparable<N>> implements Iterator<N> {
    private Graph<N> graph;
    
    public void BFS() {
        ArrayList queue = new ArrayList();
        Set<E> visited = new Set<E>();
        while (queue != null) {
            int vertex = queue.dequeue();
            visited.add(vertex);
            queue.extend(graph.get_adjacent(vertex) - visited);
        }
    }
}

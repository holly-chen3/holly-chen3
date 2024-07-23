
#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"

void depthFirst(Graph g, int v, int *visited);

void depthFirstSearch(Graph g, int src) {
	int *visited = calloc(GraphNumVertices(g), sizeof(int));
	depthFirst(g, src, visited);
	free(visited);
}

void depthFirst(Graph g, int v, int *visited) {
	printf("%d ", v);
	visited[v] = 1;
	for (int i = 0; i < GraphNumVertices(g); i++) {
		if (GraphIsAdjacent(g, v, i) && !visited[i]) {
			depthFirst(g, i, visited);
		}
	}
}
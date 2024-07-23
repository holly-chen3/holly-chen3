
#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"

int recurseNum(Graph g, int v, int *visited);

int numReachable(Graph g, int src) {
	int *visited = calloc(GraphNumVertices(g), sizeof(int));
	// number of reachable consists of a depth first search algorithm
	int result = recurseNum(g, src, visited);
	free(visited);
	return result;
}

int recurseNum(Graph g, int v, int *visited) {
	visited[v] = 1;
	int result = 1;
	for (int w = 0; w < GraphNumVertices(g); w++) {
		if (GraphIsAdjacent(g, v, w) && !visited[w]) {
			result += recurseNum(g, w, visited);
		}
	}
	return result;
}
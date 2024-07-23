
#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"
#include "Stack.h"

bool dfs(Graph g, int v, int prev, bool *visited);

bool hasCycle(Graph g) {
	int nV = GraphNumVertices(g);
	bool *visited = calloc(nV, sizeof(bool));
	for (int v = 0; v < nV; v++) {
		if (!visited[v]) {
			if (dfs(g, v, v, visited)) {
				free(visited);
				return true;
			}
		}
	}
	free(visited);
	return false;
}

bool dfs(Graph g, int v, int prev, bool *visited) {
	visited[v] = true;
	for (int w = 0; w < GraphNumVertices(g); w++) {
		if (GraphIsAdjacent(g, v, w)) {
			if (visited[w]) {
				if (w != prev) {
					return true;
				}
			} else {
				if (dfs(g, w, v, visited)) {
					return true;
				}
			}
		}
	}
	return false;
}


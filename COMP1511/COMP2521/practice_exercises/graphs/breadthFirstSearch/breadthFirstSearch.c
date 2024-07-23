
#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"
#include "Queue.h"

void breadthFirstSearch(Graph g, int src) {
	int nV = GraphNumVertices(g);
	int *visited = calloc(nV, sizeof(int));
	visited[src] = 1;
	Queue q = QueueNew();
	QueueEnqueue(q, src);
	while (!QueueIsEmpty(q)) {
		int item = QueueDequeue(q);
		printf("%d ", item);
		for (int w = 0; w < nV; w++) {
			if (GraphIsAdjacent(g, item, w) && !visited[w]) {
				QueueEnqueue(q, w);
				visited[w] = 1;
			}
		}
	}
	QueueFree(q);
	free(visited);
}


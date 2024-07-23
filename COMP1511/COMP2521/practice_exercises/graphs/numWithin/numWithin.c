
#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"
#include "Queue.h"

int numWithin(Graph g, int src, int dist) {
	bfs();
	return 0;
}

bfs(Graph g, int v, int dist) {
	Queue q = QueueNew();
	QueueEnqueue(q, v);
	while (dist != 0 && !QueueIsEmpty(q)) {
		int v = QueueDequeue(q);
		for (int w = 0; w < GraphNumVertices(g); w++) {
			if ()
				QueueEnqueue(q, w);
		}
	}
}
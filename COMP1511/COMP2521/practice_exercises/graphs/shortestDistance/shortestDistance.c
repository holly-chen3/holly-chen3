
#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"
#include "Queue.h"

int shortestDistance(Graph g, int src, int dest) {
	int nV = GraphNumVertices(g);
	int pred[nV];
	for (int i = 0; i < nV; i++) {
		pred[i] = -1;
	}
	pred[src] = src;

	bool found = false;
	Queue q = QueueNew();
	QueueEnqueue(q, src);
	while (!found && !QueueIsEmpty(q)) {
		Vertex v = QueueDequeue(q);
		for (Vertex w; w < nV; w++) {
			if (GraphIsAdjacent(g, v, w) && pred[w] == -1) {
				pred[w] = v;
				if (w == dest) {
					found = true;
				}
				QueueEnqueue(q, w);
			}
		}
	}
	QueueFree(q);
	// if the destination has not yet been reached and found, 
	// there is no shortest distance to the dest from src.
	if (pred[dest] == -1) {
		return -1;
	} 
	// find the number of edges
	int numEdge = 0;
	Vertex curr = dest;
	while (curr != src) {
		numEdge++;
		curr = pred[curr];
	}
	return numEdge;
}



#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"
#include "Queue.h"


int *getDistances(Graph g, int src);
int *bfs(Graph g, int src);

int furthestReachable(Graph g, int src) {
	int *dist = getDistances(g, src);
	int furthest = src;
	for (int i = 0; i < GraphNumVertices(g); i++) {
		if (dist[i] != -1 && dist[i] >= dist[furthest]) {
			furthest = i;
		}
	}
	free(dist);
	return furthest;
}

int *getDistances(Graph g, int src) {
	int *pred = bfs(g, src);
	int *dist = malloc(sizeof(int) * GraphNumVertices(g));
	for (int v = 0; v < GraphNumVertices(g); v++) {
		if (pred[v] == -1) {
			// if there is no connection between vertice and src
			dist[v] = -1;
		} else {
			int numEdges = 0;
			int curr = v;
			while (curr != src) {
				numEdges ++;
				curr = pred[curr];
			}
			dist[v] = numEdges;
		}
	}
	free(pred);
	return dist;
}

int *bfs(Graph g, int src) {
	int *pred = malloc(sizeof(int) * GraphNumVertices(g));
	for (int i = 0; i < GraphNumVertices(g); i++) {
		pred[i] = -1;
	}
	pred[src] = src;
	Queue q = QueueNew();
	QueueEnqueue(q, src);

	while (!QueueIsEmpty(q)) {
		int v = QueueDequeue(q);
		for (int w = 0; w < GraphNumVertices(g); w++) {
			if (GraphIsAdjacent(g, v, w) && pred[w] == -1) {
				pred[w] = v;
				QueueEnqueue(q, w);
			}
		}
	}
	QueueFree(q);
	return pred;
}
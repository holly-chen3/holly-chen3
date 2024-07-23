// Centrality Measures API implementation
// COMP2521 Assignment 2

#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "CentralityMeasures.h"
#include "Dijkstra.h"
#include "Graph.h"

void recurseBetweennessCentrality(NodeData *dijkstra, double *newBetweennessCentrality, int v, int i);
double findPredNodeCount(PredNode *pred);

double *closenessCentrality(Graph g) {
	int gnV = GraphNumVertices(g);
	double *newClosenessCentrality = malloc(gnV * sizeof(double));
	assert(newClosenessCentrality != NULL);
	//find the closeness centrality for each node in the graph
	double result = 0;
	for (int v = 0; v < gnV; v++) {
		NodeData *newDijkstra = dijkstra(g, v);
		// n is the number or nodes reachable from vertex v, including itself
		double n = 1;
		double sum = 0;
		// shortest path to all vertices.
		for (int i = 0; i < gnV; i++) {
			if (newDijkstra[i].dist != INFINITY && i != v) {
				sum += newDijkstra[i].dist;
				n++;
			}
		}
		// if gnV - 1 or sum is 0, then the result will be undefined
		if (gnV - 1 == 0 || sum == 0) {
			result = 0;
		} else {
			result = ((n - 1) * (n - 1))/((gnV - 1) * sum);
		}
		newClosenessCentrality[v] = result;

	}
	
	return newClosenessCentrality;
}

double *betweennessCentrality(Graph g) {
	// trace back the shortest path using the pred
	// see if there is a particular vertex in between

	// use the spanning tree structure to figure out the paths
	// the number of leaf nodes will be the number of shortest paths.

	int gnV = GraphNumVertices(g);
	double *newBetweennessCentrality = malloc(gnV * sizeof(double));
	assert(newBetweennessCentrality != NULL);
	for (int v = 0; v < gnV; v++) {
		NodeData *newDijkstra = dijkstra(g, v);

		// shortest path to all vertices.
		// instead of the distance, look at the pred
		// list and trace the vertices back. 
		for (int i = 0; i < gnV; i++) {
			// recurses through the new dijkstra preds and 
			// traces back to the source.
			recurseBetweennessCentrality(newDijkstra, newBetweennessCentrality, v, i);
		}
		
		freeNodeData(newDijkstra, gnV);
	}
	return newBetweennessCentrality;
}

// helper function that recurses through the shortest path and counts vertices between
void recurseBetweennessCentrality(NodeData *dijkstra, double *newBetweennessCentrality, int v, int i) {
	PredNode *pred = dijkstra[i].pred;
	if (pred == NULL) {
		return;
	}
	double predNodeCount = findPredNodeCount(pred);
	for (PredNode *curr = pred; curr != NULL; curr = curr->next) {
		if (curr->v != v && curr->v != i) {
			newBetweennessCentrality[curr->v] += 1 / predNodeCount;
			recurseBetweennessCentrality(dijkstra, newBetweennessCentrality, v, curr->v);
		}
	}
}

// helper function that finds the number of pred nodes altogether
double findPredNodeCount(PredNode *pred) {
	double count = 0;
	for (PredNode *curr = pred; curr != NULL; curr = curr->next) {
		count++;
	}
	return count;
}
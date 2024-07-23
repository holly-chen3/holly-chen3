// Dijkstra API implementation
// COMP2521 Assignment 2
// Written by HOLLY-CHEN (z5359932)

#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "Dijkstra.h"
#include "Graph.h"
#include "PQ.h"

PredNode *createPredNode(Vertex w);
PredNode *insertPredNode(PredNode *pred, Vertex i);
void removePredList(PredNode *pred);

NodeData *dijkstra(Graph g, Vertex src) {
	assert(g != NULL);
	int gnV = GraphNumVertices(g);
	// create a list of nodedata
	NodeData *newDist = malloc(gnV * sizeof(struct NodeData));
	assert(newDist != NULL);
	for (int i = 0; i < gnV; i++) {
		// initialise all distance to infinity
		newDist[i].dist = INFINITY;
		// initialise all pred to null
		newDist[i].pred = NULL;
	}
	// make distance of src = 0
	newDist[src].dist = 0;
	// priority queue -> find the vertex with the least dist
	PQ newPQ = PQNew();
	PQInsert(newPQ, src, 0);
	
	while (!PQIsEmpty(newPQ)) {
		int newVertex = PQDequeue(newPQ);
		AdjList newOut = GraphOutIncident(g, newVertex);
		while (newOut != NULL) {
			// if the distance of newOut.v is larger
			// than the new distance given, then update
			// v->w update the predecessor list when
			// the new distance has been updated
			if (newDist[newOut->v].dist  > 
			(newDist[newVertex].dist + newOut->weight)) {
				// update the distance, the pred list and PQ
				newDist[newOut->v].dist = newDist[newVertex].dist + newOut->weight;
				PQInsert(newPQ, newOut->v, newDist[newOut->v].dist);
				removePredList(newDist[newOut->v].pred);
				newDist[newOut->v].pred = createPredNode(newVertex);
			} else if (newDist[newOut->v].dist == (newDist[newVertex].dist + newOut->weight)) {
				// when new dist is equal
				PQInsert(newPQ, newOut->v, newDist[newOut->v].dist);
				newDist[newOut->v].pred = insertPredNode(newDist[newOut->v].pred, newVertex);
			}
			newOut = newOut->next;
		}
	}
	// free the priority queue
	PQFree(newPQ);
	return newDist;
}

// helper function that creates a new pred node
PredNode *createPredNode(Vertex w) {
	// malloc the new pred node
	PredNode *newPredNode = malloc(sizeof(struct PredNode));
	assert(newPredNode != NULL);
	// initialise the pred node
	newPredNode->v = w;
	newPredNode->next = NULL;
	return newPredNode;
}

// helper function that inserts a new pred node
PredNode *insertPredNode(PredNode *pred, Vertex i) {
	// sort it so that vertex number is ascending
	// lowest to highest
	assert(pred != NULL);
	PredNode *newPredNode = createPredNode(i);
	PredNode *curr = pred;
	PredNode *prev = NULL;
	while(curr != NULL) {
		
		if (curr->v > i) {
			
			// if the current vertex is bigger than i,
			// insert i before that node
			if (prev == NULL) {
				newPredNode->next = curr;
				return newPredNode;
			} else {
				prev->next = newPredNode;
				newPredNode->next = curr;
				return pred;
			}
		}
		prev = curr;
		curr = curr->next;
	}
	prev->next = newPredNode;
	return pred;
}

// helper function that frees the pred list
void removePredList(PredNode *pred) {
	if (pred == NULL) return;
	removePredList(pred->next);
	free(pred);
}

void freeNodeData(NodeData *data, int nV) {
	if (data == NULL) {
		return;
	}
	for (int i = 0; i < nV; i++) {
		removePredList(data[i].pred);
	}
	free(data);
}


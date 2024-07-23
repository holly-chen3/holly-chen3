// Lance-Williams Algorithm for Hierarchical Agglomerative Clustering
// COMP2521 Assignment 2

#include <assert.h>
#include <float.h>
#include <limits.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"
#include "LanceWilliamsHAC.h"

#define INFINITY DBL_MAX


bool dContinue(Dendrogram *dendA, int gnV);
Dendrogram newDNode(int v);
double calculateLanceWilliams(double disti, double distj, int method);
void freeDistArray(double **distArray, int gnV);

/**
 * Generates  a Dendrogram using the Lance-Williams algorithm (discussed
 * in the spec) for the given graph  g  and  the  specified  method  for
 * agglomerative  clustering. The method can be either SINGLE_LINKAGE or
 * COMPLETE_LINKAGE (you only need to implement these two methods).
 * 
 * The function returns a 'Dendrogram' structure.
 */
Dendrogram LanceWilliamsHAC(Graph g, int method) {
	int gnV = GraphNumVertices(g);
	double **distArray = malloc(gnV * sizeof(*distArray));
	assert(distArray != NULL);
	
	for (int i = 0; i < gnV; i++) {
		distArray[i] = malloc(gnV * sizeof(double));
		assert(distArray[i] != NULL);
		for (int j = 0; j < gnV; j++) {
			// initialise all distance to infinity
			distArray[i][j] = INFINITY;
			if (i == j) {
				distArray[i][j] = 0;
			}
		}
	}
	// consider direct edges
	for (int v = 0; v < gnV; v++) {
		AdjList newOut = GraphOutIncident(g, v);
		AdjList newIn = GraphInIncident(g, v);

		while (newOut != NULL) {
			// if the vertices distance is smaller 
			if (distArray[v][newOut->v] > (1.0 / newOut->weight)) {
				distArray[v][newOut->v] = 1.0 / newOut->weight;
			}
			newOut = newOut->next;
		}
		while (newIn != NULL) {
			if (distArray[v][newIn->v] > (1.0 / newIn->weight)) {
				distArray[v][newIn->v] = (1.0 / newIn->weight);
			}
			newIn = newIn->next;
		}
	}
	
	// create dendrogram array
	Dendrogram *dendA = malloc(gnV * sizeof(Dendrogram));
	assert(dendA != NULL);
	for (int v = 0; v < gnV; v++) {
		dendA[v] = newDNode(v);
	}
	
	// if there is more than one dendogram, continue looping
	while (dContinue(dendA, gnV)) {
		// find closest clusters
		double min = INFINITY;
		int closei = -1;
		int closej = -1; 
		for (int i = 0; i < gnV; i++) {
			for (int j = 0; j < gnV; j++) {
				if (distArray[i][j] < min && distArray[i][j] != 0) {
					min = distArray[i][j];
					closei = i;
					closej = j;
				}
			}
		}
		// remove ci and cj from dendA
		Dendrogram newD = malloc(sizeof(struct DNode));
		assert(newD != NULL);
		newD->left = dendA[closei];
		newD->right = dendA[closej];
		dendA[closei] = newD;
		dendA[closej] = NULL;
		free(dendA[closej]);
		// remove ci and cj from dist array and update distances
		for (int i = 0; i < gnV; i++) {
			if (dendA[i] != NULL && i != closei && i != closej) {
				// if the closest cluster is not equal to i and the dendogram is still there for i
				// update the distance of the vertex/cluster i to the cluster
				distArray[i][closei] = calculateLanceWilliams(distArray[i][closei], distArray[i][closej], method);
				distArray[closei][i] = calculateLanceWilliams(distArray[closei][i], distArray[closej][i], method);
			}
			// delete ci and cj
			distArray[closej][i] = INFINITY;
			distArray[i][closej] = INFINITY;
		}
	}
	freeDistArray(distArray, gnV);
	return dendA[0];
}

// helper function that determines if there is only one dendogram 
// left or more
bool dContinue(Dendrogram *dendA, int gnV) {
	int dSwitch = 0;
	for (int d = 1; d < gnV; d++) {
		if (dendA[d] != NULL) {
			dSwitch = 1;
		}
	}
	return dSwitch;
}

// helper function that creates a new dendogram node
Dendrogram newDNode(int v) {
	Dendrogram newD = malloc(sizeof(struct DNode));
	assert(newD != NULL);
	newD->vertex = v;
	newD->left = NULL;
	newD->right = NULL;
	return newD;
}

// helper function that calculates the lance williams formula
double calculateLanceWilliams(double disti, double distj, int method) {
	// if one of the distances are infinity, return the minimum dist of the two
	if (disti == INFINITY) {
		return distj;
	} else if (distj == INFINITY) {
		return disti;
	}
	double y = 0.0;
	if (method == SINGLE_LINKAGE) {
		// ai is 0.5, aj is 0.5, and b is 0, y is -0.5
		y = -0.5;
	} else {
		// complete linkage
		// ai is 0.5, aj is 0.5, and b is 0, y is 0.5
		y = 0.5;
	}
	// find absolute value of the difference between disti and distj
	double difference = disti - distj;
	if (difference < 0) {
		difference = -difference;
	}
	return (0.5 * disti) + (0.5 * distj) + (y * difference);
}

// helper function that frees the distance array
void freeDistArray(double **distArray, int gnV) {
	for (int i = 0; i < gnV; i++) {
		free(distArray[i]);
	}
	free(distArray);
}

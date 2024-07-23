// Algorithms to design electrical grids

#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include "Graph.h"
#include "Place.h"
#include "PQ.h"

////////////////////////////////////////////////////////////////////////
// Your task

/**
 * Designs  a minimal cost electrical grid that will deliver electricity
 * from a power plant to all the given cities. Power lines must be built
 * between cities or between a city and a power plant.  Cost is directly
 * proportional to the total length of power lines used.
 * Assumes  that  numCities  is at least 1 (numCities is the size of the
 * cities array).
 * Stores the power lines that need to be built in the given  powerLines
 * array, and returns the number of power lines stored. Assumes that the
 * powerLines array is large enough to store all required power lines.
 */
int planGrid1(Place cities[], int numCities, Place powerPlant,
              PowerLine powerLines[]) {
    // create minimum spanning tree with the cities
    // then after that, connect the closest city with the powerplant
    
    // 1. create a new graph
    Graph newGraph = GraphNew(numCities + 1);
    double distance = 0.0;
    int newNum = numCities + 1;
    // 2. calculate the distance between each city and the powerplant
    for (int p = 0; p < numCities; p++) {
        distance = sqrt(pow((cities[p].x - powerPlant.x),2) + pow((cities[p].y - powerPlant.y),2));
        Edge newEdge;
        newEdge.v = newNum;
        newEdge.w = p;
        newEdge.weight = distance;
        GraphInsertEdge(newGraph, newEdge);
    }
    for (int i = 0; i < numCities; i++) {
        for (int j = 0; j < numCities; j++) {
            distance = sqrt(pow((cities[i].x - cities[j].x),2) + pow((cities[i].y - cities[j].y),2));
            if (distance != 0.0 && i < j) {
                Edge e;
                e.v = i;
                e.w = j;
                e.weight = distance;
                GraphInsertEdge(newGraph, e);
            }
        }
        
    }
    Graph newMST = GraphMST(newGraph);
    if (newMST == NULL) {
        return 0;
    } 
    // // get the edges within the graph, and find the vertices, and then link it to the Place cities
    // // and put in PowerLine
    // int k = 0;
    // for (int p = 0; p < GraphNumVertices(newMST); p++) {
    //     if (GraphIsAdjacent(newMST, newNum, p)) {
    //         PowerLine newPowerLine;
    //         newPowerLine.p1 = powerPlant;
    //         newPowerLine.p2 = cities[p];
    //         powerLines[k] = newPowerLine;
    //         k++;
    //     }
    // }
    
    // for (int i = 0; i < GraphNumVertices(newMST); i++) {
    //     for (int j = 0; j < GraphNumVertices(newMST); j++) {
    //         if (GraphIsAdjacent(newMST, i, j)) {
    //             PowerLine newPowerLine;
    //             newPowerLine.p1 = cities[i];
    //             newPowerLine.p2 = cities[j];
    //             powerLines[k] = newPowerLine;
    //             k++;
    //         }
    //     }
    // }
    return GraphNumVertices(newMST) - 1;
}

////////////////////////////////////////////////////////////////////////
// Optional task

/**
 * Designs  a minimal cost electrical grid that will deliver electricity
 * to all the given cities.  Power lines must be built between cities or
 * between a city and a power plant.  Cost is directly  proportional  to
 * the  total  length of power lines used.  Assume that each power plant
 * generates enough electricity to supply all cities, so not  all  power
 * plants need to be used.
 * Assumes  that  numCities and numPowerPlants are at least 1 (numCities
 * and numPowerPlants are the sizes of the cities and powerPlants arrays
 * respectively).
 * Stores the power lines that need to be built in the given  powerLines
 * array, and returns the number of power lines stored. Assumes that the
 * powerLines array is large enough to store all required power lines.
 */
int planGrid2(Place cities[], int numCities,
              Place powerPlants[], int numPowerPlants,
              PowerLine powerLines[]) {
    // TODO: Complete this function
    return 0;
}

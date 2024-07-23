// BFS maze solver

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Cell.h"
#include "helpers.h"
#include "Maze.h"
#include "Queue.h"

bool solve(Maze m) {
	bool **newBoolMatrix = createBoolMatrix(MazeHeight(m), MazeWidth(m));
	Cell **newCellMatrix = createCellMatrix(MazeHeight(m), MazeWidth(m));
	Queue q = QueueNew();
    Cell start = MazeGetStart(m);
	Item v = start;
	newBoolMatrix[start.row][start.col] = 1;
	newCellMatrix[start.row][start.col] = start;
	QueueEnqueue(q, start);
	MazeVisit(m, start);
	while (!QueueIsEmpty(q)) {
		v = QueueDequeue(q);

		if (MazeVisit(m, v)) {
			MazeMarkPath(m, v);
			Cell prev = v;
			while ((prev.row != start.row) || (prev.col != start.col)) {
				prev = newCellMatrix[prev.row][prev.col];
				MazeMarkPath(m, prev);
			}
			MazeMarkPath(m, start);
			freeBoolMatrix(newBoolMatrix);
			freeCellMatrix(newCellMatrix);
			QueueFree(q);
			return true;
		}

		
		for (int i = 0; i < 4; i++) {
            Item newCell = v;
            switch (i) {
				case 0:
					newCell.col--;
					break;
				case 1:
					newCell.col++;
					break;
				case 2:
					newCell.row++;
					break;
				case 3:
					newCell.row--;
					break;
			}
			if (newCell.row >= 0 && newCell.row < MazeHeight(m) && newCell.col >= 0 && newCell.col < MazeWidth(m) && 
				!MazeIsWall(m, newCell) && !newBoolMatrix[newCell.row][newCell.col]) {
				newBoolMatrix[newCell.row][newCell.col] = 1;
				newCellMatrix[newCell.row][newCell.col] = v;
				QueueEnqueue(q, newCell);
			}
		}
	}
	freeBoolMatrix(newBoolMatrix);
	freeCellMatrix(newCellMatrix);
	QueueFree(q);
	return false;
}

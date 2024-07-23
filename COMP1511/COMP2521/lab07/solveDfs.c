// DFS maze solver

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Cell.h"
#include "helpers.h"
#include "Maze.h"
#include "Stack.h"

bool solve(Maze m) {
	bool **newBoolMatrix = createBoolMatrix(MazeHeight(m), MazeWidth(m));
	Cell **newCellMatrix = createCellMatrix(MazeHeight(m), MazeWidth(m));
	Stack q = StackNew();
    Cell start = MazeGetStart(m);
	Item v = start;
	newBoolMatrix[start.row][start.col] = 1;
	newCellMatrix[start.row][start.col] = start;
	StackPush(q, start);
	MazeVisit(m, start);
	while (!StackIsEmpty(q)) {
		v = StackPop(q);
		if (MazeIsWall(m, v)) {
			continue;
		}
		newBoolMatrix[v.row][v.col] = 1;
		if (MazeVisit(m, v)) {
			MazeMarkPath(m, v);
			Cell curr = v;
			while ((curr.row != start.row) || (curr.col != start.col)) {
				curr = newCellMatrix[curr.row][curr.col];
				MazeMarkPath(m, curr);
			}
			MazeMarkPath(m, start);

			freeBoolMatrix(newBoolMatrix);
			freeCellMatrix(newCellMatrix);
			StackFree(q);
			return true;
		}
		
		for (int i = 3; i >= 0; i--) {
            Item newCell = v;
            switch (i) {
				case 0:
					newCell.row++;
					break;
				case 1:
					newCell.row--;
					break;
				case 2:
					newCell.col--;
					break;
				case 3:
					newCell.col++;
					break;
			}
			if (newCell.row >= 0 && newCell.row < MazeHeight(m) && newCell.col >= 0 && newCell.col < MazeWidth(m) && 
				!MazeIsWall(m, newCell) && !newBoolMatrix[newCell.row][newCell.col]) {
				
				newCellMatrix[newCell.row][newCell.col] = v;
				StackPush(q, newCell);
			}
		}
	}
	freeBoolMatrix(newBoolMatrix);
	freeCellMatrix(newCellMatrix);
	StackFree(q);
	return false;
}


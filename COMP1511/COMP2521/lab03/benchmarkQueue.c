
#include <stdio.h>
#include <stdlib.h>

#include "Queue.h"

int main(void) {
	Queue q = QueueNew();

	// Write a benchmark test to demonstrate the difference between
	// ArrayQueue and CircularArrayQueue
	int i;
	for (i = 0; i < 10000000000; i++) {
		QueueEnqueue(q, i);
	}

	for (i = 0; i < 100; i++) {
		QueueDequeue(q);
	}


	QueueFree(q);
}


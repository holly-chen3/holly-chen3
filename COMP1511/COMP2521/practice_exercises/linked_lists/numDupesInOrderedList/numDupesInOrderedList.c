
#include "list.h"

int numDupesInOrderedList(List l) {
	// loop through the ordered list
	// get all the duplicates and count it
	int count = 0;
	Node curr = l->head->next;
	Node prev = l->head;
	while (curr != NULL) {
		if (prev->value == curr->value) {
			count++;
		}
		prev = curr;
		curr = curr->next;
	}
	return count;
}


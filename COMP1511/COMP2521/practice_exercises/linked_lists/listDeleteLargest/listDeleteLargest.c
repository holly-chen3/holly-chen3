
#include "list.h"

int listDeleteLargest(List l) {
	Node curr = l->head;
	int max = 0;
	while (curr != NULL) {
		if (max < curr->value) {
			max = curr->value;
		}
		curr = curr->next;
	}
	// now find the max again in the list
	curr = l->head;
	Node prev = NULL;
	while (curr->value != max) {
		prev = curr;
		curr = curr->next;
	}
		
	// delete node at the front of the list
	if (prev == NULL) {
		l->head = curr->next;
	} else {
		prev->next = curr->next;
	}
	free(curr);
	return max;
}


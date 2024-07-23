
#include "list.h"

void listReverse(List l) {
	Node curr = l->head;
	Node prev = NULL;
	while (curr != NULL) {
		Node tmpNext = curr->next;
		curr->next = prev;
		prev = curr;
		curr = tmpNext;
	}
	// point head to the end
	l->head = prev;
	return;
}


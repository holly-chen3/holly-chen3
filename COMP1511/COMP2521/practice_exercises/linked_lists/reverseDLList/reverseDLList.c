
#include "list.h"

void reverseDLList(List l) {
	// loop through first and last lists
	// forwards:
	Node firstCurr = l->first;
	while (firstCurr != NULL) {
		Node firstTmp = firstCurr->prev;
		firstCurr->prev = firstCurr->next;
		firstCurr->next = firstTmp;
		firstCurr = firstCurr->prev;
	}
	Node tmp = l->first;
	l->first = l->last;
	l->last = tmp;
}


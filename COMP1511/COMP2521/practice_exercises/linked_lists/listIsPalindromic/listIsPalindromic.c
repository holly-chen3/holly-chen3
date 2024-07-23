
#include "list.h"

bool listIsPalindromic(List l) {
	if (l->first == NULL || l->last == NULL) {
		return true;
	}
	Node firstCurr = l->first;
	Node lastCurr = l->last;
	while (firstCurr != NULL && lastCurr != NULL) {
		if (firstCurr->value != lastCurr->value) {
			return false;
		}
		firstCurr = firstCurr->next;
		lastCurr = lastCurr->prev;
	}
	return true;
}



#include "list.h"

#define SORTED 1
#define REVERSED 0

bool listIsOrdered(List l) {
	if (l == NULL) {
		return true;
	}
	if (l->head == NULL) {
		return true;
	}
	Node curr = l->head->next;
	Node prev = l->head;
	if (curr != NULL && prev->value > curr->value) {
		while (curr != NULL) {
			if (prev->value < curr->value) {
				return false;
			}
			prev = curr;
			curr = curr->next;
		}
		return true;
	} else if (curr != NULL && prev->value < curr->value) {
		while (curr != NULL) {
			if (prev->value > curr->value) {
				return false;
			}
			prev = curr;
			curr = curr->next;
		}
		return true;
	}
	return true;
	
}


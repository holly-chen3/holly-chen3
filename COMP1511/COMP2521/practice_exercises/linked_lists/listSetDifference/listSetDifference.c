
#include "list.h"

void insertNum(Node new, List newSet);

List listSetDifference(List l1, List l2) {
	// set difference is how many numbers are unique to list l1
	List newSet = newList();
	for (Node curr = l1->head; curr != NULL; curr = curr->next) {
		int s = 0;
		for (Node twoCurr = l2->head; twoCurr != NULL; twoCurr = twoCurr->next) {
			if (curr->value == twoCurr->value) {
				s = 1;
			}
		}
		if (!s) {
			// if not any, put in set difference list
			Node new = newNode(curr->value);
			insertNum(new, newSet);
		} 
	}
	return newSet;
}

void insertNum(Node new, List newSet) {
	if (newSet == NULL) {
		newSet->head = new;
	} else {
		new->next = newSet->head;
		newSet->head = new;
	}
}
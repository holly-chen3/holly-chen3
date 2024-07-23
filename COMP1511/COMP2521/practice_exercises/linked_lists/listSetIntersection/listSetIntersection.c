
#include "list.h"

void insertNum(Node n, List newSet);

List listSetIntersection(List s1, List s2) {
	// traverse both lists
	List newSet = newList();
	for (Node s1Curr = s1->head; s1Curr != NULL; s1Curr = s1Curr->next) {
		for (Node s2Curr = s2->head; s2Curr != NULL; s2Curr = s2Curr->next) {
			if (s1Curr->value == s2Curr->value) {
				Node newN = newNode(s1Curr->value);
				insertNum(newN, newSet);
			}
		}
	}
	return newSet;
}

void insertNum(Node n, List newSet) {
	if (newSet->head == NULL) {
		newSet->head = n;
	} else {
		n->next = newSet->head;
		newSet->head = n;
	}
}


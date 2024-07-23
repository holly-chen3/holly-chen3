
#include "list.h"

void insertNum(Node n, List unionList);
bool noRepetition(int value, List unionList);

List listSetUnion(List s1, List s2) {
	// insert number into new list, except if it already there
	List unionList = newList();
	for (Node curr = s1->head; curr != NULL; curr = curr->next) {
		Node new = newNode(curr->value);
		insertNum(new, unionList);
	}
	for (Node curr = s2->head; curr != NULL; curr = curr->next) {
		if (noRepetition(curr->value, unionList)) {
			Node new = newNode(curr->value);
			insertNum(new, unionList);
		}
	}
	return unionList;
}

void insertNum(Node n, List unionList) {
	Node curr = unionList->head;
	Node prev = NULL;
	if (unionList->head == NULL) {
		unionList->head = n;
		return;
	}
	while (curr != NULL) {
		prev = curr;
		curr = curr->next;
	}
	// now curr == NULL
	prev->next = n;
}

bool noRepetition(int value, List unionList) {
	Node curr = unionList->head;
	while (curr != NULL) {
		if (curr->value == value) {
			return false;
		}
		curr = curr->next;
	}
	return true;
}
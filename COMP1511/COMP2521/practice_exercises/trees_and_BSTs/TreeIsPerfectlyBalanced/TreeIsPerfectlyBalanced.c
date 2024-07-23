
#include <stdlib.h>

#include "tree.h"

int getSize(Tree t);

bool TreeIsPerfectlyBalanced(Tree t) {
	if (t == NULL) {
		return true;
	} 
	int difference = getSize(t->left) - getSize(t->right);
	if (difference < 0) {
		difference = -difference;
	}
	if (difference > 1) {
		return false;
	} else {
		return TreeIsPerfectlyBalanced(t->left) && TreeIsPerfectlyBalanced(t->right);
	}
}

int getSize(Tree t) {
	if (t == NULL) {
		return 0;
	} else {
		return 1 + getSize(t->left) + getSize(t->right);
	}
}
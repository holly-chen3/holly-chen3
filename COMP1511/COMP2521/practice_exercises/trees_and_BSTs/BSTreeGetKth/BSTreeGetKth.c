
#include <stdlib.h>

#include "BSTree.h"

int getSize(BSTree t);

int BSTreeGetKth(BSTree t, int k) {
	int leftSize = getSize(t->left);
	if (k == leftSize) {
		return t->value;
	} else if (k < leftSize) {
		return BSTreeGetKth(t->left, k);
	} else {
		return BSTreeGetKth(t->right, k - leftSize - 1);
	}
}

int getSize(BSTree t) {
	if (t == NULL) {
		return 0;
	} else {
		return 1 + getSize(t->left) + getSize(t->right);
	}
}

#include "BSTree.h"

#include <stdlib.h>

BSTree BSTreeGetSmallest(BSTree t) {
	if (t == NULL) {
		return NULL;
	}
	// find the smallest number in the BSTree
	else if (t->left == NULL) {
		return t;
	} else {
		return BSTreeGetSmallest(t->left);
	}
}


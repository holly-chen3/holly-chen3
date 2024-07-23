
#include <stdlib.h>

#include "BSTree.h"

int BSTreeNodeDepth(BSTree t, int key) {
	if (t == NULL) {
		return -1;
	}
	int height = 0;
	if (t->value == key) {
		return 0;
	} else if (key < t->value) {
		height = BSTreeNodeDepth(t->left, key);
		return (height == -1 ? -1 : height + 1);
	} else {
		height = BSTreeNodeDepth(t->right, key);
		return (height == -1 ? -1 : height + 1);
	} 
}


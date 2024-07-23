
#include <stdlib.h>

#include "tree.h"

bool isOdd(int value);

int TreeSumOdds(Tree t) {
	if (t == NULL) {
		return 0;
	}
	if (isOdd(t->value)) {
		return t->value + TreeSumOdds(t->left) + TreeSumOdds(t->right);
	} else {
		return 0 + TreeSumOdds(t->left) + TreeSumOdds(t->right);
	}
}

bool isOdd(int value) {
	if (value % 2 == 1) {
		return true;
	} else {
		return false;
	}
}
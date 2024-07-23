
#include <stdio.h>
#include <stdlib.h>

#include "BSTree.h"

BSTree newNode(int val);

BSTree BSTreeInsert(BSTree t, int val) {
	if (t == NULL) {
		return newNode(val);
	}
	// insert into the BST if the value is not
	// already in the BST using recursion
	if (val < t->value) {
		t->left = BSTreeInsert(t->left, val);
	} else if (val > t->value) {
		t->right = BSTreeInsert(t->right, val);
	} 
	return t;
}

BSTree newNode(int val) {
	BSTree new = malloc(sizeof(struct BSTNode));
	new->value = val;
	new->left = NULL;
	new->right = NULL;
	return new;
}
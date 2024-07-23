
#include "tree.h"

Tree newNode(int val);

Tree TreeCopy(Tree t, int depth) {
	if (t == NULL || depth < 0) {
		return NULL;
	} 
	Tree copy = newNode(t->value);
	copy->left = TreeCopy(t->left, depth - 1);
	copy->right = TreeCopy(t->right, depth - 1);
	return copy;
}

Tree newNode(int val) {
	Tree new = malloc(sizeof(*new));
	if (new == NULL) {
		fprintf(stderr, "Insufficient memory\n");
		exit(EXIT_FAILURE);
	}
	new->value = val;
	new->left = NULL;
	new->right = NULL;
	return new;
}
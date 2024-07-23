
#include <stdlib.h>

#include "tree.h"

int max(int lHeight, int rHeight);

int TreeHeight(Tree t) {
    if (t == NULL) {
        return -1;
    } 
    int lHeight = TreeHeight(t->left);
    int rHeight = TreeHeight(t->right);
    return 1 + max(lHeight, rHeight);
}

int max(int lHeight, int rHeight) {
    if (lHeight < rHeight) {
        return rHeight;
    } else {
        return lHeight;
    }
}
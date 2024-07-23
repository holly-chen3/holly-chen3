
#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "BSTree.h"

int doDiff(BSTree t);
int doSize(BSTree t);

int maxDiff(BSTree t) {
    // find the size of left and right subtrees
    // find the difference between the two sizes. 
    // compare the difference 
    if (t == NULL) {
        return 0;
    }
    int currMax = doDiff(t);
    int leftMax = maxDiff(t->left);
    int rightMax = maxDiff(t->right);
    int max = leftMax > rightMax ? leftMax : rightMax;
    if (currMax <= max) {
        return max;
    } else {
        return currMax;
    }
}

int doDiff(BSTree t) {
    int lSize = doSize(t->left);
    int rSize = doSize(t->right);
    int difference = lSize - rSize;
    if (difference < 0) {
        difference = -difference;
    }
    return difference;
}

int doSize(BSTree t) {
    if (t == NULL) {
        return 0;
    }
    int lSize = doSize(t->left);
    int rSize = doSize(t->right);
    return 1 + lSize + rSize;
}

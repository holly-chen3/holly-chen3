
#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "list.h"


// Worst case time complexity of this solution: O(n^2)
List zipList(List l1, int x, List l2, int y) {
    // create a new list
    List l = ListNew();
    // if both lists are empty
    if (l1 == NULL && l2 == NULL) {
        return NULL;
    } 
    // if either list is empty
    if (l1 == NULL && l2 != NULL) {
        return l2;
    } else if (l2 == NULL && l1 != NULL) {
        return l1;
    }
    // if both x or y is 0
    if (x == 0 && y == 0) {
        return NULL;
    }
    // traverse l1 
    Node curr = l1->first;
    Node currL2 = l2->first;
    int i = 0;
    while (curr != NULL) {
        if (i == x) {
            // change to the next list
            i = 0;
            int j = 0;
            while (currL2 != NULL) {
                if (j == y) {
                    break;
                } else {
                    Node n = newNode(currL2->value);
                    assert(n != NULL);
                    l->last->next = n;
                }
                j++;
                currL2 = currL2->next;
            }
        } else {
            // add the numbers into the new list
            Node n = newNode(curr->value);
            assert(n != NULL);
            l->last->next = n;
        }
        i++;
        curr = curr->next;
    }

    return l;
}

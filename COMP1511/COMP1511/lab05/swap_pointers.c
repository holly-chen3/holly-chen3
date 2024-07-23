//swap pointers
//By HOLLY-CHEN (z5359932)
//19-03-2021

#include <stdio.h>

void swap_pointers(int *a, int *b);

// This is a simple main function which could be used
// to test your swap_pointers function.
// It will not be marked.
// Only your swap_pointers function will be marked.

int main(void) {
    int first = 1;
    int second = 2;
    
    swap_pointers(&first, &second);
    
    printf("%d, %d\n", first, second);
    return 0;
}

// swap the values in two integers, given as pointers
void swap_pointers(int *a, int *b) {
    // PUT YOUR CODE HERE (you must change the next line!)
    int tmp;
    tmp = *b;
    *b = *a;
    *a = tmp;
}

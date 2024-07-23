// Reverse Array 
// Read integers line by line and when it reaches the end
// of input, print those integers in reverse order, line by line.
#include <stdio.h>
int swap_nums (int *num1, int *num2);
int function_reverse (int array[]);
int main(void) {
    int array[100] = {};
    printf("Enter numbers forwards: ");
    int i = 0;
    while (scanf("%d", &array[i]) == 1) {
        printf("%d", function_reverse(array));
    }
}

int function_reverse (int array) {
    int temp;
    temp = array[
    
}

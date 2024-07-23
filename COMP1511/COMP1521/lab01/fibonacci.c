// Written By Holly Chen, z5359932
// Fibonacci Sequence --> input a number which will output 
// the value in fibonacci sequence.

#include <stdio.h>
#include <stdlib.h>

#define SERIES_MAX 46

int fibonacci(int num, int already_computed[SERIES_MAX + 1]);

int main(void) {
    int already_computed[SERIES_MAX + 1] = { 0, 1 };
	int num = 0;
    while(scanf("%d", &num) == 1) {
        printf("%d\n", fibonacci(num, already_computed));
    }
    return EXIT_SUCCESS;
}

int fibonacci(int num, int already_computed[SERIES_MAX + 1]) {
    if(num <= 0 || num > SERIES_MAX) {
        return 0;
    }
    if(already_computed[num] != 0) {
        return already_computed[num];
    }
    int output = fibonacci(num - 1, already_computed) + fibonacci(num - 2, already_computed);
    already_computed[num] = output;
    return output;
}
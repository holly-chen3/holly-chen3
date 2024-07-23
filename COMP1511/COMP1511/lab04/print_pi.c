// Prints the first n digits of pi, where n is specified 
// by Holly Chen 
// z5359932

#include <stdio.h>

#define MAX_DIGITS 10

int main(void) {
    int digits;
    int pi[MAX_DIGITS] = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
    printf("How many digits of pi would you like to print? ");
    scanf("%d", &digits);
    int i = 0;
    while (digits <= MAX_DIGITS && i < digits) {
        if (i == 1) {
            printf(".");/* If you put if after the printf, it will 
            print the . at 31.4
            If you put the printf first and then do i==0,
            then when digits = 1, then i = 0 and would print out 3.
            */
        }
        printf("%d", pi[i]);
        i++;
    }
    printf("\n");
    return 0;
}

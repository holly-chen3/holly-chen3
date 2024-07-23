// By Holly Chen 
// z5359932
// 5th of March 2021
#include <stdio.h>
int main(void) {
    int n;
    printf("Enter number: ");
    scanf("%d", &n);
    printf("The factors of %d are:\n", n);
    int i = 1;
    int total = 0;
    while (i <= n) {
        if (n % i == 0) {
            printf("%d\n", i);
            total = total + i;
        }
        i++;
    }
    printf("Sum of factors = %d\n", total);
    if ((total - n) == n) {
        printf("%d is a perfect number\n", n);
    } else {
        printf("%d is not a perfect number\n", n);
    }
    return 0;
}
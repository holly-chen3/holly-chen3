// Three_five
// By Holly Chen: z5359932
//5th of March 2021
#include <stdio.h>
int main(void) {
    int n;
    printf("Enter number: ");
    scanf("%d", &n);
    int i = 1;
    while (i < n) {
        if (i % 3 == 0) {
            printf("%d\n", i);
        } else if (i % 5 == 0) {
            printf("%d\n", i);
        }
        i++;
    }
    return 0;
}//

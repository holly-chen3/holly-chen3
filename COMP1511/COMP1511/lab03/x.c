// By Holly Chen on 5th of March
// z5359932
#include <stdio.h>
int main(void) {
    int num;
    printf("Enter size: ");
    scanf("%d", &num);
    int i = 0;
    while (i < num) {
        int j = 0;
        while (j < num) {
            if (i == j || j == num - i - 1) {
                printf("*");
            } else {
                printf("-");
            }
            j++;
        }
        printf("\n");
        i++;
    }
    return 0;
}

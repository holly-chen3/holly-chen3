//By Holly Chen 
//z5359932
//Drawing hollow triangles
#include <stdio.h>
int main(void) {
    int n;
    printf("Enter size: ");
    scanf("%d", &n);
    int i = 0;
    while (i < n) {
        int j = 0;
        while (j < n) {
            if (j == i || j == 0 || i == n - 1) {
                printf("*");
            } else {
                printf(" ");
            }
            j++;
        }
        printf("\n");
        i++;
    }
    return 0;
}

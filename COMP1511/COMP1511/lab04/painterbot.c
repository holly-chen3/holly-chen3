//Printing out a one dimensional array which has 36 0's except at given
//indices it has '1'.
//By HOLLY CHEN (z5359932)
//12-03-2021
#include <stdio.h>
int main(void){
    int array[36] = {};
    int scanned_in_value;
    while (scanf("%d", &scanned_in_value) == 1) {
        array[scanned_in_value] = 1;
    }
    int i = 0;
    while (i < 36) {
        printf("%d", array[i]);
        i++;
    }
    printf("\n");
    return 0;
}

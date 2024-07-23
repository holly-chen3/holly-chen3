//Ordering Three Integers
//By Holly Chen: z5359932
//5/03/21
#include <stdio.h>
int main(void){
    int a;
    int b;
    int c;
    int tmp;
    printf("Enter integer: ");
    scanf("%d", &a);
    printf("Enter integer: ");
    scanf("%d", &b);
    printf("Enter integer: ");
    scanf("%d", &c);
    if (a > b) {
        //if a = 3 b = 2
        tmp = b; // tmp = 2
        b = a;  // b = 3 a = 3 tmp = 2
        a = tmp; //a = 2 b = 3
    } if (a > c) {
        tmp = c;
        c = a;
        a = tmp;
    } if (b > c) {
         //if b = 5 c = 2
        tmp = c; // tmp = 2
        c = b; // c = 5 b = 5 tmp = 2
        b = tmp; // b = 2 c = 5
    } 

    printf("The integers in order are: %d %d %d \n", a, b, c);    
    return 0;
}



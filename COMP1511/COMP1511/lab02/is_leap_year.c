//Decide whether any year is a leap year.
//Holly Chen on 26/02/2021

#include <stdio.h>
int main(void){
    int a;
    printf("Enter year: ");
    scanf("%d", &a);
    if (a%4==0){
    if (a%100==0 && a%400!=0){
    printf("%d is not a leap year.\n", a);
    }
    else {
    printf("%d is a leap year.\n", a);
    }
    }
    else {
    printf("%d is not a leap year.\n", a);
    }
    return 0;
}

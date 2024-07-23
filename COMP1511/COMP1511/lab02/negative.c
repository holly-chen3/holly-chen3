//Game called 'Don't be so negative!' 
//By Holly Chen, first year Computer Science student

#include <stdio.h>

int main(void){
    int input;
    scanf("%d", &input);
    if (input<0){
    printf("Don't be so negative!\n");
    }
    else if (input==0) {
    printf("You have entered zero. \n");
    }
    else { 
    printf("You have entered a positive number.\n");
    }
    return 0;
}

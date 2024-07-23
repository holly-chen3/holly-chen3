//Dice Range Game
//By Holly Chen on 26/02/2021

#include <stdio.h>
int main(void){
    int sides;
    int no._of_dice;
    printf("Enter the number of sides on your dice: ");
    scanf("%d", &sides);
    printf("Enter the number of dice being rolled: ");
    scanf("%d", &no._of_dice);
    int maxrange=sides*no.ofdice;
    if(maxrange!=0 && maxrange>0){
    printf("Your dice range is %d to %d.\n",no.ofdice,maxrange);
    printf("The average value is %lf\n", (no.ofdice+maxrange)/2.0);
    } else {
    printf("These dice will not produce a range.\n");
    }
    return 0;
}

#include <stdio.h>

void change_lame(int lame);
void alt_change(int *lame);

int main(void) {
    int lame = 1;
    //change_lame(lame);
     alt_change(&lame);
    printf("%d\n", lame);
    
    return 0;
}

void change_lame(int lame) {
    lame++;
}

void alt_change(int *lame) {
    (*lame)++;
}

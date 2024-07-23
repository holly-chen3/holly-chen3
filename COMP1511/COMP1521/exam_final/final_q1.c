#include <stdio.h>

// read two integers and print all the integers which have their bottom 2 bits set.

int main(void) {
    int x, y;

    scanf("%d", &x);
    scanf("%d", &y);

    int bit_mask = 0x3;

    int c = x + 1;
    while (c < y) {
        if ((c & bit_mask) == bit_mask) {
            printf("%d\n", c);
        }
        c++;
    }

    return 0;
}

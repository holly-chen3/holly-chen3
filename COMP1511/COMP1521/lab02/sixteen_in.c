// Written at 22-09-2021
// By Holly Chen (z5359932)
// Convert string of binary digits to 16-bit signed integer

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#define N_BITS 16

int16_t sixteen_in(char *bits);

int main(int argc, char *argv[]) {

    for (int arg = 1; arg < argc; arg++) {
        printf("%d\n", sixteen_in(argv[arg]));
    }

    return 0;
}

//
// given a string of binary digits ('1' and '0')
// return the corresponding signed 16 bit integer
//
int16_t sixteen_in(char *bits) {

    int16_t result = 0;
    int bit = 0;
    while(bit < N_BITS) {
        int16_t w_mask = 1 << (N_BITS - 1 - bit);
        if (bits[bit] == '1') {
            result = result | w_mask;
        }
        bit++;
    }
    return result;
}


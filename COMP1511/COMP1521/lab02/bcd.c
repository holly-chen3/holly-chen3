#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

#define N_BITS 16

int bcd(int bcd_value);

int main(int argc, char *argv[]) {

    for (int arg = 1; arg < argc; arg++) {
        long l = strtol(argv[arg], NULL, 0);
        assert(l >= 0 && l <= 0x0909);
        int bcd_value = l;

        printf("%d\n", bcd(bcd_value));
    }

    return 0;
}

// given a  BCD encoded value between 0 .. 99
// return corresponding integer
int bcd(int bcd_value) {
    int16_t result_one = 0;
    int16_t result_two = 0;
    int bit = 0;
    while(bit < (N_BITS / 2)) {
        int16_t w_mask = 1 << (N_BITS - 1 - bit);
        int16_t t_mask = 1 << ((N_BITS / 2) - 1 - bit);
        if(w_mask & bcd_value) {
            result_one = result_one | w_mask;
        } 
        if(t_mask & bcd_value) {
            result_two = result_two | t_mask;
        }
        bit++;
    }
    
    result_one = result_one >> (N_BITS / 2);
    int16_t result = (result_one * 10) + result_two;

    return result;
}


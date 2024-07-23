// count bits in a uint64_t

#include <assert.h>
#include <stdint.h>
#include <stdlib.h>

#define N_BITS 64

// return how many 1 bits value contains
int bit_count(uint64_t value) {
    int bit = 0;
    int count = 0;
    uint64_t one = 1;
    while(bit < N_BITS) {
        uint64_t w_mask = one << (N_BITS - 1 - bit);
        if(w_mask & value) {
            count++;
        }
        bit++;
    }


    return count;
}

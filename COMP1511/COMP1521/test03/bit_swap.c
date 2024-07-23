// swap pairs of bits of a 64-bit value, using bitwise operators

#include <assert.h>
#include <stdint.h>
#include <stdlib.h>

#define N_BITS 64

// return value with pairs of bits swapped
uint64_t bit_swap(uint64_t value) {
    int bit = 0;
    uint64_t one = 1;
    uint64_t result = 0;
    while(bit < N_BITS) {
        uint64_t w_mask = one << (N_BITS - 1 - bit);
        uint64_t s_mask = one << (N_BITS - 2 - bit);
        if(w_mask & value) {
            result = (result | (w_mask >> 1));
        }
        if(s_mask & value) {
            result = (result | (s_mask << 1));
        }

        bit += 2;

    }

    return result;
}

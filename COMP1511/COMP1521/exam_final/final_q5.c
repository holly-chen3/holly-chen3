#include <stdlib.h>
#include <stdint.h>

#define N_BITS 32
// given 2 uint32_t values
// return the number of bits which are set (1) in both values

int final_q5(uint32_t value1, uint32_t value2) {
    uint32_t one = 1;

    int bit = 0;
    int count = 0;
    while (bit < 32) {
        uint32_t w_mask = (one << (N_BITS - 1 - bit));
        if ((value1 & w_mask) && (value2 & w_mask)) {
            count++;
        }
        bit++;
    }
    return count;   
}

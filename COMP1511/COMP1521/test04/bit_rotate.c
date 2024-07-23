#include "bit_rotate.h"

#define N_BITS 16

// return the value bits rotated left n_rotations
uint16_t bit_rotate(int n_rotations, uint16_t bits) {
    if(n_rotations < 0) {
        n_rotations = - n_rotations;
        while(n_rotations > 0) {
            uint16_t last_bit = bits & 1;
            bits = (bits >> 1) | (last_bit << (N_BITS - 1));
            n_rotations--;
        }
    } else {
        n_rotations %= N_BITS;
        while(n_rotations > 0) {
            uint16_t last_bit = (bits >> (N_BITS - 1)) & 1;
            bits = (bits << 1) | last_bit;
            n_rotations--;
        }
    }
    return bits; 
}

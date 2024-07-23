#include "sign_flip.h"

// given the 32 bits of a float return it with its sign flipped
uint32_t sign_flip(uint32_t f) {
    uint32_t one = 1;
    f ^= (one << 31);
    return f; 
}

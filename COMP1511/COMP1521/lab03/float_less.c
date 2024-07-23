// Compare 2 floats using bit operations only

#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

#include "floats.h"

// float_less is given the bits of 2 floats bits1, bits2 as a uint32_t
// and returns 1 if bits1 < bits2, 0 otherwise
// 0 is return if bits1 or bits2 is Nan
// only bit operations and integer comparisons are used
uint32_t float_less(uint32_t bits1, uint32_t bits2) {
    float_components_t float_one;
    float_one.fraction = bits1 & 0x7fffff;
    float_one.exponent = (bits1 >> 23) & 0xff;
    float_one.sign = (bits1 >> 31) & 1; 
    float_components_t float_two;
    float_two.fraction = bits2 & 0x7fffff;
    float_two.exponent = (bits2 >> 23) & 0xff;
    float_two.sign = (bits2 >> 31) & 1; 

    if (float_one.exponent == 0xff && float_one.fraction != 0) {
        return 0;
    } 
    if (float_two.exponent == 0xff && float_two.fraction != 0) {
        return 0;
    } 
    if (bits1 == 0 && bits2 == 0) {
        return 0;
    }

    if (float_one.sign == 1) {
        if (float_two.sign == 0) {
            return 1;
        } else {
            float_one.sign = 0;
            float_two.sign = 0;
            if (bits1 > bits2) {
                return 1;
            } else {
                return 0;
            }
        }
    } else { //bits1 is positive
        if(float_two.sign == 0) {
            if (bits1 < bits2) {
                return 1;
            } else {
                return 0;
            }
        } else { //bits2 is negative
            return 0;
        }
    }
}

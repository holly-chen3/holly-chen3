// Multiply a float by 2048 using bit operations only

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

#include "floats.h"

// float_2048 is given the bits of a float f as a uint32_t
// it uses bit operations and + to calculate f * 2048
// and returns the bits of this value as a uint32_t
//
// if the result is too large to be represented as a float +inf or -inf is returned
//
// if f is +0, -0, +inf or -inf, or Nan it is returned unchanged
//
// float_2048 assumes f is not a denormal number
//
uint32_t float_2048(uint32_t f) {
    float_components_t separated_float;
    separated_float.fraction = f & 0x7fffff;
    separated_float.exponent = ((f >> 23) & 0xff);
    separated_float.sign = (f >> 31) & 1; 
    if(separated_float.exponent < 0xff && separated_float.fraction > 0) {
        separated_float.exponent += 11;
        if(separated_float.exponent >= 0xff) {
            separated_float.fraction = 0;
            separated_float.exponent = 0xff;
        }
    }


    return (separated_float.sign << 31) | (separated_float.exponent << 23) | (separated_float.fraction);
}

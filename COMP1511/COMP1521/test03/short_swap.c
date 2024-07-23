// Swap bytes of a short

#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

// given uint16_t value return the value with its bytes swapped
uint16_t short_swap(uint16_t value) {
    uint16_t byte1 = (value & 0xff) << 8;
    uint16_t byte2 = (value & 0xff00) >> 8;

    return (byte1 | byte2);
}
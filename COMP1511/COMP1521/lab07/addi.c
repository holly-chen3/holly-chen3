// generate the opcode for an addi instruction

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

#include "addi.h"

#define  N_BITS 32

// return the MIPS opcode for addi $t,$s, i
uint32_t addi(int t, int s, int i) {
    uint32_t tee = t;
    uint32_t es = s;

    return 0x20000000 | (es << (N_BITS - 11)) | (tee << (N_BITS - 16)) | (i & 0xFFFF); 

}

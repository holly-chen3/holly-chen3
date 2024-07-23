// COMP1521 21T2 ... final exam, question 0

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#define N_BITS 32

int
count_leading_zeroes (uint32_t x)
{
	// shift int to the right 
	int byte = 0;
	while (byte < N_BITS) {
		uint32_t one = 0x80000000;
		uint32_t w_mask = (one >> byte);
		if (w_mask & x) {
			return byte;
		}
		byte++;
	}
	return byte;
}


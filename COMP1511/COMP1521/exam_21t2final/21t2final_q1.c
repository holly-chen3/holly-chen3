// COMP1521 21T2 ... final exam, question 1

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BITS 8

void
and (bool x[BITS], bool y[BITS], bool result[BITS])
{
	int byte = 0;
	while (byte < BITS) {
		if (x[byte] && y[byte]) {
			result[byte] = 1;
		} else {
			result[byte] = 0;
		}
		byte++;
	}
	return;
}

void
or (bool x[BITS], bool y[BITS], bool result[BITS])
{
	int byte = 0;
	while (byte < BITS) {
		if(x[byte] || y[byte]) {
			result[byte] = 1;
		} else {
			result[byte] = 0;
		}
		byte++;
	}
	return;
}

void
xor (bool x[BITS], bool y[BITS], bool result[BITS])
{
	int byte = 0;
	while (byte < BITS) {
		if((x[byte] || y[byte]) && !(x[byte] && y[byte])) {
			result[byte] = 1;
		} else {
			result[byte] = 0;
		}
		byte++;
	}
	return;
}

void
not (bool x[BITS], bool result[BITS])
{
	int byte = 0;
	while (byte < BITS) {
		if(!(x[byte])) {
			result[byte] = 1;
		} else {
			result[byte] = 0;
		}
		byte++;
	}
	return;
}

// COMP1521 21T2 ... final exam, question 5

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

void
print_utf8_count (FILE *file)
{
	unsigned long amount_1_byte = 0;
	unsigned long amount_2_byte = 0;
	unsigned long amount_3_byte = 0;
	unsigned long amount_4_byte = 0;

	uint8_t two_mask = 0x6;
	uint8_t three_mask = 0xe;
	uint8_t four_mask = 0x1e;
	int c;
	while ((c = fgetc(file)) != EOF) {
		if (!(c >> 7)) {
			amount_1_byte++;
		} else if (!((c >> 5) ^ two_mask)) {
			amount_2_byte++;
		} else if (!((c >> 4) ^ three_mask)) {
			amount_3_byte++;
		} else if (!((c >> 3) ^ four_mask)) {
			amount_4_byte++;
		} 
	}

	printf("1-byte UTF-8 characters: %lu\n", amount_1_byte);
	printf("2-byte UTF-8 characters: %lu\n", amount_2_byte);
	printf("3-byte UTF-8 characters: %lu\n", amount_3_byte);
	printf("4-byte UTF-8 characters: %lu\n", amount_4_byte);

	return;
}

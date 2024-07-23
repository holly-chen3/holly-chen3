// COMP1521 21T2 ... final exam, question 6

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

void
convert_hexdump_to_bytes (FILE *hexdump_input, FILE *byte_output)
{
	// TODO
	// Hint: `man 3 fscanf`
	// Hint: `man 3 fseek`
	// Hint: See question text for
	//        third hint.
	int offset;
	while (fscanf(hexdump_input, "%x", &offset) == 1) {
		fseek(byte_output, offset, SEEK_SET);

		for (int i = 0; i < 16; i++) {
			int byte;
			fscanf(hexdump_input, "%x", &byte);
			fputc(byte, byte_output);
		}

		int byte;
		while ((byte = fgetc(hexdump_input)) != '\n' && byte != EOF) {
		}
	}
	
}

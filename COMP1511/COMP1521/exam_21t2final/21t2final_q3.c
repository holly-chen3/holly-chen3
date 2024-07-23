// COMP1521 21T2 ... final exam, question 3

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

void
cp (char *path_from, char *path_to)
{
	FILE *input_stream = fopen(path_from, "rb");
	if (input_stream == NULL) {
		perror(path_from);
		exit(1);
	}
	FILE *output_stream = fopen(path_to, "wb");
	if (output_stream == NULL) {
		perror(path_to);
		exit(1);
	}
	int c;
	while((c = fgetc(input_stream)) != EOF) {
		fputc(c, output_stream);
	}
	fclose(input_stream);
	fclose(output_stream);
}


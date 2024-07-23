// Written By Holly Chen, z5359932
#include <stdio.h>
#include <stdlib.h>

void collatz_func (int num);

int main(int argc, char **argv)
{
	if (argc == 1) {
		printf("Usage: %s NUMBER\n", argv[0]);
		return EXIT_FAILURE;
	}
	int num = atoi(argv[1]);
	collatz_func(num);
	return EXIT_SUCCESS;
}

void collatz_func (int num) {
	printf("%d\n", num);
	if (num == 1) {
		return;
	}
	if ((num % 2) == 0) {
		num = num / 2;
	} else {
		num = 3 * num + 1;
	}
	collatz_func(num);
}
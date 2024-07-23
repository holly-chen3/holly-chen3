#include <stdio.h>
#include <ctype.h>

int main(void) {
	int letter;
	while ((letter = getchar())!= EOF) {
		putchar(tolower(letter));
	}
	return 0;
}

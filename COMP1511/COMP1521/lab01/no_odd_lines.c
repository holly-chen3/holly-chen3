#include <stdio.h>
#include <string.h>

#define MAX_STRING_LENGTH 3000

int main(void) {
	char string[MAX_STRING_LENGTH + 1];
	while (fgets(string, sizeof(string), stdin) != NULL) {
		if (!(strlen(string) % 2)) {
			fputs(string,stdout);
		}
	}
	return 0;
}

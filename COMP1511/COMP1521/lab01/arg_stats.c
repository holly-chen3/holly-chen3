#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {

	if(argc == 1) {
		printf("Usage: %s NUMBER [NUMBER ...]\n", argv[0]);
	} else {
		int i = 1;
		int minimum = 1;
		int maximum = 1;
		int sum = 0;
		int product = 1;
		while (i < argc) {
			if (atoi(argv[i]) < atoi(argv[minimum])) {
				minimum = i;
			} 
			if (atoi(argv[i]) > atoi(argv[maximum])) {
				maximum = i;
			}
			sum += atoi(argv[i]);
			product *= atoi(argv[i]);
			i++;
		}
		printf("MIN:  %d\n", atoi(argv[minimum]));
		printf("MAX:  %d\n", atoi(argv[maximum]));
		printf("SUM:  %d\n", sum);
		printf("PROD: %d\n", product);
		printf("MEAN: %d\n", sum/(argc - 1));
	}

	return 0;
}

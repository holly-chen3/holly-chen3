#include <stdio.h>

int main(int argc, char *argv[]) {
    if (argc != 4) {
        fprintf(stderr, "Usage: %s <source file> <destination file>\n", argv[0]);
        return 1;
    }

    FILE *fp = fopen(argv[1], "w");
    if (fp == NULL) {
        perror(argv[1]);
        return 1;
    }

    int min = atoi(argv[2]);
    int max = atoi(argv[3]);
    while (min <= max) {
        fprintf(fp, "%d\n", min);
        min++;
    }
}
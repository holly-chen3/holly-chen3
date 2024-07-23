#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <source file> <destination file>\n", argv[0]);
        return 1;
    }
    FILE *fp = fopen(argv[1], "wb");
    if (fp == NULL) {
        perror(argv[1]);
        return 1;
    }
    int i = 2;
    while(argv[i] != NULL) {
        fputc(atoi(argv[i]), fp);
        i++;
    }
    fclose(fp);
    return 0;
}
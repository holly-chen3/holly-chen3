#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <source file> <destination file>\n", argv[0]);
        return 1;
    }
    FILE *fp = fopen(argv[1], "rb");
    
    if (fp == NULL) {
        perror(argv[1]);
        return 1;
    }
    long offset = -2;
    while(fseek(fp, offset, SEEK_END) == 0 && fgetc(fp) != '\n') {
        offset--;
    }
    fseek(fp, offset + 1, SEEK_END);
    int c;
    while ((c = fgetc(fp)) != EOF) {
        fputc(c, stdout);
    }
    fclose(fp);
    return 0;
}
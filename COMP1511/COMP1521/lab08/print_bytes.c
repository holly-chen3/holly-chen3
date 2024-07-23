#include <stdio.h>
#include <ctype.h>

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
    int c;
    size_t i = 0;
    while ((c = fgetc(fp)) != EOF) {
        printf("byte %4ld: %3d 0x%02x ", i, c, c);
        if (isprint(c)) {
            printf("'%c'", c);
        }
        printf("\n");
        i++;
    }
    return 0;
}
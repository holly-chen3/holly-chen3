#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    if (argc != 4) {
        fprintf(stderr, "Usage: %s <source file> <destination file>\n", argv[0]);
        return 1;
    }

    FILE *fp = fopen(argv[1], "wb");
    if (fp == NULL) {
        perror(argv[1]);
        return 1;
    }

    uint16_t start = atoi(argv[2]);
    uint16_t finish = atoi(argv[3]);

    while (start <= finish) {
        uint8_t left_num = (start >> 8);
        uint8_t right_num = (start & 0xFF);
        fputc(left_num, fp);
        fputc(right_num, fp);
        start++;
    }
    fclose(fp);
    return 0;
}
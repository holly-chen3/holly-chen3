#include <stdio.h>
#include <stdlib.h>

// print the locations of a specified byte sequence in a file
// the first command line argument is a filname
// other command line arguments are integers specifying a byte sequence
// all positions the byte sequence occurs in the file are printed

int main(int argc, char *argv[]) {
    FILE *fp = fopen(argv[1], "rb");

    ssize_t byte_position = 0;
    ssize_t first_position = 0;
    int c;
    
    while ((c = fgetc(fp)) != EOF) {
        int value = 2;
        if (c == atoi(argv[value])) {
            first_position = byte_position;
            value++;
            while (value < argc) {
                if ((c = fgetc(fp)) != EOF && c == atoi(argv[value])) {
                    value++;
                } else {
                    value = argc + 1;
                }
            }
            if (value == argc) {
                printf("Sequence found at byte position: %d\n", first_position);
                fclose(fp);
                return 0;
            }
        }
        byte_position++;
    }
    printf("Sequence not found\n");
    fclose(fp);
    return 0;
}

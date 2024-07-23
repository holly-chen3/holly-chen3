#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <math.h>

// print a specified byte from a file
//
// first command line argument specifies a file
// second command line argument specifies a byte location
//
// output is a single line containing only the byte's value
// printed as a unsigned decimal integer (0..255)
// if the location does not exist in the file
// a single line is printed saying: error

int main(int argc, char *argv[]) {
    FILE *fp = fopen(argv[1], "rb");
    long bit_position = atoi(argv[2]);
    struct stat s;
    stat(argv[1], &s);
    if (s.st_size < labs(bit_position)) {
        printf("error\n");
        return 0;
    }
    
    if (bit_position < 0) {
        // if byte position is negative
        fseek(fp, bit_position, SEEK_END);
    } else {
        // if byte position is 0 or positive
        fseek(fp, bit_position, SEEK_SET);
    }

    printf("%d\n", fgetc(fp));

    fclose(fp);

    return 0;
}

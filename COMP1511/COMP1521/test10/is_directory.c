// If the pathname exists and is a directory, 
// it should print 1, otherwise it should print 0.

// Written by HOLLY-CHEN (z5359932)
// on the 24/11/2021


#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>


int main(int argc, char *argv[]) {
    struct stat s;
    stat(argv[1], &s);
    if (S_ISDIR(s.st_mode)) {
        printf("%d\n", 1);
    } else {
        printf("%d\n", 0);
    }
    return 0;
}

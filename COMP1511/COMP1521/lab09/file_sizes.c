// Given one or more filenames as command line arguments. 
// It should print one line for each filename which gives 
// the size in bytes of the file. It should also print a line 
// giving the combined number of bytes in the files.

// By HOLLY-CHEN (z5359932) 
// Written in 12/11/2021

#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <stdio.h>
#include <stdlib.h>

long stat_file(char *pathname);

int main(int argc, char *argv[]) {
    size_t total = 0;
    for (int arg = 1; arg < argc; arg++) {
        total += (stat_file(argv[arg]));
    }
    printf("Total: %ld bytes\n", total);
    return 0;
}

long stat_file(char *pathname) {
    struct stat s;
    if (stat(pathname, &s) != 0) {
        perror(pathname);
        exit(1);
    }
    printf("%s: %ld bytes\n", pathname, s.st_size);
    return s.st_size;  
}
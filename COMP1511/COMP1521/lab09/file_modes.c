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

void stat_file(char *pathname);

int main(int argc, char *argv[]) {
    for (int arg = 1; arg < argc; arg++) {
        stat_file(argv[arg]);
    }
    return 0;
}

void stat_file(char *pathname) {
    struct stat s;
    if (stat(pathname, &s) != 0) {
        perror(pathname);
        exit(1);
    }
    if (S_ISREG(s.st_mode)) {
        printf("-");
    }
    if (S_ISDIR(s.st_mode)) {
        printf("d");
    }
    if (s.st_mode & S_IRUSR) {
        printf("r");
    } else {
        printf("-");
    }
    if (s.st_mode & S_IWUSR) {
        printf("w");
    } else {
        printf("-");
    }
    if (s.st_mode & S_IXUSR) {
        printf("x");
    } else {
        printf("-");
    }
    if (s.st_mode & S_IRGRP) {
        printf("r");
    } else {
        printf("-");
    }
    if (s.st_mode & S_IWGRP) {
        printf("w");
    } else {
        printf("-");
    }
    if (s.st_mode & S_IXGRP) {
        printf("x");
    } else {
        printf("-");
    }
    if (s.st_mode & S_IROTH) {
        printf("r");
    } else {
        printf("-");
    }
    if (s.st_mode & S_IWOTH) {
        printf("w");
    } else {
        printf("-");
    }
    if (s.st_mode & S_IXOTH) {
        printf("x");
    } else {
        printf("-");
    }
    printf(" %s\n", pathname);
}
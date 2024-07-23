//  getcwd and chdir example

#include <unistd.h>
#include <limits.h>
#include <stdio.h>
#include <string.h>

int main(void) {
    // use repeated chdir("..") to climb to root of the file system
    char pathname[PATH_MAX];
    while (1) {
        if (getcwd(pathname, sizeof pathname) == NULL) {
            perror("getcwd");
            return 1;
        }
        printf("getcwd() returned %s\n", pathname);

        if (strcmp(pathname, "examples/2_files.d") == 0) {
            return 0;
        }

        if (chdir("examples/2_files.d") != 0) {
            perror("chdir");
            return 1;
        }
    }
    return 0;
}
#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {
    int i = 0;
    while (i < argc) {
        if (argv[i] == hi) {
            printf("argv[%d] is the string \"hi\"!\n", i);
        }
        i++;
    }
}
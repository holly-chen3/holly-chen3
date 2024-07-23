// exam_q5.c
//
// This program was written by HOLLY-CHEN (z5359932)
// on 01-04-2021
//
// Prints out the least common command line argument.
// If there are multiple equally least-common arguments, 
// print out the one that appears first in the list.

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define MAX_ARGS 1000


int main(int argc, char *argv[]) {
    int i = 1;
    int min_num = MAX_ARGS;
    int min_counter = MAX_ARGS;
    while (i < argc) {
        int j = 1;
        int counter = 0;
        while (j < argc) {
            if (argv[i] == argv[j]) {
                counter++;
            }

            j++;
        }
        if (min_counter > counter) {
            min_counter = counter;
            min_num = i;
        } 
        i++;
    }    

    printf("%s", argv[min_num]);
    return 0;
}

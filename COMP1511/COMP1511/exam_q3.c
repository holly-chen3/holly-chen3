// exam_q3.c
//
// This program was written by HOLLY-CHEN (z5359932)
// on 01-05-2021
//
// Program will identify pairs of integers that have 
// the same value and figure out the distance between them, 
// then it will print out the minimum distance that it found.
#include <stdio.h>
#include <stdlib.h>

#define MAX_LENGTH 10000

int find_distance (int i, int j, int counter, int distance);

int main(void) {
    int num[MAX_LENGTH] = {};
    int min_distance = MAX_LENGTH;
    int distance = -1;
    int input = 0;
    while (scanf("%d", &num[input]) != EOF) {
        input++;
    }
    int i = 0;
    while (i < input) {
        int j = 0;
        int counter = 0;
        while (j < input) {
            if (i != j && num[i] == num[j]) {
                distance = find_distance(i, j, counter, distance);
                counter++;
            }
            j++;
        }
        if (distance < min_distance) {
            min_distance = distance;
        }
        i++;
    }
    printf("The minimum distance was: %d\n", min_distance);
	return 0;
}

int find_distance (int i, int j, int counter, int distance) {
    if (counter > 0) {
        if (distance > abs(j - i)) {
            return abs(j - i);
        } else {
            return distance;
        }
    } else {
        return abs(j - i);
    }
}

                
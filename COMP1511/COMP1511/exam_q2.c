// exam_q2.c
//
// This program was written by HOLLY-CHEN (z5359932)
// on 1-05-2021
//
// This program determines if the sum of every element in the first 
// linked list is greater than, less than, or equal to the sum of 
// every element in the second linked list.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

struct node {
    struct node *next;
    int          data;
};

struct node *strings_to_list(int len, char *strings[]);

// exam_q2() takes in the head of two lists and prints
// something based on which has the larger sum
int exam_q2(struct node *first, struct node *second){
    struct node *curr_first = first;
    int first_sum = 0;

    while (curr_first != NULL) {

        first_sum = first_sum + curr_first->data;

        curr_first = curr_first->next;
    }

    struct node *curr_second = second;
    int second_sum = 0;

    while (curr_second != NULL) {

        second_sum = second_sum + curr_second->data;

        curr_second = curr_second->next;
    }

    if (first_sum > second_sum) {
        return 1;
    } else if (first_sum < second_sum) {
        return -1;
    } else {
        return 0;
    }
    
}



// DO NOT CHANGE THIS MAIN FUNCTION
int main(int argc, char *argv[]) {
    // create two linked lists from command line arguments
    int dash_arg = argc - 1;
    while (dash_arg > 0 && strcmp(argv[dash_arg], "-") != 0) {
        dash_arg = dash_arg - 1;
    }
    struct node *first = strings_to_list(dash_arg - 1, &argv[1]);
    struct node *second = strings_to_list(argc - dash_arg - 1, &argv[dash_arg + 1]);

    int result = exam_q2(first, second);
    printf("%d\n", result);

    return 0;
}

// DO NOT CHANGE THIS FUNCTION
// create linked list from array of strings
struct node *strings_to_list(int len, char *strings[]) {
    struct node *head = NULL;
    for (int i = len - 1; i >= 0; i = i - 1) {
        struct node *n = malloc(sizeof (struct node));
        assert(n != NULL);
        n->next = head;
        n->data = atoi(strings[i]);
        head = n;
    }
    return head;
}

// exam_q6.c
//
// This program was written by HOLLY-CHEN (z5359932)
// on 01-04-2021
//
// 

#include <stdio.h>
#include <stdlib.h>

#define MAX_LENGTH 1000

struct node {
    int data;
    struct node *next;
};

struct stack {
    struct node *top;
    int size;
};


int opening_bracket(int bracket);
int closing_bracket(int bracket);
int closing_bracket(int bracket);
int brackets_match(int opening, int closing);
struct stack *create_stack(void);
struct node *new_node(int data);
void free_stack(struct stack *new_stack);
void stack_push(struct stack *new_stack, int item);
int stack_pop(struct stack *new_stack);
int stack_size(struct stack *new_stack); 

int main(void) {
    char string[MAX_LENGTH] = {};
    while (scanf("%s", string) != EOF) {
        char *brackets = &string[MAX_LENGTH];
        struct stack *first_stack = NULL;
        first_stack = create_stack();
        int valid = 1;
        int i = 0;
        while (brackets[i] != '\0' && valid) {
            int ch = brackets[i];
            if (opening_bracket(ch)) {
                stack_push(first_stack,ch);
            } else if (closing_bracket(ch)) {
                int recent_bracket = stack_pop(first_stack);
                if (!brackets_match(recent_bracket, ch)) {
                    valid = 0;
                }
            }
        }
        i++;
    }
    if (stack_size(first_stack) != 0 || !valid) {
        printf("Invalid Sequence, the correct closing sequence is:\n");
        int i = 0;
        while (i < stack_size(first_stack)) {
            struct node *close_stack = new_stack->top;
            char character = '0';
            while (close_stack != NULL) {
                if (close_stack->data == '(') {
                    character = ')';
                } 
                if (close_stack->data == '{') {
                    character = '}';
                } 
                if (close_stack->data == '<') {
                    character =  '>';
                }
                if (close_stack->data == '[') {
                    character =  ']';
                }
                close_stack = close_stack->next;
            }
            printf("%c", character);
        }
    } else {
        printf("Valid Sequence!\n");
    }
    free_stack(new_stack);
    
    return 0;
}

int opening_bracket(int bracket) {
    if (bracket == '(' || bracket == '{' || bracket == '<' || bracket == '[') {
        return 1;
    }
    return 0;
}

int closing_bracket(int bracket) {
    if (bracket == ')' || bracket == '}' || bracket == ']') {
        return 1;
    }
    return 0;
}

char close_bracket(struct stack *new_stack, int bracket) {
    struct node *close_stack = new_stack->top;
    char character = '0';
    while (close_stack != NULL) {
        if (close_stack->data == '(') {
            character = ')';
        } 
        if (close_stack->data == '{') {
            character = '}';
        } 
        if (close_stack->data == '<') {
            character =  '>';
        }
        if (close_stack->data == '[') {
            character =  ']';
        }
        close_stack = close_stack->next;
        return character;
    }
}

int brackets_match(int opening, int closing) {
    if (opening == '(' && closing == ')') {
        return 1;
    }
    if (opening == '{' && closing == '}') {
        return 1;
    }
    if (opening == '[' && closing == ']') {
        return 1;
    }
    return 0;
}

struct stack *create_stack(void) {
    struct stack *new_stack = malloc(sizeof(struct stack));
    new_stack->top = NULL;
    new_stack->size = 0;
}

struct node *new_node(int data) {
    struct node *new = malloc(sizeof(struct node));
    new->data = data;
    new->next = NULL;
}

void free_stack(struct stack *new_stack) {
    if (new_stack == NULL) {
        return;
    }
    struct node *curr = new_stack->top;
    struct node *prev = NULL;
    while (curr != NULL) {
        prev = curr;
        curr = curr->next;
        free(prev);
    }
    free(new_stack);
}

//Add and remove items from stacks.
//Removing the item returns item for use.
void stack_push(struct stack *new_stack, int item) {
    struct node *new = new_node(item);

    if (new_stack->top == NULL) {
        new_stack->top = new;
    } else {
        new->next = new_stack->top;
        new_stack->top = new;
    }
    new_stack->size++;
} 

//Return value which was just popped off.
int stack_pop(struct stack *new_stack) {
    if (new_stack->top == NULL) {
        exit(1);
    }
    int value = new_stack->top->data;
    struct node *tmp = new_stack->top;
    new_stack->top = new_stack->top->next;
    free(tmp);
    new_stack->size--;
    return value;
}

//Check the size of the stack
int stack_size(struct stack *new_stack) {
    return new_stack->size;
}


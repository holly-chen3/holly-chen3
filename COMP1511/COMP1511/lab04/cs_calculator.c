//Creating a simple calculator, reading different
//numbers of integers.
//By HOLLY CHEN (z5359932)
//12-03-2021
#include <stdio.h>
#include <math.h>
int function_one (int num_a);
int function_two (int num_a, int num_b);
int main(void) {
    int first_num;
    int num_a;
    int num_b;
    printf("Enter instruction: ");
    while (scanf ("%d", &first_num) == 1) {
        if (first_num == 1) {
            scanf("%d", &num_a);
            printf("%d\n", function_one(num_a)); 
        } else if (first_num == 2) {
            scanf("%d %d", &num_a, &num_b);
            printf("%d\n", function_two(num_a, num_b));
        }
        printf("Enter instruction: ");
    }
    return 0;
}

int function_one (int num_a) {
    return num_a * num_a;
}
int function_two (int num_a, int num_b) {
    return pow(num_a, num_b);
}

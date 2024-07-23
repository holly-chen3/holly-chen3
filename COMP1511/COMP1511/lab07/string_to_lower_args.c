//Reads command line arguments then prints them out. When it prints out, it 
//will convert all upper case letters to lower case.
//Written By HOLLY-CHEN (z5359932)
//02-04-2021

#include <stdio.h>

int main(int argc, char *argv[]) {
    int counter = 1;
    //argc shows how many words there are in the line.
    while (counter < argc) {
        
        char *current_string = argv[counter];
        int i = 0;
        while (current_string[i] != '\0') {
            if (current_string[i] >= 'A' && current_string[i] <= 'Z') {
                int alphabet_position = current_string[i] - 'A';
                current_string[i] = 'a' + alphabet_position;
            } else {
                current_string[i] = current_string[i];
            }
            putchar(current_string[i]);
            i++;
        }
        counter++;
    }
    putchar('\n');
}


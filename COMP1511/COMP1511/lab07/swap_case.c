// Function to print out a string that has had it's cases swapped.
// swap_case.c
//
// This program was written by HOLLY-CHEN (z5359932)
// on 02-04-2021
 
#include <stdio.h>

int swap_case(int character);

int main(void) {

    // TODO: Write this function, using the swap_case function.
    int character = getchar();
    while (character != EOF) {
        int new_character = swap_case(character);
        putchar(new_character);
        character = getchar();
    }
    return 0;
}

int swap_case(int character) {
    // TODO: Write this function, which should:
    //  - return character in lower case if it is an upper case letter
    //  - return character in upper case if it is an lower case letter
    //  - return the character unchanged otherwise
    if (character >= 'a' && character <= 'z') {
        int alphabet_position = character - 'a';
        return 'A' + alphabet_position;
    } else if (character >= 'A' && character <= 'Z') {
        int alphabet_position = character - 'A';
        return 'a' + alphabet_position;
    } else {
        return character;
    }
}

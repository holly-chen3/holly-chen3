//Reads characters from its input and writes the characters to its output 
//encrypted with a substitution cipher. 
//By HOLLY-CHEN (z5359932)
//06-04/2021

#include <stdio.h>
#include <stdlib.h>

int encrypt(int character, char *argv);

int main(int argc, char *argv[]) {
    int character = getchar();
    while (character != EOF) {
        int shifted_character = encrypt(character, argv[1]);
        putchar(shifted_character);
        character = getchar();
    }
}
int encrypt(int character, char *argv) {
    if (character >= 'a' && character <= 'z') {
        int alphabet_position = character -'a';
        return argv[alphabet_position];
    } else if (character >= 'A' && character <= 'Z') {
        int alphabet_position = character - 'A';
        return argv[alphabet_position] - 'a' + 'A';
    } else { 
        return character;
    }
}


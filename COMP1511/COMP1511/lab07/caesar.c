//reads characters from its input and writes the characters to its output 
//encrypted with a Caesar cipher.
//A Caesar cipher shifts each letter a certain number of positions in the 
//alphabet.
//By HOLLY-CHEN (z5359932)
//02-04-2021

#include <stdio.h>
#include <stdlib.h>

int encrypt(int character, int shift);

int main(int argc, char *argv[]) {
    if (argc == 2) {
        int shift = atoi(argv[1]) % 26;
        if (shift < 0) {
            shift = 26 + shift;
        }
        int character;
        character = getchar();
        while (character != EOF) {
            int shifted_character = encrypt(character, shift);
            putchar(shifted_character);
            character = getchar();
        }
    }
}

int encrypt(int character, int shift) {
    if (character >= 'a' && character <= 'z') {
        if (character + shift > 'z') {
            return (character + shift - 'a') % 26 + 'a';
        } 
        return character + shift;
    } else if (character >= 'A' && character <= 'Z') {
        if (character + shift > 'Z') {
            return (character + shift -'A') % 26 + 'A';
        }
        return character + shift;
    } else {
        return character;
    }
}

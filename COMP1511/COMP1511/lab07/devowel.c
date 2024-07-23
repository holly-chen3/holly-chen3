//Devowel reads characters from its input and writes the same characters
//to its output, except it does not write lower case vowels
//By HOLLY-CHEN (z5359932)
//02-04-2021

#include <stdio.h>

int is_vowel(int character);

int main(void) {
    int vowel = 0;
    int character;
    character = getchar();
    while (character != EOF) {
        if (!vowel) {
        putchar(character);
        }
        character = getchar();
        vowel = is_vowel(character);
    }
    return 0;
}

int is_vowel(int character) {
    if (character == 'a'||character == 'e') {
        return 1;
    } else if (character == 'i'||
    character == 'o'|| character == 'u') {
        return 1;
    } else {
        return 0;
    }
}

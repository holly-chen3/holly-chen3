// Written by HOLLY CHEN (z5359932) 
// on 13/11/2021




#include <stdio.h>



int main(int argc, char *argv[]) {
    FILE *fp = fopen(argv[1], "rb");

    int count = 0;
    int c;
    while ((c = fgetc(fp)) != EOF) {
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'|| c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
            count++;
        }
    }
    printf("%d\n", count);
    fclose(fp);
    return 0;
}
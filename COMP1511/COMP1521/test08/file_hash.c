#include <stdio.h>
#include <stdint.h>

uint32_t hash(uint32_t old_hash_value, uint8_t byte, size_t index);

int main(int argc, char *argv[]) {

    FILE *fp = fopen(argv[1], "rb");

    uint32_t old_hash_value = 0;
    int c;
    int index = 0;
    while ((c = fgetc(fp)) != EOF) {
        old_hash_value = hash(old_hash_value, c, index);
        index++;
    }
    printf("%d\n", old_hash_value);
    fclose(fp);
    return 0;
}

// DO NOT CHANGE THIS FUNCTION

uint32_t hash(uint32_t old_hash_value, uint8_t byte, size_t index) {
    return old_hash_value + (byte << (index % 17));
}

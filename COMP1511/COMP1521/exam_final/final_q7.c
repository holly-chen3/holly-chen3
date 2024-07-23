#include <stdio.h>

// copy file specified as command line argument 1 to
// file specified as command line argument 2
// convert UTF8 to ASCII by replacing multibyte UTF8 characters with '?'

int main(int argc, char *argv[]) {

    FILE *input_stream = fopen(argv[1], "rb");
    FILE *output_stream = fopen(argv[2], "wb");

    int two_mask = 0x6;
    int three_mask = 0xe;
    int four_mask = 0x1e;
    int c;
    while ((c = fgetc(input_stream)) != EOF) {
        if (!((c >> 7) | 0)) {
            fputc(c, output_stream);
        } else {
            if (!((c >> 5) ^ two_mask)) {
                fgetc(input_stream);
            } else if (!((c >> 4) ^ three_mask)) {
                int i = 0;
                while (i < 2) {
                    fgetc(input_stream);
                    i++;
                }
            } else if (!((c >> 3) ^ four_mask)) {
                int i = 0;
                while (i < 3) {
                    fgetc(input_stream);
                    i++;
                }
            } 
            fputc(63, output_stream);
        }
    }
    fclose(input_stream);
    fclose(output_stream);

    return 0;
}

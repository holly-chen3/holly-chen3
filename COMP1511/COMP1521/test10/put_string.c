#include <stdio.h>

#include "put_string.h"

// print s to stdout with a new line appended using fputc (only)

void put_string(char *s) {

   int byte = 0;
   while (s[byte] != '\0') {
      fputc(s[byte], stdout);
      byte++;
   }
   fputc('\n', stdout);
   return;
}

// diary.c appends one line to $HOME/.diary
// Written by HOLLY-CHEN (z5359932)
// On 22/11/2021

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_CONTENT_LENGTH 1000

int main(int argc, char *argv[]) {
    // need to get the filepath name 
	char *home_pathname = getenv("HOME");
	if (home_pathname == NULL) {
		home_pathname = ".";
	}
	char *filename = ".diary";
	int diary_pathname_length = strlen(home_pathname) + strlen(filename) + 2; // \0 + /
	char diary_pathname[diary_pathname_length];
	snprintf(diary_pathname, sizeof diary_pathname, "%s/%s", home_pathname, filename);
	// open the file for appending
	FILE *fp = fopen(diary_pathname, "ab");
	if (fp == NULL) {
		perror(diary_pathname);
		return 1;
	}

	int i = 1;
	while (i < argc) {
		fprintf(fp, "%s ", argv[i]);
		i++;
	}
	fprintf(fp, "\n");
	// close the file
	fclose(fp);
	return 0;
}

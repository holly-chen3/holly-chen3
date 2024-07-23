// COMP1521 21T2 ... final exam, question 7

#include <sys/types.h>
#include <sys/stat.h>

#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_PATHNAME_LEN 65535

const mode_t NEW_DIR_MODE = S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH;

void
cp_directory (char *dir_from, char *dir_to)
{
	// TODO
	// hint:
	// - `man 3 opendir`
	// - `man 3 readdir`
	// - `man 3 closedir`
	// - `man 2 mkdir`
	DIR *dirp = opendir(dir_from);
	mkdir(dir_to, NEW_DIR_MODE);
	char path[MAX_PATHNAME_LEN + 1];
	char path_cp[MAX_PATHNAME_LEN + 1];
	struct dirent *dp;
	while ((dp = readdir(dirp)) != NULL) {
		if (strcmp(dp->d_name, ".") != 0 && strcmp(dp->d_name, "..") != 0) {
			// append the string to make new pathname
			strcpy(path, dir_from);
			strcat(path, "/");
			strcat(path, dp->d_name);
			strcat(path, "\0");
			strcpy(path_cp, dir_to);
			strcat(path_cp, "/");
			strcat(path_cp, dp->d_name);
			strcat(path_cp, "\0");


			struct stat s;
			stat(path, &s);

			if (S_ISDIR(s.st_mode)) {
				cp_directory(path, path_cp);
			} else {
				FILE *input_stream = fopen(path, "rb");
				FILE *output_stream = fopen(path_cp, "wb");
				chmod(path_cp, s.st_mode);
				int c;
				while ((c = fgetc(input_stream)) != EOF) {
					fputc(c, output_stream);
				}
				fclose(input_stream);
				fclose(output_stream);
			}
		}
	}
	closedir(dirp);
}



#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Fs.h"

int doTotalTextSize(File f);

int totalTextSize(Fs fs) {
	// loop through the fs
	// if it is a directory, go into the directory
	// if it is a text file, get the length of the text in file.
	if (fs == NULL || fs->root == NULL) {
		return 0;
	} 
	
	return doTotalTextSize(fs->root);
}

int doTotalTextSize(File f) {
	int totalSize = 0;
	for (FileList curr = f->files; curr != NULL; curr = curr->next) {
		if (curr->file->type == TEXT_FILE) {
			totalSize += strlen(curr->file->text);
		} else {
			totalSize += doTotalTextSize(curr->file);
		}
	}
	return totalSize;
}

// COMP2521 Assignment 1
// By Holly Chen (z5359932)

#include <assert.h>
#include <ctype.h>
#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "invertedIndex.h"

#define MAX_WORD 100


// Part 1 helper functions
void normaliseWord(char *word);
InvertedIndexBST newInvertedIndexNode(char *item);
InvertedIndexBST insertInvertedIndex(char *fileName, InvertedIndexBST tree, char *item);
FileList createFileNode(char *fileName);
FileList updateFileList(char *fileName, FileList l);
double findWordCount(char *filename);
void recurseprintInvertedIndex(InvertedIndexBST tree, FILE *out);
void freeFileList(FileList fileList);
// Part 2 helper functions
double calculateIdf(FileList filelist, int D);
TfIdfList newTfidfNode(char *filename, double tfidf);
TfIdfList generateTfIdf(FileList list, double idf, TfIdfList tfidflist);
TfIdfList iterateinsertTfIdf(TfIdfList tfidflist);
TfIdfList insertTfIdf(char *filename, double tfidf, TfIdfList tfidflist);
FileList recurseSearchOne(InvertedIndexBST tree, char *searchWord);

// Part 1

InvertedIndexBST generateInvertedIndex(char *collectionFilename) {
	// open collectionfile to read
	FILE *fp = fopen(collectionFilename, "rb");
	// make sure there is no error when opening
	if (fp == NULL) {
		fprintf(stderr, "error: failed to open %s\n", collectionFilename);
		exit(EXIT_FAILURE);
	}
	// initialise the filename and word
	char fileName[MAX_WORD];
	char word[MAX_WORD];
	// create an empty tree
	InvertedIndexBST newTree = NULL;
	// scan through every file and read
	while (fscanf(fp, "%s", fileName) == 1) {
		FILE *in = fopen(fileName, "rb");
		if (in == NULL) {
			fprintf(stderr, "error: failed to open %s\n", fileName);
			exit(EXIT_FAILURE);
		}
		// now go through every word
		// normalise it then make a new node to put into balanced tree
		// create a struct every time
		// if the node with the char *word already exists,
		// then do not insert a new one
		// compare words with each other (to determine left or right)
		
		// scan each word in the file
		while (fscanf(in, "%s", word) == 1) {
			// normalise word
			normaliseWord(word);
			// insert new word into tree
			newTree = insertInvertedIndex(fileName, newTree, word);
		}
		// close file
		fclose(in);
	}
	// close file
	fclose(fp);
	return newTree;
}

// helper function for normalising a word
void normaliseWord(char *word) {
	int i;
    for (i = 0; word[i] != '\0'; i++) {
        word[i] = tolower(word[i]);
    }
	i--;
	// make sure that i is not accessing the null
	// terminator
	while (i >= 0) {
		// if it has this punctuation, put a null terminator
		if (word[i] == '.' || word[i] == ',' || word[i] == ':' ||
			word[i] == ';' || word[i] == '?' || word[i] == '*') {
			word[i] = '\0';
		} else {
			// return if it is a normal letter
			return;
		}
		i--;
	}
    return;
}

// helper function to make new node containing data
// add the filename
InvertedIndexBST newInvertedIndexNode(char *item) {
	InvertedIndexBST new = malloc(sizeof(*new)); 
	new->word = malloc(sizeof(char) * (strlen(item) + 1));
	assert (new != NULL);
	strcpy(new->word, item);
	new->fileList = NULL;
	new->left = NULL;
	new->right = NULL;
	
	return new;
}


// helper function that iterates and inserts new nodes into the tree
InvertedIndexBST insertInvertedIndex(char *fileName, InvertedIndexBST tree, char *item) {
    
	if (tree == NULL) {
		// if the tree is empty, create a new node and update the filelist
		InvertedIndexBST new_index = newInvertedIndexNode(item);
		new_index->fileList = updateFileList(fileName, new_index->fileList);
		return new_index;
	} else {
		// compare the scanned in word to the words in the tree
		int cmp = strcmp(item, tree->word);
		if (cmp < 0) {
			// traverse the tree from the left if the word is less than the tree->word
			tree->left = insertInvertedIndex(fileName, tree->left, item);
		} else if (cmp > 0) {
			// traverse right if word is more than tree->word
			tree->right = insertInvertedIndex(fileName, tree->right, item);
		} else {
			// avoid duplicating words and add to file list
			tree->fileList = updateFileList(fileName, tree->fileList);
		}
		return tree;
	} 
}

// helper function that creates a file node
FileList createFileNode(char *fileName) {
	FileList newFileList = malloc(sizeof(*newFileList));
	newFileList->filename = malloc(sizeof(char)*(strlen(fileName) + 1));
	assert(newFileList != NULL);
	strcpy(newFileList->filename, fileName);
	// term frequency will change. Right now it is the 
	// count of the word over the number of words in the 
	// entire folder
	newFileList->tf = 1/findWordCount(fileName);
	newFileList->next = NULL;
	return newFileList;
}

// helper function that inserts a file node into the file list
// and updates the frequency of term in the file. 
FileList updateFileList(char *fileName, FileList l) {
	if (l == NULL) {
		// if list is empty, add a new node into the list
		FileList newList = createFileNode(fileName);
		return newList;
	} else {
		// compare the filename to existing filenames in the list
		int cmp = strcmp(fileName, l->filename);
		if (cmp == 0) {
			// another word that is the same within 
			// the file
			l->tf += 1/findWordCount(fileName);
			return l;
		} else if (cmp < 0) {
			// adds the node ordered in the list
			FileList newList = createFileNode(fileName);
			newList->next = l;
			return newList;
		} else {
			// recurse through until a base case occurs
			l->next = updateFileList(fileName, l->next);
		}
		return l;
	}
}

// find the total word count of a file
double findWordCount(char *filename) {
	FILE *readFile = fopen(filename, "rb");
	if (readFile == NULL) {
		fprintf(stderr, "error: failed to open %s\n", filename);
		exit(EXIT_FAILURE);
	}
	char word[MAX_WORD];
	double wordCount = 0;
	while (fscanf(readFile, "%s", word) == 1) {
		wordCount++;
	}
	fclose(readFile);
	return wordCount;
}

void printInvertedIndex(InvertedIndexBST tree, char *filename) {
	// print inverted index to the file
	FILE *out = fopen(filename, "wb");
	if (out == NULL) {
		fprintf(stderr, "error: failed to open %s\n", filename);
		exit(EXIT_FAILURE);
	}
	recurseprintInvertedIndex(tree, out);
	fclose(out);
}

// helper function for printing inverted index
// recurses, using the LNR approach
void recurseprintInvertedIndex(InvertedIndexBST tree, FILE *out) {
	if (tree == NULL) {
		return;
	}
	recurseprintInvertedIndex(tree->left, out);
	
	fprintf(out, "%s", tree->word);
	FileList curr = tree->fileList;
	while (curr != NULL) {
		fprintf(out, " %s (%.7lf)", curr->filename, curr->tf);
		curr = curr->next;
	}
	fprintf(out, "\n");
	recurseprintInvertedIndex(tree->right, out);
}

void freeInvertedIndex(InvertedIndexBST tree) {
	if (tree == NULL) {
		return;
	}
	// recurse through the function
	freeInvertedIndex(tree->left);
	freeInvertedIndex(tree->right);
	// call the free list function
	freeFileList(tree->fileList);
	free(tree->word);
	free(tree);
}

void freeFileList(FileList fileList) {
	if (fileList == NULL) return;
	freeFileList(fileList->next);
	free(fileList->filename);
	free(fileList);
}

// Part 2

TfIdfList searchOne(InvertedIndexBST tree, char *searchWord, int D) {
	// search through the tree and find the word

	// if the word is the right one
	// insert into the tfidf list
	// insert tfidf is going to loop through the 
	// curr node file list and get all the information 
	FileList filelist = recurseSearchOne(tree, searchWord);
	if (filelist == NULL) {
		return NULL;
	}
	double idf = calculateIdf(filelist, D);
	TfIdfList newTfIdfList = NULL;
	newTfIdfList = generateTfIdf(filelist, idf, newTfIdfList);
	return newTfIdfList;
}

// recursively search through the tree and find the word
FileList recurseSearchOne(InvertedIndexBST tree, char *searchWord) {
	if (tree == NULL) {
		return NULL;
	} else {
		int cmp = strcmp(searchWord, tree->word);
		if (cmp == 0) {
			// if it is the right word, return the filelist pointer
			return tree->fileList;
		} else if (cmp < 0) {
			return recurseSearchOne(tree->left, searchWord);
		} else {
			return recurseSearchOne(tree->right, searchWord);
		}
	}
}

// helper function to find out the idf
double calculateIdf(FileList filelist, int D) {
	FileList curr = filelist;
	double count = 0;
	while (curr != NULL) {
		count++;
		curr = curr->next;
	}
	return log10(D/count);
}

// helper function to make a new tfidflist node
TfIdfList newTfidfNode(char *filename, double tfidf) {
	TfIdfList newNode = malloc(sizeof(*newNode));
	assert(newNode != NULL);
	newNode->filename = malloc(sizeof(char) * (strlen(filename) + 1));
	strcpy(newNode->filename, filename);
	newNode->tfIdfSum = tfidf;
	newNode->next = NULL;
	return newNode;
}

TfIdfList generateTfIdf(FileList list, double idf, TfIdfList tfidflist) {
	for (;list != NULL; list = list->next) {
		// calculates the tfidf
		double tfidf = list->tf * idf;
		tfidflist = insertTfIdf(list->filename, tfidf, tfidflist);
	}
	return tfidflist;
}
TfIdfList insertTfIdf(char *filename, double tfidf, TfIdfList tfidflist) {
	if (tfidflist == NULL) {
		// if the list is empty initially
		TfIdfList newNode = newTfidfNode(filename, tfidf);
		return newNode;
	} // if the list is not empty, insert the tfidf filenode
	// in the right order (tfidf) must be in descending order
	// and filenames in alphabetical order
	else {
		// added on part for search many
		// if the file name is the same in the list,
		// then you add the tfidf sum together
		int cmp = strcmp(filename, tfidflist->filename);
		if (cmp == 0) {
			tfidflist->tfIdfSum += tfidf;
			return tfidflist;
		} // if tfidf is less than or equal to, 
		// continue recursing through the list
		else if (tfidf <= tfidflist->tfIdfSum) {
			tfidflist->next = insertTfIdf(filename, tfidf, tfidflist->next);
			return tfidflist;
		} // tfidf > tfidflist->tfidfsum
		else {
			// create a new node and insert new node into 
			// existing list
			TfIdfList newNode = newTfidfNode(filename, tfidf);
			newNode->next = tfidflist;
			return newNode;
		}
	}
}

// helper function to call insertTfIdf iteratively
TfIdfList iterateinsertTfIdf(TfIdfList tfidflist) {
	if (tfidflist == NULL) return NULL;
	TfIdfList curr = tfidflist;
	int i = 0;
	while (i < 2) {
		TfIdfList newList = NULL;
		while(curr != NULL) {
			newList = insertTfIdf(curr->filename, curr->tfIdfSum, newList);
			curr = curr->next;
		}
		curr = newList;
		i++;
	}
	return curr;
}

TfIdfList searchMany(InvertedIndexBST tree, char *searchWords[], int D) {
	// loop through the array of search words

	TfIdfList manyWords = NULL;
	for (int i = 0; searchWords[i] != NULL; i++) {
		// word now searched for, the search one returns a list of 
		// all the files that contain that word and their tfidf
		TfIdfList searchedWord = searchOne(tree, searchWords[i], D);
		// put the different search word lists together. 
		for (;searchedWord != NULL; searchedWord = searchedWord->next) {   
			manyWords = insertTfIdf(searchedWord->filename, searchedWord->tfIdfSum, manyWords);       
		}
	}
	// make sure that the list is ordered and there are no duplicates
	manyWords = iterateinsertTfIdf(manyWords);
	return manyWords;
}

void freeTfIdfList(TfIdfList list) {
	if (list == NULL) return;
	freeTfIdfList(list->next);
	free(list->filename);
	free(list);
}


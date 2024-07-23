////////////////////////////////////////////////////////////////////////
// COMP1521 21T3 --- Assignment 2: `chicken', a simple file archiver
// <https://www.cse.unsw.edu.au/~cs1521/21T3/assignments/ass2/index.html>
//
// Written by HOLLY-CHEN (z5359932) on 10-11-2021.
//
// 2021-11-08   v1.1    Team COMP1521 <cs1521 at cse.unsw.edu.au>

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <assert.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h>
#include <dirent.h>

#include "chicken.h"


// ADD ANY extra #defines HERE
#define MAX_PATHNAME_LEN 65535

// ADD YOUR FUNCTION PROTOTYPES HERE
uint64_t check_egg_length(FILE *f, uint8_t *hash_value, int egg_length);
void check_egg_byte(uint8_t *hash_value, FILE *f, int byte_length);
uint8_t check_egg_hash(FILE *f, uint8_t *hash_value);
void create_egg_hash(uint8_t *hash_value, uint8_t c, FILE *f);
void create_egg_permissions(uint8_t *hash_value, FILE *egg, struct stat s);
void extract_files(char *pathname, mode_t mode, FILE *f);
void extract_directories(char *pathname, mode_t mode, FILE *f);
void create_egg_file(char *pathname, FILE *egg, int format);
void create_egg_directory(char *pathname, char *pathnames, FILE *egg, int format);
void create_egg_directory_or_file(char *path, char *pathnames, FILE *egg, int format);

// print the files & directories stored in egg_pathname (subset 0)
//
// if long_listing is non-zero then file/directory permissions, formats & sizes are also printed (subset 0)

void list_egg(char *egg_pathname, int long_listing) {
    // open egg_pathname to be read
    FILE *f = fopen(egg_pathname, "rb");

    if (f == NULL) {
        perror(egg_pathname);
        exit(1);
    }
    // check if it is the magic number or end of file 
    // at the beginning of each file and directory.
    while (fgetc(f) != EOF) {
        // get the eggliet format
        uint8_t egglet_format = fgetc(f);
        // find the mode of the file or directory
        // and store it.
        int byte = 0;
        char permissions[EGG_LENGTH_MODE + 1];
        while (byte < EGG_LENGTH_MODE) {
            permissions[byte] = fgetc(f);
            byte++;
        }
        // find the pathname length
        uint16_t pathl_byte1 = fgetc(f);
        uint16_t pathl_byte2 = fgetc(f);
        uint16_t pathnamel = (pathl_byte2 << 8) | pathl_byte1;
        // store the pathname
        byte = 0;
        char pathname[pathnamel + 1];
        while (byte < pathnamel) {
            pathname[byte] = fgetc(f);
            byte++;
        }
        // find the content length
        int contentl_byte = 0;
        uint64_t contentl = 0;
        while (contentl_byte < EGG_LENGTH_CONTLEN) {
            uint64_t contentl_c = fgetc(f);
            contentl = (contentl_c << (8 * contentl_byte)) | contentl;
            contentl_byte++;
        }
        // if user inputs -L
        if (long_listing) {
            // print out the file or directory's mode
            byte = 0;
            while (byte < EGG_LENGTH_MODE) {
                printf("%c", permissions[byte]);
                byte++;
            }
            // print out the egglet format
            printf("%3c", egglet_format);
            // print out the file size
            uint64_t file_size = contentl;
            printf("%7lu  ", file_size);
        }
        // print out the pathname
        int pathname_byte = 0;
        while (pathname_byte < pathnamel) {
            printf("%c", pathname[pathname_byte]);
            pathname_byte++;
        }
        printf("\n");
        // fseek to the beginning of the next
        // file or directory.
        fseek(f, contentl + EGG_LENGTH_HASH, SEEK_CUR);
    }
    // close the egg_pathname
    fclose(f);
}


// check the files & directories stored in egg_pathname (subset 1)
//
// prints the files & directories stored in egg_pathname with a message
// either, indicating the hash byte is correct, or
// indicating the hash byte is incorrect, what the incorrect value is and the correct value would be

void check_egg(char *egg_pathname) {
    // open egg pathname to read.
    FILE *f = fopen(egg_pathname, "rb");

    if (f == NULL) {
        perror(egg_pathname);
        exit(1);
    }
    // store magic number 
    int magic_num;
    while ((magic_num = fgetc(f)) != EOF) {
        // initialise hash value to 0
        uint8_t hash_value = 0;
        // if the magic number is wrong, 
        // print an error statement.
        if (magic_num != EGGLET_MAGIC) {
            fprintf(stderr, "error: incorrect first egglet byte: 0x%x should be 0x63\n", magic_num);
            exit(1);
        }
        // add up the hash_value for magic number
        hash_value = egglet_hash(hash_value, magic_num);
        // check every byte in the egg up to the pathname length
        check_egg_byte(&hash_value, f, EGG_OFFSET_PATHNLEN - 1);
        uint16_t pathnamel = check_egg_length(f, &hash_value, EGG_LENGTH_PATHNLEN);
        // print out the pathname
        int byte = 0;
        while (byte < pathnamel) {
            printf("%c", check_egg_hash(f, &hash_value));
            byte++;
        }
        // check the content length
        uint64_t contentl = check_egg_length(f, &hash_value, EGG_LENGTH_CONTLEN);
        // check every byte in the egg for the file content
        check_egg_byte(&hash_value, f, contentl);
        // get the file's hash
        uint8_t hash = fgetc(f);
        if (hash_value == hash) {
            printf(" - correct hash\n");
        } else {
            printf(" - incorrect hash 0x%x should be 0x%x\n", hash_value, hash);
        }
    }
    fclose(f);
}


// extract the files/directories stored in egg_pathname (subset 2 & 3)

void extract_egg(char *egg_pathname) {
    // open the egg_pathname for reading
    FILE *f = fopen(egg_pathname, "rb");
    if (f == NULL) {
        perror(egg_pathname);  
        exit(1);
    }
    // check if it is the magic number or end of file 
    // at the beginning of each file and directory.
    while(fgetc(f) != EOF) {
        // go to the mode of the file
        fseek(f, EGG_OFFSET_MODE - 1, SEEK_CUR);
        // find the permissions of the file
        int byte = 0;
        char permissions[EGG_LENGTH_MODE + 1];
        while (byte < EGG_LENGTH_MODE) {
            permissions[byte] = fgetc(f);
            byte++;
        }
        // make sure to end with a null terminator
        permissions[EGG_LENGTH_MODE] = '\0';
        // find the pathname length
        int pathnamel_byte = 0;
        uint16_t pathnamel = 0;
        while (pathnamel_byte < EGG_LENGTH_PATHNLEN) {
            uint16_t pathnamel_c = fgetc(f);
            pathnamel = (pathnamel_c << (8 * pathnamel_byte)) | pathnamel;
            pathnamel_byte++;
        }
        // store the pathname
        byte = 0;
        char pathname[pathnamel + 1];
        while (byte < pathnamel) {
            pathname[byte] = fgetc(f);
            byte++;
        }
        pathname[pathnamel] = '\0';
        // put the mode into the file or directory
        mode_t mode = 0;
        const char chars[] = "rwxrwxrwx";
        for (size_t i = 0; i < 9; i++) {
            if (chars[i] == permissions[i + 1]) {
                mode = mode | (1 << (8 - i));
            } else if (permissions[i + 1] != '-'){
                fprintf(stderr, "Error: Invalid Mode\n");
                exit(1);
            }
        }
        // if it is a file, then extract the file
        if (permissions[0] == '-') {
            extract_files(pathname, mode, f);
            fseek(f, EGG_LENGTH_HASH, SEEK_CUR);
        } else if (permissions[0] == 'd') {
            // if it is a directory, create the directory
            if (mkdir(pathname, mode) != 0) {
                perror(pathname);  // prints why the mkdir failed
                exit(1);
            }  
            printf("Creating directory: %s\n", pathname);
            fseek(f, EGG_LENGTH_CONTLEN + EGG_LENGTH_HASH, SEEK_CUR);
        }
    }
    // close the egg pathname
    fclose(f);
}


// create egg_pathname containing the files or directories specified in pathnames (subset 3)
//
// if append is zero egg_pathname should be over-written if it exists
// if append is non-zero egglets should be instead appended to egg_pathname if it exists
//
// format specifies the egglet format to use, it must be one EGGLET_FMT_6,EGGLET_FMT_7 or EGGLET_FMT_8

void create_egg(char *egg_pathname, int append, int format,
                int n_pathnames, char *pathnames[n_pathnames]) {
    // initialise egg with NULL
    FILE *egg = NULL;
    if (append) {
        // if append, then open egg with ab
        egg = fopen(egg_pathname, "ab");
        if (egg == NULL) {
            perror(egg_pathname);
            exit(1);
        }
    } else {
        // if write, then open egg with wb
        egg = fopen(egg_pathname, "wb");
        if (egg == NULL) {
            perror(egg_pathname);
            exit(1);
        }
    }
    
    int num = 0;
    while(num < n_pathnames) {
        // for every argument in the command line, 
        // loop through the files
        char *pathn = pathnames[num];
        // split up the pathname with the character "/"
        char *pathname = strtok(pathnames[num], "/");
        struct stat s;
        if (stat(pathname, &s) != 0) {
            perror(pathname);
            exit(1);
        }
        // if it is a regular file, then add regular file
        if (S_ISREG(s.st_mode)) {
            create_egg_file(pathname, egg, format);
        }
        // if it is a directory, then find the path 
        if (S_ISDIR(s.st_mode)) {
            create_egg_directory(pathname, pathn, egg, format);
            
        }
        num++;
    }
    // close newly created egg
    fclose(egg);
}



// ADD YOUR EXTRA FUNCTIONS HERE

// add up hash value as bytes are read
uint8_t check_egg_hash(FILE *f, uint8_t *hash_value) {
    int c = fgetc(f);
    *hash_value = egglet_hash(*hash_value, c);
    return c;
}
// check the length of pathname and content
uint64_t check_egg_length(FILE *f, uint8_t *hash_value, int egg_length) {
    int byte = 0;
    uint64_t length = 0;
    while (byte < egg_length) {
        uint64_t c = check_egg_hash(f, hash_value);
        length = (c << (8 * byte)) | length;
        byte++;
    }
    return length;
}
// input a byte to the egg and calculate hash value
void create_egg_hash(uint8_t *hash_value, uint8_t c, FILE *f) {
    fputc(c, f);
    *hash_value = egglet_hash(*hash_value, c);
}
// calculate every permission byte and the hash value
void create_egg_permissions(uint8_t *hash_value, FILE *egg, struct stat s) {
    const char chars[] = "rwxrwxrwx";
    for (size_t i = 0; i < 9; i++) {
        if(s.st_mode & (1 << (8 - i))) {
            create_egg_hash(hash_value, chars[i], egg);
        } else {
            create_egg_hash(hash_value, '-', egg);
        }
    }
}
// read every byte in the particular byte length and 
// calculate hash value
void check_egg_byte(uint8_t *hash_value, FILE *f, int byte_length) {
    int byte = 0;
    while (byte < byte_length) {
        check_egg_hash(f, hash_value);
        byte++;
    }
}
// extract file
void extract_files(char *pathname, mode_t mode, FILE *f) {
    // open file to be written in 
    FILE *output_f = fopen(pathname, "wb");
    if (output_f == NULL) {
        perror(pathname);
        exit(1);
    }
    // change the mode of the file
    if (chmod(pathname, mode) != 0) {
        perror(pathname);  // prints why the chmod failed
        exit(1);
    }
    // calculate the content length
    int contentl_byte = 0;
    uint64_t contentl = 0;
    while (contentl_byte < EGG_LENGTH_CONTLEN) {
        uint64_t contentl_c = fgetc(f);
        contentl = (contentl_c << (8 * contentl_byte)) | contentl;
        contentl_byte++;
    }
    // input the content into the file
    int byte = 0;
    while (byte < contentl) {
        int c = fgetc(f);
        fputc(c, output_f);
        byte++;
    }
    printf("Extracting: %s\n", pathname);
    // close file 
    fclose(output_f);
}

void create_egg_file(char *pathname, FILE *egg, int format) {
    // initialise hash value to 0
    uint8_t hash_value = 0;
    // open file for reading
    FILE *f = fopen(pathname, "rb");
    if (f == NULL) {
        perror(pathname);
        exit(1);
    }
    printf("Adding: %s\n", pathname);
    // calculate the hash value of the file
    create_egg_hash(&hash_value, EGGLET_MAGIC, egg);
    create_egg_hash(&hash_value, format, egg);
    struct stat s;
    if (stat(pathname, &s) != 0) {
        perror(pathname);
        exit(1);
    }
    if (S_ISREG(s.st_mode)) {
        create_egg_hash(&hash_value, '-', egg);
    }
    if (S_ISDIR(s.st_mode)) {
        create_egg_hash(&hash_value, 'd', egg);
    }
    // calculate the hash value of the permissions
    create_egg_permissions(&hash_value, egg, s);
    // calculate pathname length bytes
    // make sure it is in little endian
    uint8_t pathnamel_byte1 = strlen(pathname) & 0xff;
    uint8_t pathnamel_byte2 = (strlen(pathname) >> 8) & 0xff;
    // calculate hash value of the pathname length bytes
    create_egg_hash(&hash_value, pathnamel_byte1, egg);
    create_egg_hash(&hash_value, pathnamel_byte2, egg);
    // calculate the hash value of the pathname
    int index = 0;
    while (index < strlen(pathname)) {
        create_egg_hash(&hash_value, pathname[index], egg);
        index++;
    }
    if (S_ISREG(s.st_mode)) {
        // calculate hash value of content length
        int byte = 0;
        while (byte < EGG_LENGTH_CONTLEN) {
            uint64_t contentl_byte = (s.st_size >> 8 * byte) & 0xff;
            create_egg_hash(&hash_value, contentl_byte, egg);
            byte++;
        }
        // input the content into the egg
        // calcalate the hash value of content
        int c;
        while ((c = fgetc(f)) != EOF) {
            create_egg_hash(&hash_value, c, egg);
        }
    } else {
        // if it is a directory, set content length to 0
        // calculate hash value of content length
        int byte = 0;
        while (byte < EGG_LENGTH_CONTLEN) {
            create_egg_hash(&hash_value, 0, egg);
            byte++;
        }
    }
    

    // input the hash value into the egg
    fputc(hash_value, egg);
    
    // close file
    fclose(f);
}

void create_egg_directory(char *pathn, char *pathnames, FILE *egg, int format) {
    // create a directory
    create_egg_file(pathn, egg, format);
    char path[MAX_PATHNAME_LEN + 1];
    DIR *dir = opendir(pathn);
    if (dir == NULL) {
        perror(pathn);  // prints why the open failed
        exit(1);
    }
    // if pathnames is now NULL, and it is still a 
    // directory, then add everything in the directory
    // if not, then jump to the next string
    if (pathnames != NULL) {
        pathnames = strtok(NULL, "/");
    }
    struct dirent *dp;
    // read the directory's contents
    while ((dp = readdir(dir)) != NULL) {
        if (pathnames == NULL) {
            // add everything in the directory
            if (strcmp(dp->d_name, ".") != 0 && strcmp(dp->d_name, "..") != 0) {
                // append the string to make new pathname
                strcpy(path, pathn);
                strcat(path, "/");
                strcat(path, dp->d_name);
                create_egg_directory_or_file(path, pathnames, egg, format);
            }
        } else {
            // find the file or directory that matches pathnames
            if (strcmp(dp->d_name, pathnames) == 0) {
                // append the string to make new pathname
                strcpy(path, pathn);
                strcat(path, "/");
                strcat(path, dp->d_name);
                create_egg_directory_or_file(path, pathnames, egg, format);
            }
            
        }
    }
    closedir(dir);
}

void create_egg_directory_or_file(char *path, char *pathnames, FILE *egg, int format) {
    // check permissions to see whether to call create egg file or directory.
    struct stat s;
    if (stat(path, &s) != 0) {
        perror(path);
        exit(1);
    }
    if (S_ISREG(s.st_mode)) {
        create_egg_file(path, egg, format);
    }
    if (S_ISDIR(s.st_mode)) {
        create_egg_directory(path, pathnames, egg, format);
    }
}
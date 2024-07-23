// Assignment 2 21T1 COMP1511: Beats by CSE
// beats.c
//
// This program was written by HOLLY-CHEN (z5359932)
// on 07-04-2021
//
// Version 1.0.0: Assignment released.

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

// Add any extra #includes your code needs here.

#include "ext_beats.h"

// Add your own #defines here.
#define HIGHEST_OCTAVE 10
#define HIGHEST_KEY 12
//////////////////////////////////////////////////////////////////////


struct track {

    struct beat *head;
    struct beat *curr;
};


struct beat {

    struct note *notes;
    struct beat *next;
};


struct note {

    int octave;
    int key;
    struct note *next;
};


//////////////////////////////////////////////////////////////////////

// Add prototypes for any extra functions you create here.

int add_note_to_beat_two(Beat beat, struct note *new_note, 
                         int octave, int key);

int add_note_to_beat_three(Beat beat, struct note *new_note, 
                           struct note *curr, int octave, int key);

int remove_selected_beat_two(Track track, Beat current, Beat prev);

struct beat *merging_beats(Beat beat, Beat beat_two);

struct note *check_lower_note(struct note *a, struct note *b);

struct note *copy_notes(struct note *old_note);


// Return a malloced Beat with fields initialized.
Beat create_beat(void) {
    Beat new_beat = malloc(sizeof (struct beat));
    assert(new_beat != NULL);

    new_beat->next = NULL;
    new_beat->notes = NULL;

    return new_beat;
}

//////////////////////////////////////////////////////////////////////
//                        Stage 1 Functions                         //
//////////////////////////////////////////////////////////////////////

// Add a note to the end of a beat.
int add_note_to_beat(Beat beat, int octave, int key) {

    //create a new note and initialise values.
    struct note *new_note = malloc(sizeof(struct note));
    new_note->next = NULL;
    new_note->octave = octave;
    new_note->key = key;

    if (octave < 0 || octave >= HIGHEST_OCTAVE) { //if octave is invalid
        free(new_note);
        return INVALID_OCTAVE;

    } else { //if octave is valid

        if (key < 0 || key >= HIGHEST_KEY) { //if key is invalid
            free(new_note);
            return INVALID_KEY;

        } else { //if key is valid
            //call function add_note_to_beat_two. 
            int add_note_two;
            add_note_two = add_note_to_beat_two(beat, new_note, 
                                                octave, key);
                                                
            return add_note_two;
        }
    }
}

//Extension of the function 'add_note_to_beat'.
int add_note_to_beat_two(Beat beat, struct note *new_note, 
                         int octave, int key) {
    struct note *curr = beat->notes;

    if (curr != NULL) { 

        while (curr->next != NULL) {

            curr = curr->next;

        }
        
        if (curr->octave > octave) { //if previous octave 
                                     //is higher than present octave.
            free(new_note);
            return NOT_HIGHEST_NOTE;

        } else {//if previous octave is lower or equal to present octave.

            //call function add_note_to_beat_three.
            int add_note_three;
            add_note_three = add_note_to_beat_three(beat, new_note, 
                                                    curr, octave, key);

            return add_note_three;

        }
    } else { //curr is NULL

        beat->notes = new_note;
        return VALID_NOTE;

    }
}

//Extension of the function 'add_note_to_beat_two'.
int add_note_to_beat_three(Beat beat, struct note *new_note, 
                           struct note *curr, int octave, int key) {
    if (curr->octave == octave && curr->key >= key) {
        free(new_note);
        return NOT_HIGHEST_NOTE;

    } else {

        curr->next = new_note;
        return VALID_NOTE;

    }
}

// Print the contents of a beat.
void print_beat(Beat beat) {
    struct note *curr = beat->notes;

    while (curr != NULL) {

        printf("%d ", curr->octave);

        if (curr->key < 10) {

            printf("0%d", curr->key);

        } else {

            printf("%d", curr->key);

        } 
        if (curr->next != NULL) {

            printf(" | ");
        } 

        curr = curr->next;
    } 
    printf("\n");
}

// Count the number of notes in a beat that are in a given octave.
int count_notes_in_octave(Beat beat, int octave) {
    int counter = 0;
    struct note *curr = beat->notes;

    while (curr != NULL) {

        if (curr->octave == octave) {

            counter++;

        }
        curr = curr->next;
    }

    return counter;
}

//////////////////////////////////////////////////////////////////////
//                        Stage 2 Functions                         //
//////////////////////////////////////////////////////////////////////

// Return a malloced track with fields initialized.
Track create_track(void) {
    Track new_track = malloc(sizeof(struct track));

    new_track->head = NULL;
    new_track->curr = NULL;
    return new_track;
}

// Add a beat after the current beat in a track.
void add_beat_to_track(Track track, Beat beat) {

    if (track->curr == NULL) { // if there is no selected beat 
                               // in the track.

        if (track->head == NULL) {

            track->head = beat;

        } else { //if track->head is not NULL.

            beat->next = track->head;
            track->head = beat;

        }
    } else { // if there is a selected beat in the track.

        beat->next = track->curr->next;
        track->curr->next = beat; 

    }
}

// Set a track's current beat to the next beat.
int select_next_beat(Track track) {
    if (track->curr == NULL) { //if there is no currently selected beat.

        if (track->head != NULL) { //if there is at least 
                                   //one beat in track.

            track->curr = track->head;
            return TRACK_PLAYING;

        } else { //if there is no beat in track.

            return TRACK_STOPPED;

        }
    } else { // if there is a currently selected beat.

        if (track->curr->next == NULL) { //if the currently selected 
                                         //beat is the last beat 
                                         //in the track.

            track->curr = NULL;
            return TRACK_STOPPED;

        } else { //if the currently selected beat is not the last
                 //beat in the track.

            track->curr = track->curr->next;
            return TRACK_PLAYING;

        }
    }
}

// Print the contents of a track.
void print_track(Track track) {
    struct beat *current = track->head;
    int counter = 1;

    while (current != NULL) {

        if (current == track->curr) {

            printf(">");

        } else {

            printf(" ");

        }

        printf("[%d] ", counter);
        print_beat(current);

        counter++;
        current = current->next;

    }
}

// Count beats after the current beat in a track.
int count_beats_left_in_track(Track track) {
    if (track->head == NULL) {
        return 0;

    }

    if (track->curr != NULL) { //there is a selected beat in track.

        struct beat *current = track->curr;
        int counter = 0;

        while (current->next != NULL) {

            current = current->next;
            counter++;

        }

        return counter;

    } else { //there is no selected beat in track
             //return number of beats in the track.

        struct beat *current = track->head;
        int counter = 0;

        while (current != NULL) {

            current = current->next;
            counter++;

        }

        return counter;
    }
}

//////////////////////////////////////////////////////////////////////
//                        Stage 3 Functions                         //
//////////////////////////////////////////////////////////////////////

// Free the memory of a beat, and any memory it points to.
void free_beat(Beat beat) {
    if (beat->notes != NULL) {
         
        struct note *current = beat->notes;

        while (current != NULL) {

            struct note *free_note = current;
            current = current->next;
            free(free_note);

        }
    } 

    free(beat);
}

// Free the memory of a track, and any memory it points to.
void free_track(Track track) {
    if (track == NULL) {

        return;

    }

    if (track->head != NULL) {

        struct beat *current = track->head;

        while (current != NULL) {

            struct beat *free_current_beat = current;
            current = current->next;
            free_beat(free_current_beat);

        }
    }

    free(track);
}

// Remove the currently selected beat from a track.
int remove_selected_beat(Track track) {
    struct beat *current = track->head;
    struct beat *prev = NULL;

    while (current != track->curr) {

        prev = current;
        current = current->next;

    }

    if (track->curr != NULL) {

        int remove_beat;
        remove_beat = remove_selected_beat_two(track, 
        current, prev);

        return remove_beat;

    } else { //if track does not have a selected beat.

        return TRACK_STOPPED;

    }
}

//Extension of function 'remove_selected_beat'.
int remove_selected_beat_two(Track track, Beat current, Beat prev) {
    struct beat *free_current_beat = track->curr;

    if (track->curr->next == NULL) {

        if (prev != NULL) {
            prev->next = current->next;

        } else {
            track->head = NULL;
            
        }

        free_beat(free_current_beat);
        track->curr = NULL;

        return TRACK_STOPPED;

    } else {

        if (prev != NULL) {
            prev->next = current->next;

        } else {
            track->head = current->next;

        }

        track->curr = track->curr->next;
        free_beat(free_current_beat);

        return TRACK_PLAYING;
    }
}

////////////////////////////////////////////////////////////////////////
//                Extension -- Stage 4 Functions                      //
////////////////////////////////////////////////////////////////////////

// Merge `beats_to_merge` beats into `merged_beats`
//function finds two beats to pass
//and loops through till the last bit.
void merge_beats(Track track, int beats_to_merge, int merged_beats) {
    // a beat in the track needs to be selected before merging beats.
    if (merged_beats > 0 && track->curr != NULL) {
        int divided_beats = beats_to_merge / merged_beats;
        int extra_beats = beats_to_merge % merged_beats; 
        int first_beat_merge = extra_beats + divided_beats;
        int counter = 1;
        int count_merge = 1;
        Beat merge_curr = track->curr;
        Beat merge_next = NULL;
        if (beats_to_merge < merged_beats) {
            return;
        }
        while (merge_curr->next != NULL && counter < first_beat_merge) {
            
            merge_next = merge_curr->next;
            
            struct beat *final = merging_beats(merge_curr, merge_next);

            final->next = merge_next->next;
            
            struct beat *prev = track->head;
            while (prev != merge_curr && prev->next != merge_curr) {
                prev = prev->next;
            }

            if (prev == track->head && merge_curr == track->head) {
                track->head = final;
            } else {
                prev->next = final;
            }
            free_beat(merge_curr);
            free_beat(merge_next);
            merge_curr = final;
            track->curr = final->next;
            
            counter++;
            if (counter == first_beat_merge 
                && count_merge < merged_beats) {
                counter = 1;
                first_beat_merge = divided_beats;
                count_merge++;
                merge_curr = merge_curr->next;
            }
        }
        //if after the while loop counter is still less than divided beats,
        if (counter < divided_beats && first_beat_merge == divided_beats) {
            if (merged_beats != 1) {
                track->curr = track->curr->next;
            }
        }
    }
}

struct beat *merging_beats(Beat beat, Beat beat_two) {
    //function gets two beats and 
    //merges them in the right order.
    if (beat == NULL || beat_two == NULL) {
        return beat;
    }
    struct note *a = beat->notes;
    struct note *b = beat_two->notes;
    struct beat *merged_list = malloc(sizeof(struct beat));
    merged_list->next = NULL;
    merged_list->notes = NULL;

    while (a != NULL || b != NULL) {
        //check lower note returns the lower note.
        struct note *lower_note = check_lower_note(a,b);
        //put the lower note into merged list.
        if (lower_note == NULL) {
            lower_note = a;
            b = b->next;
        }
        struct note *cpy_note = copy_notes(lower_note);

        if (merged_list->notes == NULL) {
            merged_list->notes = cpy_note;
            cpy_note->next = NULL;
        } else {
            struct note *current = merged_list->notes;
            while (current->next != NULL) {
                current = current->next;
            }
            current->next = cpy_note;
        }
        if (lower_note == a) {
            a = a->next;
        } else if (lower_note == b) {
            b = b->next;
        }
    }

    return merged_list;
}

struct note *check_lower_note(struct note *a, struct note *b) {
    //function checks which note is lower.
    if (a == NULL) {
        return b;
    } else if (b == NULL) {
        return a;
    }
    if (a->octave < b->octave) {
        return a;
    } else if (a->octave == b->octave) {
        if (a->key < b->key) {
            return a;
        } else if (a->key > b->key) {
            return b;
        } else {
            return NULL;
        }
    } else {
        return b;
    }
}

struct note *copy_notes(struct note *old_note) {
    //function copies lower note.
    struct note *cpy_note = malloc(sizeof(struct note));
    cpy_note->octave = old_note->octave;
    cpy_note->key = old_note->key;
    cpy_note->next = NULL;
    return cpy_note;
}
////////////////////////////////////////////////////////////////////////
//                Extension -- Stage 5 Functions                      //
////////////////////////////////////////////////////////////////////////

void save_track(Track track, char *name) {
    printf("save_track not implemented yet.\n");

}

Track load_track(char *name) {
    printf("load_track not implemented yet.\n");

    return NULL;
}

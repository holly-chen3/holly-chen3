// Slide
// slide.c
//
// This program was written by HOLLY-CHEN (z5359932)
// on 15-03-2021 to 29-03-2021
//
// Version 1.0.0 (2021-03-08): Initial Assignment Release

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define SIZE 15
#define EMPTY 0
#define STONE 1

// TODO: Add any extra #defines here.

void print_map(int map[SIZE][SIZE], int laser_y);
int move_laser(int map[SIZE][SIZE], int laser_y);
void fire_laser(int map[SIZE][SIZE], int laser_y);
void laser_tnt(int map[SIZE][SIZE], int laser_y, int j);
int game_win_checker(int map[SIZE][SIZE], int laser_y);
int game_lose_checker(int map[SIZE][SIZE], int laser_y);
void shift_left(int map[SIZE][SIZE]);
void shift_left_value(int map[SIZE][SIZE], int row, int col);
int rotate_one(int map[SIZE][SIZE]); 
int rotate_two(int map[SIZE][SIZE]);


// TODO: Add any extra function prototypes here.

int main (void) {
    // This line creates our 2D array called "map" and sets all
    // of the blocks in the map to EMPTY.
    int map[SIZE][SIZE] = {EMPTY};

    // This line creates our laser_y variable. The laser starts in the
    // middle of the map, at position 7.
    int laser_y = SIZE / 2;
    
    int num_blocks;

    printf("How many blocks? ");
    // TODO: Scan in the number of blocks.
    scanf("%d", &num_blocks);

    printf("Enter blocks:\n");
    // TODO: Scan in the blocks.
    int row;
    int col;
    int value;
    int i = 0;
    while (i < num_blocks) {
        scanf("%d %d %d", &row, &col, &value);
        if (row >= 0 && row < SIZE) {
            if (col >= 0 && col < SIZE) {
                map[row][col] = value;
            }
        }
        i++;
    }
    print_map(map, laser_y);
    int keep_looping = 1;
    int input;
    int counter = 0;
    while (scanf("%d", &input) == 1 && keep_looping) {
        if (input == 1) {
            //Move the laser
            //check which direction to go
            laser_y = move_laser(map, laser_y);
        } else if (input == 2) {
            //fire laser, eliminate maximum 4 stones.
            fire_laser(map, laser_y);
            //check if they won the game.
            keep_looping = game_win_checker(map, laser_y);
        } else if (input == 3) {
            //check if they lost the game.
            keep_looping = game_lose_checker(map, laser_y);
            //shift everything left.
            shift_left(map);
        } else if (input == 4 && counter == 0) {
            //Rotate map
            int inp_rotate;
            scanf("%d", &inp_rotate);
            if (inp_rotate == 1) {
                //rotate clockwise
                counter = rotate_one(map);
            } else if (inp_rotate == 2 ) {
                //rotate anti-clockwise
                counter = rotate_two(map);
            } else {
                keep_looping = 1;
            }
        } else if (input == 4 && counter == 1) {
            int inp_rotate;
            scanf("%d", &inp_rotate);
        }
        //'if condition' here so that after game won or lost it does not
        //print another map.
        if (keep_looping) {      
            print_map(map, laser_y);
        }
    }
    
    return 0;
}

// Print out the contents of the map array. 
// Also print out a > symbol to denote the current laser position.
void print_map(int map[SIZE][SIZE], int laser_y) {
    int i = 0;
    while (i < SIZE) {
        if (i == laser_y) {
            printf("> ");
        } else {
            printf("  ");
        }
        int j = 0;
        while (j < SIZE) {
            printf("%d ", map[i][j]);
            j++;
        }
        printf("\n");
        i++;
    }
}
//move the laser up and down.
int move_laser(int map[SIZE][SIZE], int laser_y) {
    int inp_move;
    scanf("%d", &inp_move);
    if (inp_move == 1) {
        //move laser down
        laser_y ++;
        //clamp laser
        if (laser_y >= SIZE) {
            //bottom
            laser_y = SIZE - 1;
        } 
    } else if (inp_move == -1) {
        //move laser up
        laser_y--;
        //clamp laser
        if (laser_y < 0) {
            //top
            laser_y = 0;
        }
    }  
    return laser_y;
}
//fire the laser, laser shoots up to 4 blocks, and can shoot tnt.
void fire_laser(int map[SIZE][SIZE], int laser_y) {
    int j = 0;
    int j_counter = 0;
    while (j < SIZE && j_counter < 4) {
        if (map[laser_y][j] == STONE) {
            map[laser_y][j] = EMPTY;
            j++;
            j_counter++;
        } else if (map[laser_y][j] >= 4 && map[laser_y][j] <= 9) {
            laser_tnt(map, laser_y, j);
            map[laser_y][j] = EMPTY;
            j++;
            j_counter = 4;
        } else if (map[laser_y][j] == EMPTY) {
            j++;
        }
    }
}
//function shows what happens when the laser hits a tnt.
void laser_tnt(int map[SIZE][SIZE], int laser_y, int j) {
    int x = 0;
    while (x < SIZE) {
        int y = 0;
        while (y < SIZE) {
            int d = sqrt((x - laser_y) * (x - laser_y) 
            + (y - j) * (y - j));
            if (d < map[laser_y][j] && d != 0) {
                map[x][y] = EMPTY;
            } else if (d > map[laser_y][j]) {
                map[x][y] = map[x][y];   
            } else if (d == 0) {
                map[x][y] = map[laser_y][j];
            }
            y++;
        }
        x++;
    }
}
//check if they won the game.
int game_win_checker(int map[SIZE][SIZE], int laser_y) {
    int row_check = 0;
    int counter_check = 0;
    while (row_check < SIZE) {
        int col_check = 0;
        while (col_check < SIZE) {
            if (map[row_check][col_check] == EMPTY) {
                counter_check ++;
            } col_check++;
        } row_check++;
    }
    if (counter_check == SIZE*SIZE) {
        print_map(map, laser_y);
        printf("Game Won!\n");
        return 0;
    } else {
        return 1;
    }
}
//check if they lost the game.
int game_lose_checker(int map[SIZE][SIZE], int laser_y) {
    int row_check = 0;
    int col_check = 0;
    int counter_check = 0;
    while (row_check < SIZE && col_check == 0) {
        if (map[row_check][col_check] != EMPTY) {
            counter_check ++;
        } row_check++;
    }
    if (counter_check > 0) {
        print_map(map, laser_y);
        printf("Game Lost!\n");
        return 0;
    } else {
        return 1;
    }
}
//shift everything left.
void shift_left(int map[SIZE][SIZE]) {
    int row = 0;
    while (row < SIZE) {
        int col = 0;
        while (col < SIZE) {
            shift_left_value(map, row, col);
            col++;
        }
        row++;
    }
}
/*function contains an 'if' statement within another 'if' statement
which shifts a value left on the map if it is not equal to EMPTY*/ 
void shift_left_value(int map[SIZE][SIZE], int row, int col) {
    if (map[row][col] != EMPTY) {
        int tmp = map[row][col];
        map[row][col] = EMPTY;
        if (col > 0) {
            col--;
            map[row][col] = tmp;
        } 
    }
}
//function rotate_one rotates everything clockwise
int rotate_one(int map[SIZE][SIZE]) {
    //copy the map 
    int map_copy[SIZE][SIZE];
    int i_copy = 0;
    while (i_copy < SIZE) {
        int j_copy = 0;
        while (j_copy < SIZE) {
            map_copy[i_copy][j_copy] = map[i_copy][j_copy];
            j_copy++;
        } i_copy++;
    }
    int row_rotate = 0;
    while (row_rotate < SIZE) {
        int col_rotate = 0;
        while (col_rotate < SIZE) {
            int tmp_row = col_rotate;
            int tmp_col = SIZE - 1 - row_rotate;
            map[tmp_row][tmp_col] =
            map_copy[row_rotate][col_rotate]; 
            col_rotate++;
        }
        row_rotate++;
    }
    return 1;
}
//function rotates everything anticlockwise.
int rotate_two(int map[SIZE][SIZE]) {
    //copy the map
    int map_copy[SIZE][SIZE];
    int i_copy = 0;
    while (i_copy < SIZE) {
        int j_copy = 0;
        while (j_copy < SIZE) {
            map_copy[i_copy][j_copy] = map[i_copy][j_copy];
            j_copy++;
        } i_copy++;
    }
    int row_rotate = 0;
    while (row_rotate < SIZE) {
        int col_rotate = 0;
        while (col_rotate < SIZE) {
            int tmp_row = SIZE - 1 - col_rotate;
            int tmp_col = row_rotate;
            map[tmp_row][tmp_col] = 
            map_copy[row_rotate][col_rotate];
            col_rotate++;
        }
        row_rotate++;
    }
    return 1;
}



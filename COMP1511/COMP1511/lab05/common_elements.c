// copy all of the values in source1 which are also found in source2 into destination
// return the number of elements copied into destination
//By HOLLY-CHEN (z5359932)
//19-03-2021

int common_elements(int length, int source1[length], int source2[length], int destination[length]) {
    // PUT YOUR CODE HERE (you must change the next line!)
    int i = 0;
    int counter = 0;
    while (i < length) {
        int j = 0;
        int found = 0;
        while (j < length && !found) {
            if (source1[i] == source2[j]) {
                destination[counter] = source1[i];
                counter++;
                found = 1; //1 = true and 0 = false
            }
            j++;
        }
        i++;
    }
    return counter;
}

// You may optionally add a main function to test your common_elements function.
// It will not be marked.
// Only your common_elements function will be marked.

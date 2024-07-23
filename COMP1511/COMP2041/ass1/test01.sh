#!/bin/dash
# Testing correct implementation of pushy-add

PATH="$PATH:$(pwd)"
INDEX=".pushy"

trap 'rm "$expected_output" "$actual_output" -rf "$testingDir"' INT HUP QUIT TERM EXIT

testingDir="$(mktemp -d)"
cd "$testingDir" || exit 1

expected_output="$(mktemp)" 
actual_output="$(mktemp)" 
# HELPER FUNCTION - comparing the actual output to expected output
comp() {
	if diff "$expected_output" "$actual_output" >/dev/null
	then
	   echo "Test $1 passed!"
	else
	   echo "Test $1 failed!"
	fi
	echo '\n'
}
### TEST 01 - test no .pushy found ###
# expected: a .pushy folder does not exist, print error
echo "pushy-add: error: pushy repository directory .pushy not found" > "$expected_output"
pushy-add a > "$actual_output" 2>&1
comp 1

### TEST 02 - test not a regular file ###
# expected: a .pushy folder does not contain the regular file 'a'
echo "Initialized empty pushy repository in .pushy
pushy-add: error: 'a' is not a regular file" > "$expected_output"
{ pushy-init; mkdir a; pushy-add a; } > "$actual_output" 2>&1
comp 2

### TEST 03 - test non existent file ###
# expected: a .pushy folder does not contain file 'a'
rm -r $INDEX 2>/dev/null
rm -r a 2>/dev/null
echo "Initialized empty pushy repository in .pushy
pushy-add: error: can not open 'a'" > "$expected_output"
{ pushy-init; pushy-add a; } > "$actual_output" 2>&1
comp 3
echo "Finished successfully!"

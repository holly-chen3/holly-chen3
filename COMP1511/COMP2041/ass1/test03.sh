#!/bin/dash
# Testing correct implementation of pushy-show (subset 0)

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
### TEST 01 - test show base case ###
# expected: a commit is successfully created and pushy show shows the content of the file
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
line 1" > "$expected_output"
{ pushy-init; echo "line 1" > a; pushy-add a; pushy-commit -m commit-A; pushy-show 0:a; } > "$actual_output" 2>&1
comp 1

### TEST 02 - test show more than 1 file in commit ###
# expected: a commit is successfully created and pushy show shows the content of file b, using :b
rm -r $INDEX
rm a
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
line B" > "$expected_output"
{ pushy-init; echo "line 1" > a; echo "line B" > b; pushy-add a b; pushy-commit -m commit-A; pushy-show :b; } > "$actual_output" 2>&1
comp 2

### TEST 03 - test show nothing in file ###
# expected: a commit is successfully created and pushy show shows the nothing in the file
rm -r $INDEX
rm a b
echo "Initialized empty pushy repository in .pushy
Committed as commit 0" > "$expected_output"
{ pushy-init; touch a; pushy-add a; pushy-commit -m commit-A; pushy-show 0:a; } > "$actual_output" 2>&1
comp 3

### TEST 04 - test show unknown commit ###
# expected: an error
rm -r $INDEX
rm a 
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
pushy-show: error: unknown commit '1'" > "$expected_output"
{ pushy-init; touch a; pushy-add a; pushy-commit -m commit-A; pushy-show 1:a; } > "$actual_output" 2>&1
comp 4

### TEST 05 - test show file not found in commit ###
# expected: an error
rm -r $INDEX
rm a 
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
pushy-show: error: 'b' not found in commit 0" > "$expected_output"
{ pushy-init; touch a; pushy-add a; pushy-commit -m commit-A; pushy-show 0:b; } > "$actual_output" 2>&1
comp 5
echo "Finished successfully!"

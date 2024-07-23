#!/bin/dash
# Testing correct implementation of pushy-commit -m message, pushy-add and pushy-log (subset 0)

PATH="$PATH:$(pwd)"

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
### TEST 01 - test successful commit ###
# expected: a commit is successfully created
echo "Initialized empty pushy repository in .pushy
Committed as commit 0" > "$expected_output"
{ pushy-init; touch a; pushy-add a; pushy-commit -m commit-A; } > "$actual_output" 2>&1
comp 1

### TEST 02 - test nothing to commit ###
# expected: a commit is unsuccessful as there is nothing to commit
echo "nothing to commit" > "$expected_output"
pushy-commit -m commit-B > "$actual_output" 2>&1
comp 2

### TEST 03 - test log base case ###
echo "0 commit-A" > "$expected_output"
pushy-log > "$actual_output" 2>&1
comp 3

### TEST 04 - test adding another commit ###
echo "Committed as commit 1" > "$expected_output"
{ touch b; pushy-add b; pushy-commit -m commit-B; } > "$actual_output" 2>&1
comp 4

### TEST 05 - test log working in the right order when another commit is added ###
echo "1 commit-B
0 commit-A" > "$expected_output"
pushy-log > "$actual_output" 2>&1
comp 5
echo "Finished successfully!"

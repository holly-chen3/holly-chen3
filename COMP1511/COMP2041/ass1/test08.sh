#!/bin/dash
# Testing correct implementation of pushy-branch (subset 2)

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
### TEST 01 - test branch base case ###
# expected: successful creation of new branch
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
b1
master" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -m commit-A; pushy-branch b1; pushy-branch; } > "$actual_output" 2>&1
comp 1

### TEST 02 - test branch cannot start without first commit ###
# expected: error no first commit
rm -r $INDEX
echo "Initialized empty pushy repository in .pushy
pushy-branch: error: this command can not be run until after the first commit" > "$expected_output"
{ pushy-init; pushy-branch b1; } > "$actual_output" 2>&1
comp 2

### TEST 03 - test branch name required ###
# expected: error branch name required
rm -r $INDEX
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
pushy-branch: error: branch name required" > "$expected_output"
{ pushy-init; touch a; pushy-add a; pushy-commit -m commit-A; pushy-branch -d; } > "$actual_output" 2>&1
comp 3
echo "Finished successfully!"

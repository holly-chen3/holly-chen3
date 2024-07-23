#!/bin/dash
# Testing correct implementation of pushy-commit [-a] [-m]  (subset 1)

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
### TEST 01 - test commit -a base case ###
# expected: a commit is successfully created using the -a
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
Committed as commit 1
line 1
line 2" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -m commit-A; echo line 2 >>a; pushy-commit -a -m commit-A; pushy-show 1:a; } > "$actual_output" 2>&1
comp 1

### TEST 02 - test commit -am edge case ###
# expected: a commit is successfully created using -am instead of -a -m
rm -r $INDEX
rm a
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -m commit-A; echo line 2 >>a; pushy-commit -am commit-A; pushy-show 1:a; } > "$actual_output" 2>&1
comp 2

### TEST 03 - test commit -a -m cannot commit without a first commit ###
# expected: a commit is not created
rm -r $INDEX
rm a
echo "Initialized empty pushy repository in .pushy
nothing to commit" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-commit -a -m commit-A; } > "$actual_output" 2>&1
comp 3

### TEST 04 - test commit -a -m can commit without a first commit when file is already in index ###
# expected: a commit is created
rm -r $INDEX
rm a
echo "Initialized empty pushy repository in .pushy
Committed as commit 0" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -a -m commit-A; } > "$actual_output" 2>&1
comp 4
echo "Finished successfully!"

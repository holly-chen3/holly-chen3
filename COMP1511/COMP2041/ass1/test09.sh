#!/bin/dash
# Testing correct implementation of pushy-branch and pushy-checkout (subset 2)

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
### TEST 01 - test branch and checkout ###
# expected: successful checkout of new branch
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
Switched to branch 'b1'" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -m commit-A; pushy-branch b1; pushy-checkout b1; } > "$actual_output" 2>&1
comp 1

### TEST 02 - test unable to checkout ###
# expected: unsuccessful checkout
rm -r $INDEX
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
pushy-checkout: error: unknown branch 'b1'" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -m commit-A; pushy-checkout b1; } > "$actual_output" 2>&1
comp 2
echo "Finished successfully!"

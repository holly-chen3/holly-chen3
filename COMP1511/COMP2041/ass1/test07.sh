#!/bin/dash
# Testing correct implementation of pushy-status in more detail (subset 1)

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
### TEST 01 - test status in more detail ###
# expected: status prints out correct output
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
a - same as repo
b - file changed, changes not staged for commit
c - file changed, different changes staged for commit
d - file changed, changes staged for commit
e - file changed, different changes staged for commit
f - file deleted" > "$expected_output"
{ pushy-init; touch a b c d e f; pushy-add a b c d e f; pushy-commit -m commit-A; echo line 1 > b; echo line 1 > c; echo line 1 > d; echo line 1 > e; pushy-add c d e; echo -n "" > c; echo line 2 >> e; rm f; pushy-status; } > "$actual_output" 2>&1
comp 1

### TEST 02 - test status file deleted ###
# expected: status prints out correct output
rm -r $INDEX
rm a b c d e
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
a - file deleted, changes staged for commit
b - deleted from index
c - deleted from index
d - added to index
e - added to index, file changed
f - file deleted, deleted from index
g - added to index, file deleted
h - untracked
i - untracked" > "$expected_output"
{ pushy-init; touch a b c d e f g h i; pushy-add a b c f; pushy-commit -m commit-B; echo line 1 > a; echo line 1 > c; pushy-add a d e g; echo line 1 > e; rm a g; pushy-rm --force --cached b c; pushy-rm f; pushy-status; } > "$actual_output" 2>&1 
comp 2
echo "Finished successfully!"

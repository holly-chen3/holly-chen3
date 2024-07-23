#!/bin/dash
# Testing correct implementation of pushy-status (subset 1)

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
### TEST 01 - test status ###
# expected: status prints out correct output
echo "Initialized empty pushy repository in .pushy
a - added to index
b - added to index
c - added to index
d - untracked
e - untracked
f - untracked
Committed as commit 0
a - same as repo
b - same as repo
c - same as repo
d - untracked
e - untracked
f - untracked" > "$expected_output"
{ pushy-init; touch a b c d e f; pushy-add a b c; pushy-status; pushy-commit -m commit-A; pushy-status; } > "$actual_output" 2>&1
comp 1
### TEST 02 - test status - changing files ###
# expected: status prints out correct output
echo "a - file changed, changes staged for commit
b - file changed, different changes staged for commit
c - same as repo
d - untracked
e - untracked
f - untracked
Committed as commit 1
a - same as repo
b - file changed, changes not staged for commit
c - same as repo
d - untracked
e - untracked
f - untracked" > "$expected_output"
{ echo hi >> a; pushy-add a; echo hi >> b; pushy-add b; echo bye >> b; pushy-status; pushy-commit -m commit-B; pushy-status; } > "$actual_output" 2>&1
comp 2
### TEST 03 - test status - deleting files ###
# expected: status prints out correct output
echo "a - file deleted, deleted from index
b - file deleted, deleted from index
c - same as repo
d - untracked
e - untracked
f - untracked
a - file deleted, deleted from index
b - file deleted, deleted from index
c - same as repo
e - untracked
f - untracked
a - file deleted, deleted from index
b - file deleted, deleted from index
c - file deleted, deleted from index
e - untracked
f - untracked" > "$expected_output"
{ pushy-rm a; pushy-rm --force b; pushy-status; pushy-add d; pushy-rm --force d; pushy-status; pushy-rm c; pushy-status; } > "$actual_output" 2>&1
comp 3
echo "Finished successfully!"

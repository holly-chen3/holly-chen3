#!/bin/dash
# Testing correct implementation of pushy-rm (subset 1)

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
### TEST 01 - test rm base case ###
# expected: remove file from current directory and index
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
ls: cannot access 'a': No such file or directory" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -m commit-A; pushy-rm a; ls a; } > "$actual_output" 2>&1
comp 1

### TEST 02 - test rm --force --cached ###
# expected: force remove file from index
rm -r $INDEX
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
pushy-rm: error: 'a' has staged changes in the index
ls: cannot access '.pushy/.index/a': No such file or directory
a
pushy-rm: error: 'a' is not in the pushy repository
a" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -m commit-A; echo line 2 >> a; pushy-add a; pushy-rm a; pushy-rm --force --cached a; ls .pushy/.index/a; 
ls a; pushy-rm --force a; ls a; } > "$actual_output" 2>&1
comp 2

### TEST 03 - test rm --force ###
# expected: force remove file from current directory and index
rm -r $INDEX
rm a
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
pushy-rm: error: 'a' has staged changes in the index
ls: cannot access '.pushy/.index/a': No such file or directory
ls: cannot access 'a': No such file or directory" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -m commit-A; echo line 2 >> a; pushy-add a; pushy-rm a; pushy-rm --force a; ls .pushy/.index/a; 
ls a; } > "$actual_output" 2>&1
comp 3

### TEST 04 - test rm --cached ###
# expected: remove file from index only
rm -r $INDEX
echo "Initialized empty pushy repository in .pushy
Committed as commit 0
ls: cannot access '.pushy/.index/a': No such file or directory
a" > "$expected_output"
{ pushy-init; echo line 1 > a; pushy-add a; pushy-commit -m commit-A; pushy-rm --cached a; ls .pushy/.index/a; 
ls a; } > "$actual_output" 2>&1
comp 4
echo "Finished successfully!"

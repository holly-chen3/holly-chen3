#!/bin/dash
# Testing correct implementation of subset 1: multiple commands

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
### TEST 01 - test base case ###
# expected: multiple commands work
cat > "$expected_output" <<EOF
1
2
3
3
4
EOF
seq 1 5 | eddy.py '3p;4q' > "$actual_output" 2>&1
comp 1

### TEST 02 - test regex case ###
# expected: multiple commands with regex
cat > "$expected_output" <<EOF
9
10
11
11
13
13
14
EOF
seq 9 15 | eddy.py '/3/p;/2/s/2/1/;/4/q' > "$actual_output" 2>&1
comp 2

### TEST 03 - test delete multiple command ###
# expected: does not call other functions after delete if it is on the same line
cat > "$expected_output" <<EOF
1
3
4
5
6
7
EOF
seq 1 7 | eddy.py '/2/d;/2/p' > "$actual_output" 2>&1
comp 3

### TEST 04 - test print and quit ###
# expected: prints the line twice and then quits
cat > "$expected_output" <<EOF
Hallo Holly
Hallo Holly
EOF
echo Hallo Holly | eddy.py '/a/p;/y/q' > "$actual_output" 2>&1
comp 4

### TEST 05 - reverse of test01 ###
# expected: multiple commands work
cat > "$expected_output" <<EOF
1
2
3
3
4
EOF
seq 1 5 | eddy.py '4q;3p' > "$actual_output" 2>&1
comp 5

### TEST 06 - reverse of test02 ###
# expected: multiple commands with regex
cat > "$expected_output" <<EOF
9
10
11
11
13
13
14
EOF
seq 9 15 | eddy.py '/4/q;/2/s/2/1/;/3/p' > "$actual_output" 2>&1
comp 6

### TEST 07 - reverse of test03 ###
# expected: does not call other functions after delete if it is on the same line
cat > "$expected_output" <<EOF
1
3
4
5
6
7
EOF
seq 1 7 | eddy.py '/2/d;/2/p' > "$actual_output" 2>&1
comp 7

### TEST 08 - reverse of test04 ###
# expected: prints the line once and then quits
cat > "$expected_output" <<EOF
Hallo Holly
EOF
echo Hallo Holly | eddy.py '/y/q;/a/p' > "$actual_output" 2>&1
comp 8
echo "Finished successfully!"
#!/bin/dash
# Testing correct implementation of subset 0: quit command

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
# expected: quits at the right index
cat > "$expected_output" <<EOF
1
2
3
4
5
EOF
seq 1 10 | eddy.py '5q' > "$actual_output" 2>&1
comp 1

### TEST 02 - test regex case ###
# expected: quits at the right regex
cat > "$expected_output" <<EOF
1
2
3
4
5
6
7
EOF
seq 1 10 | eddy.py '/^7/q' > "$actual_output" 2>&1
comp 2

### TEST 03 - test infinite standard input ###
# expected: quits when there is an infinite number of lines from standard input
cat > "$expected_output" <<EOF
y
y
y
y
EOF
yes | eddy.py '4q' > "$actual_output" 2>&1
comp 3

### TEST 04 - test q quits the whole program after ###
# expected: quits the whole program
cat > "$expected_output" <<EOF
1
2
3
4
EOF
seq 1 10 | eddy.py '4q; 10p' > "$actual_output" 2>&1
comp 4
echo "Finished successfully!"
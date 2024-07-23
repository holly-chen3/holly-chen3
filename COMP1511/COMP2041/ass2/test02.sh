#!/bin/dash
# Testing correct implementation of subset 0: d command

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
# expected: deletes the given index
cat > "$expected_output" <<EOF
1
2
3
5
EOF
seq 1 5 | eddy.py '4d' > "$actual_output" 2>&1
comp 1

### TEST 02 - test regex case ###
# expected: deletes the given regular expressions
cat > "$expected_output" <<EOF
9
20
21
22
EOF
seq 9 22 | eddy.py '/^1./d' > "$actual_output" 2>&1
comp 2

### TEST 03 - test regex for non digits ###
# expected: prints again for non digits
cat > "$expected_output" <<EOF
EOF
echo Hello | eddy.py '/Hello/d' > "$actual_output" 2>&1
comp 3
echo "Finished successfully!"
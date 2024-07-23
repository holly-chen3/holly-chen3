#!/bin/dash
# Testing correct implementation of subset 0: p command

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
# expected: prints again at the right index
cat > "$expected_output" <<EOF
1
2
3
4
5
5
EOF
seq 1 5 | eddy.py '5p' > "$actual_output" 2>&1
comp 1

### TEST 02 - test regex case ###
# expected: prints again at the right regular expression
cat > "$expected_output" <<EOF
10
11
12
13
14
14
15
16
EOF
seq 10 16 | eddy.py '/^.4/p' > "$actual_output" 2>&1
comp 2

### TEST 03 - test regex for non digits ###
# expected: prints again for non digits
cat > "$expected_output" <<EOF
Hello
Hello
EOF
echo Hello | eddy.py '/Hello/p' > "$actual_output" 2>&1
comp 3

### TEST 04 - test -n command line ###
# expected: stops input lines being printed
cat > "$expected_output" <<EOF
1
10
EOF
seq 1 10 | eddy.py -n '/1/p' > "$actual_output" 2>&1
comp 4
echo "Finished successfully!"
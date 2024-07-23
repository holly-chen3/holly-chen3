#!/bin/dash
# Testing correct implementation of subset 1: s command

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
# expected: substitutes the specified regex with a non-whitespace 
# character as the delimiter
cat > "$expected_output" <<EOF
hehe
2
3
4
hehe
EOF
seq 1 5 | eddy.py 'sJ[15]JheheJ' > "$actual_output" 2>&1
comp 1

### TEST 02 - test global case ###
# expected: substitutes every specified regex
# with a non-whitespace character as the delimiter
cat > "$expected_output" <<EOF
9
hi0
hihi
hihi
hi3
hi4
EOF
seq 9 14 | eddy.py 'sJ[12]JhiJg' > "$actual_output" 2>&1
comp 2

### TEST 03 - test regex for non digits ###
# expected: substitutes non digits
cat > "$expected_output" <<EOF
Hilli Hilly
EOF
echo Hallo Holly | eddy.py 'sW[oa]WiWg' > "$actual_output" 2>&1
comp 3

### TEST 04 - test error ###
# expected: substitutes non digits
cat > "$expected_output" <<EOF
eddy: command line: invalid command
EOF
echo Hallo Holly | eddy.py 'sW[oa]WiWWg' > "$actual_output" 2>&1
comp 4
echo "Finished successfully!"